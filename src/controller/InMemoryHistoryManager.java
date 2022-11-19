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
        if (taskViewHistory.size() == historyCount) {
            taskViewHistory.remove(0);
        }
        taskViewHistory.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(taskViewHistory);
    }
}
