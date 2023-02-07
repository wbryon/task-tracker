package controller;
import exceptions.ManagerSaveException;
import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private File file;

    public FileBackedTasksManager() {
    }
    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    /**
     * Метод, сохраняющий текущее состояние менеджера в указанный файл
     */
    protected void save() {
        String PATH = "resources/task.csv";
        File file = new File(PATH);
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            String HEADER = "id,type,name,status,description,startTime,endTime,duration,epic\n";
            writer.append(HEADER);
            for (Map.Entry<Integer, Task> entry : taskRepo.entrySet())
                writer.append(toString(entry.getValue()));
            for (Map.Entry<Integer, Epic> entry : epicRepo.entrySet())
                writer.append(toString(entry.getValue()));
            for (Map.Entry<Integer, SubTask> entry : subtaskRepo.entrySet())
                writer.append(toString(entry.getValue()));
            writer.newLine();
            writer.append(historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Error occurred writing to file " + file.getName());
        }
    }
        /**
         * Метод, создающий задачу из строки
         */
        public Task fromString(String value) {
            Task task = null;
            final String[] fields = value.split(",");

            switch (TaskType.valueOf(fields[1])) {
                case SIMPLETASK:
                    task = new Task(fields[2], fields[4], LocalDateTime.parse(fields[5], LocalDateAdapter.formatter),
                            Duration.ofMinutes(Integer.parseInt(fields[7])));
                    task.setId(Integer.parseInt(fields[0]));
                    task.setStatus(Status.valueOf(fields[3]));
                    task.getEndTime();
                    break;
                case EPIC:
                    task = new Epic(fields[2], fields[4]);
                    task.setId(Integer.parseInt(fields[0]));
                    task.setStatus(Status.valueOf(fields[3]));
                    if (!fields[5].equals("null")) {
                        task.setStartTime(LocalDateTime.parse(fields[5], LocalDateAdapter.formatter));
                        task.setDuration(Duration.ofMinutes(Long.parseLong(fields[7])));
                        setEpicEndTime((Epic) task);
                    }
                    break;
                case SUBTASK:
                    task = new SubTask(fields[2], fields[4], LocalDateTime.parse(fields[5], LocalDateAdapter.formatter),
                            Duration.ofMinutes(Integer.parseInt(fields[7])), Integer.parseInt(fields[8]));
                    task.setId(Integer.parseInt(fields[0]));
                    task.setStatus(Status.valueOf(fields[3]));
                    task.getEndTime();
                    break;
            }
            return task;
        }

        /**
         * Метод, сохраняющий задачу в строку
         */
        public String toString(Task task) {
                if (task instanceof SubTask)
                return task.getId() + "," + TaskType.SUBTASK + "," + task.getTaskName() + "," +
                        task.getStatus() + "," + task.getTaskDescription() + "," + task.getStartTime().
                        format(LocalDateAdapter.formatter) + "," + task.getEndTime().format(LocalDateAdapter.formatter) + "," +
                        task.getDuration().toMinutes() + "," + ((SubTask) task).getEpicId() + "\n";
                else if (task instanceof Epic) {
                return task.getId() + "," + TaskType.EPIC + "," + task.getTaskName() + "," + task.getStatus() +
                        "," + task.getTaskDescription() + "," + task.getStartTime().format(LocalDateAdapter.formatter) + "," +
                        task.getEndTime().format(LocalDateAdapter.formatter) + "," + task.getDuration().toMinutes() + "\n";
            }
            else
                return task.getId() + "," + TaskType.SIMPLETASK + "," + task.getTaskName() + "," +
                        task.getStatus() + "," + task.getTaskDescription() + "," + task.getStartTime().
                        format(LocalDateAdapter.formatter) + "," + task.getEndTime().format(LocalDateAdapter.formatter) + "," +
                        task.getDuration().toMinutes() + "\n";
        }

        /**
         * Статический метод, сохраняющий задачи и историю просмотра задач в файл CSV
         */
        public static String historyToString(HistoryManager manager) {
            if (manager.getHistory().isEmpty())
                return "0";
            StringBuilder stringBuilder = new StringBuilder();
            for (Task task : manager.getHistory())
                stringBuilder.append(task.getId()).append(",");
            return stringBuilder.substring(0, stringBuilder.toString().length() - 1);
        }

        /**
         * Статический метод восстанавливающий менеджер истории из CSV
         */
        public static List<Integer> historyFromString(String value) {
            final String[] taskId = value.split(",");
            List<Integer> history = new ArrayList<>();
            for (String id: taskId)
                history.add(Integer.parseInt(id));
            return history;
        }

    /**
     * Метод для чтения задач и истории из файла
     */
    private void load() {
        int maxId = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String content = reader.readLine();
            while (content != null) {
                content = reader.readLine();
                if (content.isEmpty())
                    break;
                final Task task = fromString(content);
                final int id = task != null ? task.getId() : 0;
                if (task instanceof Epic) {
                    epicRepo.put(id, (Epic) task);
                    return;
                }
                if (task instanceof SubTask) {
                    subtaskRepo.put(id, (SubTask) task);
                    mapOfPrioritizedTasks.put(task.getStartTime(), task);
                    if (epicRepo.containsKey(((SubTask) task).getEpicId())) {
                        Epic epic = epicRepo.get(((SubTask) task).getEpicId());
                        epic.getSubtasksIds().add(id);
                        setEpicEndTime(epic);
                    }
                    return;
                }
                if (task instanceof Task) {
                    taskRepo.put(id, task);
                    mapOfPrioritizedTasks.put(task.getStartTime(), task);
                    return;
                }
                if (maxId < id)
                    maxId = id;
                generatorId = maxId;
            }
            content = reader.readLine();
            for (Integer historyId : historyFromString(content)) {
                if (taskRepo.containsKey(historyId))
                    getTask(historyId);
                if (epicRepo.containsKey(historyId))
                    getEpic(historyId);
                if (subtaskRepo.containsKey(historyId))
                    getSubTask(historyId);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Error occurred reading file " + file.getName());
        }
    }

    /**
     * Статический метод, восстанавливающий данные менеджера из файла при запуске программы
     */
    protected static FileBackedTasksManager loadFromFile(File file) {
        final FileBackedTasksManager manager = new FileBackedTasksManager(file);
        manager.load();
        return manager;
    }

    @Override
    public int createTask(Task task) {
        super.createTask(task);
        save();
        return task.getId();
    }

    @Override
    public int createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return epic.getId();
    }

    @Override
    public int createSubTask(SubTask subtask) {
        super.createSubTask(subtask);
        save();
        return subtask.getId();
    }

    @Override
    public ArrayList<Task> getTaskList() {
        ArrayList<Task> taskList =  super.getTaskList();
        save();
        return taskList;
    }

    @Override
    public ArrayList<Epic> getEpicList() {
        ArrayList<Epic> epicList = super.getEpicList();
        save();
        return epicList;
    }

    @Override
    public ArrayList<SubTask> getSubTaskList() {
        ArrayList<SubTask> subTaskList = super.getSubTaskList();
        save();
        return subTaskList;
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubTask(int id) {
        super.deleteSubTask(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public Task updateTask(Task task) {
        super.updateTask(task);
        save();
        return task;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
        return epic;
    }

    @Override
    public SubTask updateSubTask(SubTask subtask) {
        super.updateSubTask(subtask);
        save();
        return subtask;
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        super.updateEpicStatus(epic);
        save();
    }

    @Override
    public Task getTask(int id) {
        final Task task =  super.getTask(id);
        save();
        return task;
    }

    @Override
    public Task getTaskWithoutHistory(int id) {
        final Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        final Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public Epic getEpicWithoutHistory(int id) {
        final Epic epic = super.getEpicWithoutHistory(id);
        save();
        return epic;
    }

    @Override
    public SubTask getSubTask(int id) {
        final SubTask subTask = super.getSubTask(id);
        save();
        return subTask;
    }

    @Override
    public SubTask getSubTaskWithoutHistory(int id) {
        final SubTask subTask = super.getSubTaskWithoutHistory(id);
        save();
        return subTask;
    }

    private enum TaskType {
        SIMPLETASK, EPIC, SUBTASK
    }

    @Override
    public LocalDateTime setEpicStartTime(Epic epic) {
        LocalDateTime localDateTime = super.setEpicStartTime(epic);
        save();
        return localDateTime;
    }

    @Override
    public LocalDateTime setEpicEndTime(Epic epic) {
        LocalDateTime localDateTime = super.setEpicEndTime(epic);
        save();
        return localDateTime;
    }

    @Override
    public Duration setEpicDuration(Epic epic) {
        Duration duration = super.setEpicDuration(epic);
        save();
        return duration;
    }

    @Override
    public Duration updateEpicDuration(Epic epic) {
        Duration duration = super.updateEpicDuration(epic);
        save();
        return duration;
    }
}
