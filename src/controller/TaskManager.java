package controller;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    /**
     * Метод, возвращающий последние 10 просмотренных задач
     */
    List<Task> getHistory();

    /**
     * Метод для получения задачи по идентификатору
     */
    Task getTask(int id);

    /**
     * Метод для получения эпика по идентификатору
     */
    Epic getEpic(int id);

    /**
     * Метод для получения подзадачи по идентификатору
     */
    Subtask getSubtask(int id);

    /**
     * Метод для создания новой задачи
     */
    int addNewTask(Task task);

    /**
     * Метод для создания нового эпика
     */
    int addNewEpic(Epic epic);

    /**
     * Метод для создания новой подзадачи
     */
    void addNewSubtask(Subtask subtask);

    /**
     * Метод для удаления задачи по идентификатору
     */
    void deleteTask(int id);

    /**
     * Метод для удаления эпика по идентификатору
     */
    void deleteEpic(int id);

    /**
     * Метод для удаления подзадачи по идентификатору
     */
    void deleteSubtask(int id);

    /**
     * Метод для удаления всех задач
     */
    void deleteAllTasks();

    /**
     * Метод для удаления всех эпиков
     */
    void deleteAllEpics();

    /**
     * Метод для удаления всех подзадач
     */
    void deleteAllSubtasks();

    /**
     * Метод для получения списка всех задач
     */
    ArrayList<Task> getTasksList();

    /**
     * Метод для получения списка всех эпиков
     */
    ArrayList<Epic> getEpicList();

    /**
     * Метод для получения списка всех подзадач
     */
    public ArrayList<Subtask> getSubtaskList();

    /**
     * Метод для получения списка всех подзадач эпика
     */
    List<Integer> getEpicSubtasks(int epicId);

    /**
     * Метод для обновления задачи. Новая версия задачи с верным идентификатором передаётся в виде параметра
     */
    void updateTask(Task task);

    /**
     * Метод для обновления эпика. Новая версия эпика с верным идентификатором передаётся в виде параметра
     */
    void updateEpic(Epic epic);

    /**
     * Метод для обновления подзадачи. Новая версия подзадачи с верным идентификатором передаётся в виде параметра
     */
    void updateSubtask(Subtask subtask);
}
