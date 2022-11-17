package controller;
import model.Task;
import java.util.*;

/**
 * Класс, реализующий методы интерфейса TaskManager для работы с историей задач
 */
public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> taskViewHistory = new ArrayList<>();

    /**
     * Реализация метода, помечающего задачи как просмотренные (размер списка не больше 10)
     */
    @Override
    public void add(Task task) {
        int historyCount = 10;

        if (task == null) {
            return;
        }
        if (taskViewHistory.size() <= historyCount) {
            taskViewHistory.add(task);
        } else {
            taskViewHistory.add(task);
            taskViewHistory.remove(0);
        }
    }

    /**
     * Реализация метода, возвращающего список просмотренных задач
     */
    @Override
    public List<Task> getHistory() {
        return taskViewHistory;
    }
}
