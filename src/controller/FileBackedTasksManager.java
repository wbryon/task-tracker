package controller;
import exceptions.ManagerSaveException;
import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final String PATH = "resources/task.csv";
    private final File file;
    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    /**
     * Метод, сохраняющий текущее состояние менеджера в указанный файл
     */
    private void save() {
        File file = new File(PATH);
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            String HEADER = "id,type,name,status,description,epic\n";
            writer.append(HEADER);
            for (Map.Entry<Integer, SimpleTask> entry : taskRepo.entrySet()) {
                writer.append(toString(entry.getValue()));
            }
            for (Map.Entry<Integer, Epic> entry : epicRepo.entrySet()) {
                writer.append(toString(entry.getValue()));
            }
            for (Map.Entry<Integer, SubTask> entry : subtaskRepo.entrySet()) {
                writer.append(toString(entry.getValue()));
            }
            writer.newLine();
            if (historyManager.getHistory() != null)
                writer.append(historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Error occurred writing to file " + file.getName());
        }
    }

    /**
     * Метод, создающий задачу из строки
     */
    private Task fromString(String value) {
        Task task;
        final String[] fields = value.split(",");

        if (fields[1].toUpperCase().equals(TaskType.SIMPLETASK.toString()))
            task = new SimpleTask(fields[1], fields[4]);
        else if (fields[1].toUpperCase().equals(TaskType.EPIC.toString()))
            task = new Epic(fields[1], fields[4]);
        else if (fields[1].toUpperCase().equals(TaskType.SUBTASK.toString()))
            task = new SubTask(fields[1], fields[4], Integer.parseInt(fields[5]));
        else
            return null;
        task.setId(Integer.parseInt(fields[0]));
        task.setStatus(Status.valueOf(fields[3].toUpperCase()));
        task.setTaskName(fields[2]);
        return task;
    }

    /**
     * Метод, сохраняющий задачу в строку
     */
    private String toString(Task task) {
        if (task instanceof SubTask)
            return task.getId() + "," + TaskType.SUBTASK + "," + task.getTaskName() + "," +
                    task.getStatus() + "," + task.getTaskDescription() + "," + ((SubTask) task).getEpicId() + "\n";
        else if (task instanceof SimpleTask)
            return task.getId() + "," + TaskType.SIMPLETASK + "," + task.getTaskName() + "," +
                    task.getStatus() + "," + task.getTaskDescription() + "\n";
        else
            return task.getId() + "," + TaskType.EPIC + "," + task.getTaskName() + "," +
                    task.getStatus() + "," + task.getTaskDescription() + "\n";
    }

    /**
     * Статический метод, сохраняющий задачи и историю просмотра задач в файл CSV
     */
    public static String historyToString(HistoryManager manager) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < manager.getHistory().size() - 1; i++)
            stringBuilder.append(manager.getHistory().get(i).getId()).append(",");
        return stringBuilder.append(manager.getHistory().get(manager.getHistory().size() - 1).getId()).toString();
    }

    /**
     * Статический метод восстанавливающий менеджер истории из CSV
     */
    public static List<Integer> historyFromString(String value) {
        final String[] taskId = value.split(",");
        List<Integer> history = new ArrayList<>();
        for (String id: taskId)
            history.add(Integer.valueOf(id));
        return history;
    }

    /**
     * Метод для чтения задач и истории из файла
     */
    private void load() {
        int maxId = 0;
        try {
            String contents = Files.readString(Paths.get(PATH), StandardCharsets.UTF_8);
            String[] arr = contents.split("\n");
            for (int i = 1; i < arr.length - 2; i++) {
                final Task task = fromString(arr[i]);
                final  int id = task != null ? task.getId() : 0;
                if (task instanceof SimpleTask)
                    taskRepo.put(id, (SimpleTask) task);
                if (task instanceof Epic)
                    epicRepo.put(id, (Epic) task);
                if (task instanceof SubTask)
                    subtaskRepo.put(id, (SubTask) task);
                if (maxId < id)
                    maxId = id;
            }
            generatorId = maxId;
            for (Integer id : historyFromString(arr[arr.length - 1])) {
                if (taskRepo.containsKey(id))
                    getSimpleTask(id);
                if (epicRepo.containsKey(id))
                    getEpic(id);
                if (subtaskRepo.containsKey(id))
                    getSubtask(id);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Error occurred reading file" + file.getName());
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
    public int addNewSimpleTask(SimpleTask task) {
        super.addNewSimpleTask(task);
        save();
        return task.getId();
    }

    @Override
    public int addNewEpic(Epic epic) {
        super.addNewEpic(epic);
        save();
        return epic.getId();
    }

    @Override
    public int addNewSubtask(SubTask subtask) {
        super.addNewSubtask(subtask);
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
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
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
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void updateSimpleTask(SimpleTask task) {
        super.updateSimpleTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(SubTask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        super.updateEpicStatus(epic);
        save();
    }

    @Override
    public SimpleTask getSimpleTask(int id) {
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
    public SubTask getSubtask(int id) {
        final SubTask subTask = super.getSubtask(id);
        save();
        return subTask;
    }

    private enum TaskType {
        SIMPLETASK, EPIC, SUBTASK
    }

    /**
     * Статический метод, проверяющий работу сохранения и восстановления менеджера из файла (сериализация)
     */
    public static void main(String[] args) {
        File tasks = new File("resources/task.csv");
        FileBackedTasksManager manager = new FileBackedTasksManager(tasks);
        SimpleTask task1 = new SimpleTask("Задача1", "Описание задачи 1");
        manager.addNewSimpleTask(task1);

        SimpleTask task2 = new SimpleTask("Задача2", "Описание задачи 2");
        manager.addNewSimpleTask(task2);

        Epic epic = new Epic("Эпик1", "Описание эпика 1");
        int epicId1 = manager.addNewEpic(epic);

        SubTask subtask1 = new SubTask("Подзадача1", "Описание подзадачи 1 эпика1", epicId1);
        manager.addNewSubtask(subtask1);

        SubTask subtask2 = new SubTask("Подзадача2", "Описание подзадачи2 эпика1", epicId1);
        manager.addNewSubtask(subtask2);

        manager.getSimpleTask(1);
        manager.getEpic(3);
        manager.getSubtask(4);
        manager.getSubtask(5);
        System.out.println("-----------load from file---------------");
        FileBackedTasksManager manager1 = FileBackedTasksManager.loadFromFile(tasks);
        for (Integer id : manager1.taskRepo.keySet()) {
            System.out.println(manager1.taskRepo.get(id));
        }
        for (Integer id : manager1.epicRepo.keySet()) {
            System.out.println(manager1.epicRepo.get(id));
        }
        for (Integer id : manager1.subtaskRepo.keySet()) {
            System.out.println(manager1.subtaskRepo.get(id));
        }
        for (Task task : manager1.getHistory()) {
            System.out.println("id: " + task.getId());
        }
    }
}
