package model;

/**
 * Класс для создания объектов задач
 */
public class SimpleTask extends Task {

    public SimpleTask(String taskName, String taskDescription) {
        super(taskName, taskDescription);
    }

    public SimpleTask(String taskName, String taskDescription, String start, int duration) {
        super(taskName, taskDescription, start, duration);
    }

    /**
     * Переопределённый метод toString
     */
    @Override
    public String toString() {
        return id + "," +
                "SIMPLETASK" + "," +
                taskName + "," +
                status + "," +
                taskDescription + "," +
                startTime + "," +
                duration + "," +
                getEndTime();
    }
}
