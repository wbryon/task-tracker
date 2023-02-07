package controller;
import model.*;
import java.util.*;

/**
 * Интерфейс, объявляющий методы для работы с задачами
 */
public interface TaskManager {

    /**
     * Метод, возвращающий последний id
     */
    int getGeneratorId();
    /**
     * Метод, возвращающий последние 10 просмотренных задач
     */
    List<Task> getHistory();

    /**
     * Метод для получения задачи и истории просмотра по идентификатору
     */
    Task getTaskWithoutHistory(int id);

    /**
     * Метод для получения задачи по идентификатору
     */
    Task getTask(int id);

    /**
     * Метод для получения эпика по идентификатору
     */
    Epic getEpicWithoutHistory(int id);

    /**
     * Метод для получения эпика по идентификатору
     */
    Epic getEpic(int id);

    /**
     * Метод для получения подзадачи по идентификатору
     */
    SubTask getSubTaskWithoutHistory(int id);

    /**
     * Метод для получения подзадачи и истории просмотра по идентификатору
     */
    SubTask getSubTask(int id);

    /**
     * Метод для создания новой задачи
     */
    int createTask(Task task);

    /**
     * Метод для создания нового эпика
     */
    int createEpic(Epic epic);

    /**
     * Метод для создания новой подзадачи
     */
    int createSubTask(SubTask subtask);

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
    void deleteSubTask(int id);

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
    void deleteAllSubTasks();

    /**
     * Метод для получения списка всех задач
     */
    ArrayList<Task> getTaskList();

    /**
     * Метод для получения списка всех эпиков
     */
    ArrayList<Epic> getEpicList();

    /**
     * Метод для получения списка всех подзадач
     */
    ArrayList<SubTask> getSubTaskList();

    /**
     * Метод для получения списка всех подзадач эпика
     */
    List<Integer> getEpicSubTasks(int epicId);

    /**
     * Метод для обновления задачи. Новая версия задачи с верным идентификатором передаётся в виде параметра
     */
    Task updateTask(Task task);

    /**
     * Метод для обновления эпика. Новая версия эпика с верным идентификатором передаётся в виде параметра
     */
    Epic updateEpic(Epic epic);

    /**
     * Метод для обновления подзадачи. Новая версия подзадачи с верным идентификатором передаётся в виде параметра
     */
    SubTask updateSubTask(SubTask subtask);

    List<Task> getPrioritizedTasks();
}
