package controller;
import model.Task;
import java.util.List;

/**
 * Интерфейс для управления историей просмотров
 */
public interface HistoryManager {

    /**
     * Метод, помечающий задачи как просмотренные (размер списка не больше 10)
     */
    void add(Task task);

    /**
     * метод для удаления задачи из просмотра
     */
    void remove(int id);

    /**
     * Метод возвращающий список просмотренных задач
     */
    List<Task> getHistory();
}
