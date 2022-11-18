package controller;
import model.Task;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> taskViewHistory = new ArrayList<>();

    @Override
    public void add(Task task) {
        int historyCount = 10;

        if (task == null) {
            return;
        }
        taskViewHistory.add(task);
        if (taskViewHistory.size() > historyCount) {
            taskViewHistory.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> newHistory;
        newHistory = taskViewHistory;
        return newHistory;
    }
}
