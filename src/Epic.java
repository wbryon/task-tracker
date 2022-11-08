import java.util.*;

/**
 * Класс для создания объектов эпиков
 */
public class Epic extends Task {

    protected List<Subtask> subtaskList = new ArrayList<>();
    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription);
    }

    /**
     * Метод для добавления подзадачи в список подзадач эпика
     */
    public void addSubtask(Subtask subtask) {
        subtaskList.add(subtask);
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
