package controller;

import model.Epic;
import model.SimpleTask;
import model.SubTask;
import model.Task;

import java.io.File;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    File file;
    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    /**
     * Метод, сохраняющий текущее состояние менеджера в указанный файл
     */
    public int save() {
        return 0;
    }

    /**
     * Метод, сохраняющий задачи в файл CSV
     */
    public String toString(Task task) {
        return null;
    }

    /**
     * Метод, создающий задачи из строки
     */
    public Task fromString(String value) {
        return null;
    }

    /**
     * Статический метод, сохраняющий историю
     */
    public static String historyToString(HistoryManager manager) {
        return null;
    }

    /**
     * Статический метод восстанавливающий менеджер истории из CSV
     */
    public static List<Integer> historyFromString(String value) {
        return null;
    }

    /**
     * Статический метод, восстанавливающий данные менеджера из файла при запуске программы
     */
    public static FileBackedTasksManager loadFromFile(File file) {
        return null;
    }

    /**
     * Метод, проверяющий работу сохранения и восстановления менеджера из файла (сериализация)
     */
     public static void main(String[] args) {}

    @Override
    public SimpleTask getSimpleTask(int id) {
        save();
        return super.getSimpleTask(id);
    }

    @Override
    public Epic getEpic(int id) {
        save();
        return super.getEpic(id);
    }

    @Override
    public SubTask getSubtask(int id) {
        save();
        return super.getSubtask(id);
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
    public void addNewSubtask(SubTask subtask) {
        super.addNewSubtask(subtask);
        save();
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
}
