package model;

/**
 * Класс для создания объектов задач
 */
public class Task {
    protected String taskName;
    protected String taskDescription;
    protected int id;
    protected Status status;

    public Task(String taskName, String taskDescription) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
    }

    /**
     * Геттер названия задачи
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * Сеттер названия задачи
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * Геттер описания задачи
     */
    public String getTaskDescription() {
        return taskDescription;
    }

    /**
     * Сеттер описания задачи
     */
    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    /**
     * Геттер id
     */
    public int getId() {
        return id;
    }

    /**
     * Сеттер id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Геттер статуса
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Сеттер статуса
     */
    public void setStatus(Status status) {
        this.status = status;
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
