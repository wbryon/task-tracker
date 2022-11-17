package model;

/**
 * Класс для создания объектов задач
 */
public class SimpleTask extends Task {

    public SimpleTask(String taskName, String taskDescription) {
        super(taskName, taskDescription);
    }

    /**
     * Переопределённый метод toString
     */
    @Override
    public String toString() {
        return "Задача {" + "название: " + taskName +
                "; описание: " + taskDescription +
                "; id: " + id + "; статус: " + status + '}';
    }
}
