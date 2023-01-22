package controller;
import model.*;
import java.util.*;

/**
 * Интерфейс, объявляющий методы для работы с задачами
 */
public interface TaskManager {

    /**
     * Метод, возвращающий последние 10 просмотренных задач
     */
    List<Task> getHistory();

    /**
     * Метод для получения задачи и истории просмотра по идентификатору
     */
    SimpleTask getSimpleTaskWithoutHistory(int id);

    /**
     * Метод для получения задачи по идентификатору
     */
    SimpleTask getSimpleTask(int id);

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
    SubTask getSubtaskWithoutHistory(int id);

    /**
     * Метод для получения подзадачи и истории просмотра по идентификатору
     */
    SubTask getSubTask(int id);

    /**
     * Метод для создания новой задачи
     */
    int addNewSimpletask(SimpleTask task);

    /**
     * Метод для создания нового эпика
     */
    int addNewEpic(Epic epic);

    /**
     * Метод для создания новой подзадачи
     */
    int addNewSubtask(SubTask subtask);

    /**
     * Метод для удаления задачи по идентификатору
     */
    void deleteSimpletask(int id);

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
    void deleteAllSimpletasks();

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
    ArrayList<SimpleTask> getSimpletaskList();

    /**
     * Метод для получения списка всех эпиков
     */
    ArrayList<Epic> getEpicList();

    /**
     * Метод для получения списка всех подзадач
     */
    ArrayList<SubTask> getSubtaskList();

    /**
     * Метод для получения списка всех подзадач эпика
     */
    List<Integer> getEpicSubtasks(int epicId);

    /**
     * Метод для обновления задачи. Новая версия задачи с верным идентификатором передаётся в виде параметра
     */
    SimpleTask updateSimpletask(SimpleTask task);

    /**
     * Метод для обновления эпика. Новая версия эпика с верным идентификатором передаётся в виде параметра
     */
    Epic updateEpic(Epic epic);

    /**
     * Метод для обновления подзадачи. Новая версия подзадачи с верным идентификатором передаётся в виде параметра
     */
    SubTask updateSubtask(SubTask subtask);

    List<Task> getPrioritizedTasks();
}
