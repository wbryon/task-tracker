package controller;
import model.Task;
import java.util.List;

/**
 * Интерфейс для управления историей просмотров
 */
public interface HistoryManager {

    /**
     * Метод должен помечать задачи как просмотренные
     */
    void add(Task task);

    /**
     * Метод должен возвращать список просмотренных задач
     */
    List<Task> getHistory();
}
