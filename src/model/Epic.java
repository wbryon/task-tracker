package model;
import java.util.*;

/**
 * Класс для создания объектов эпиков
 */
public class Epic extends Task {

    protected List<Integer> subtasksIds = new ArrayList<>();
    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription);
    }

    /**
     * геттер для получения списка id подзадач эпика
     */
    public List<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    /**
     * Метод для добавления id подзадачи в список id подзадач эпика
     */
    public void addSubtaskId(SubTask subtask) {
        subtasksIds.add(subtask.id);
    }

    /**
     * Переопределённый метод toString
     */
    @Override
    public String toString() {
        return "Эпик {" + "название: " + taskName +
                "; описание: " + taskDescription +
                "; id: " + id + "; статус: " + status + '}';
    }
}
