package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Класс для создания объектов подзадач
 */
public class SubTask extends Task {

    protected int epicId;

    public SubTask(String taskName, String taskDescription, int epicId) {
        super(taskName, taskDescription);
        this.epicId = epicId;
    }

    public SubTask(String taskName, String taskDescription, LocalDateTime start, Duration duration, int epicId) {
        super(taskName, taskDescription, start, duration);
        this.epicId = epicId;
    }

    /**
     * Геттер id эпика
     */
    public int getEpicId() {
        return epicId;
    }

    /**
     * Сеттер id эпика
     */
    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    /**
     * Переопределённый метод toString
     */
    @Override
    public String toString() {
        return id + "," +
                "SUBTASK" + "," +
                taskName + "," +
                status + "," +
                taskDescription + "," +
                startTime + "," +
                duration + "," +
                getEndTime() + "," +
                epicId;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return epicId == subTask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }
}
