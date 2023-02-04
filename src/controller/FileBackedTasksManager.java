package controller;
import exceptions.ManagerSaveException;
import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm | dd.MM.yyyy");
    private final File file;
    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    /**
     * Метод, сохраняющий текущее состояние менеджера в указанный файл
     */
    private void save() {
        String PATH = "resources/task.csv";
        File file = new File(PATH);
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            String HEADER = "id,type,name,status,description,startTime,endTime,duration,epic\n";
            writer.append(HEADER);
            for (Map.Entry<Integer, SimpleTask> entry : taskRepo.entrySet())
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
                    task = new SimpleTask(fields[2], fields[4], fields[5], Integer.parseInt(fields[7]));
                    task.setId(Integer.parseInt(fields[0]));
                    task.setStatus(Status.valueOf(fields[3]));
                    task.getEndTime();
                    break;
                case EPIC:
                    task = new Epic(fields[2], fields[4]);
                    task.setId(Integer.parseInt(fields[0]));
                    task.setStatus(Status.valueOf(fields[3]));
                    if (!fields[5].equals("null")) {
                        task.setStartTime(LocalDateTime.parse(fields[5], formatter));
                        task.setDuration(Duration.ofMinutes(Long.parseLong(fields[7])));
                        setEpicEndTime((Epic) task);
                    }
                    break;
                case SUBTASK:
                    task = new SubTask(fields[2], fields[4], fields[5],
                            Integer.parseInt(fields[7]), Integer.parseInt(fields[8]));
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
                        format(formatter) + "," + task.getEndTime().format(formatter) + "," +
                        task.getDuration().toMinutes() + "," + ((SubTask) task).getEpicId() + "\n";
            else if (task instanceof SimpleTask)
                return task.getId() + "," + TaskType.SIMPLETASK + "," + task.getTaskName() + "," +
                        task.getStatus() + "," + task.getTaskDescription() + "," + task.getStartTime().
                        format(formatter) + "," + task.getEndTime().format(formatter) + "," +
                        task.getDuration().toMinutes() + "\n";
            else {
                return task.getId() + "," + TaskType.EPIC + "," + task.getTaskName() + "," + task.getStatus() +
                        "," + task.getTaskDescription() + "," + task.getStartTime().format(formatter) + "," +
                        task.getEndTime().format(formatter) + "," + task.getDuration().toMinutes() + "\n";
            }
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
                if (task instanceof SimpleTask) {
                    taskRepo.put(id, (SimpleTask) task);
                    mapOfPrioritizedTasks.put(task.getStartTime(), task);
                }
                if (task instanceof Epic) {
                    epicRepo.put(id, (Epic) task);
                }
                if (task instanceof SubTask) {
                    subtaskRepo.put(id, (SubTask) task);
                    mapOfPrioritizedTasks.put(task.getStartTime(), task);
                    if (epicRepo.containsKey(((SubTask) task).getEpicId())) {
                        Epic epic = epicRepo.get(((SubTask) task).getEpicId());
                        epic.getSubtasksIds().add(id);
                        setEpicEndTime(epic);
                    }
                }
                if (maxId < id)
                    maxId = id;
                generatorId = maxId;
            }
            content = reader.readLine();
            for (Integer historyId : historyFromString(content)) {
                if (taskRepo.containsKey(historyId))
                    getSimpleTask(historyId);
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
    public int createSimpleTask(SimpleTask task) {
        super.createSimpleTask(task);
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
    public void deleteSimpleTask(int id) {
        super.deleteSimpleTask(id);
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
    public void deleteAllSimpleTasks() {
        super.deleteAllSimpleTasks();
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
    public SimpleTask updateSimpleTask(SimpleTask task) {
        super.updateSimpleTask(task);
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
    public SimpleTask getSimpleTask(int id) {
        final SimpleTask simpleTask =  super.getSimpleTask(id);
        save();
        return simpleTask;
    }

    @Override
    public SimpleTask getSimpleTaskWithoutHistory(int id) {
        final SimpleTask simpleTask = super.getSimpleTask(id);
        save();
        return simpleTask;
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

    /**
     * Статический метод, проверяющий работу сохранения и восстановления менеджера из файла (сериализация)
     */
    public static void main(String[] args) {

        File file = new File("resources/task.csv");
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);

        System.out.println("------------------Создание задач--------------------------");
        SimpleTask simpleTask1 = new SimpleTask("Задача 1", "Описание задачи 1",
                "09:00 | 09.01.2023", 60);
        fileBackedTasksManager.createSimpleTask(simpleTask1);

        SimpleTask simpleTask2 = new SimpleTask("Задача 2", "Описание задачи 2",
                "22:00 | 11.01.2023", 30);
        fileBackedTasksManager.createSimpleTask(simpleTask2);

        System.out.println("Задача 1 начата: " + simpleTask1.getStartTime().format(DateTimeFormatter.
                ofPattern("HH:mm | dd.MM.yyyy")) + "; длительность: " + simpleTask1.getDuration().toMinutes() +
                "; задача 1 окончена: " + simpleTask1.getEndTime());
        System.out.println("Задача 2 начата: " + simpleTask2.getStartTime().format(DateTimeFormatter.
                ofPattern("HH:mm | dd.MM.yyyy")) + "; длительность: " + simpleTask2.getDuration().toMinutes() +
                "; задача 2 окончена: " + simpleTask2.getEndTime());

        System.out.println("------------------------------Создание эпика------------------------------");
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        fileBackedTasksManager.createEpic(epic1);

        System.out.println("Эпик без подзадач начат: " + epic1.getStartTime().format(DateTimeFormatter.
                ofPattern("HH:mm | dd.MM.yyyy")) + "; длительность: " + epic1.getDuration().toMinutes() +
                "; эпик без подзадач окончен: " + epic1.getEndTime().format(DateTimeFormatter.
                ofPattern("HH:mm | dd.MM.yyyy")));

        System.out.println("-------------------Создание подзадач--------------------------");
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1 эпика1",
                "22:15 | 08.01.2023", 20, epic1.getId());

        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2 эпика1",
                "10:00 | 10.01.2023", 15, epic1.getId());
        fileBackedTasksManager.createSubTask(subTask1);

        System.out.println("Подзадача 1 начата: " + subTask1.getStartTime().format(DateTimeFormatter.
                ofPattern("HH:mm | dd.MM.yyyy")) + "; длительность: " + subTask1.getDuration().toMinutes() +
                "; подзадача окончена: " + subTask1.getEndTime().format(DateTimeFormatter.
                ofPattern("HH:mm | dd.MM.yyyy")));
        fileBackedTasksManager.createSubTask(subTask2);

        System.out.println("Подзадача 2 начата: " + subTask2.getStartTime().format(DateTimeFormatter.
                ofPattern("HH:mm | dd.MM.yyyy")) + "; длительность: " + subTask2.getDuration().toMinutes() +
                "; подзадача окончена: " + subTask2.getEndTime().format(DateTimeFormatter.
                ofPattern("HH:mm | dd.MM.yyyy")));

        System.out.println("Эпик с подзадачами начат: " + epic1.getStartTime().format(DateTimeFormatter.
                ofPattern("HH:mm | dd.MM.yyyy")) + "; длительность: " + epic1.getDuration().toMinutes() +
                "; эпик с подзадачами окончен: " + epic1.getEndTime().format(DateTimeFormatter.
                ofPattern("HH:mm | dd.MM.yyyy")));

        System.out.println("\nИзменение статуса различных типов задач----------------------------");
        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.DONE);
        simpleTask1.setStatus(Status.IN_PROGRESS);
        System.out.println("\nЗадачи----------------------------");
        for (Task task : fileBackedTasksManager.getSimpleTaskList())
            System.out.println(task);
        System.out.println("\nПодзадачи------------------------");
        for (Task task : fileBackedTasksManager.getSubTaskList()) {
            System.out.println(task);
        }
        System.out.println("\nЭпики-----------------------------");
        for (Task task : fileBackedTasksManager.getEpicList()) {
            System.out.println(task);
        }

        fileBackedTasksManager.getSimpleTask(simpleTask1.getId());
        fileBackedTasksManager.getEpic(epic1.getId());
//        fileBackedTasksManager.getSimpletask(1);
        System.out.println("\nИстория задач--------------------------");
        for (Task task : fileBackedTasksManager.getHistory()) {
            System.out.println(task);
        }
        System.out.println("\nОтсортированные задачи-----------------------");
        for (Task task : fileBackedTasksManager.getPrioritizedTasks()) {
            System.out.println(task);
        }

        FileBackedTasksManager.loadFromFile(file);
    }
}
