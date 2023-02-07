package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Родительский класс для классов Task, Epic и SubTask
 */
public class Task {
    protected String taskName;
    protected String taskDescription;
    protected int id;
    protected Status status;
    protected LocalDateTime startTime;
    protected Duration duration;

    public Task(String taskName, String taskDescription) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
    }

    public Task(String taskName, String taskDescription, LocalDateTime startTime, Duration duration) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.startTime = startTime;
        this.duration = duration;
        setStatus(Status.NEW);
        getEndTime();
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
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

    @Override
    public String toString() {
        return id + "," +
                "TASK" + "," +
                taskName + "," +
                status + "," +
                taskDescription + "," +
                startTime + "," +
                duration + "," +
                getEndTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(taskName, task.taskName)
                && Objects.equals(taskDescription, task.taskDescription)
                && status == task.status && Objects.equals(duration, task.duration)
                && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskName, taskDescription, id, status, duration, startTime);
    }
}
