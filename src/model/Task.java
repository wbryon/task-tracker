package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Родительский класс для классов SimpleTask, Epic и SubTask
 */
public abstract class Task {
    protected String taskName;
    protected String taskDescription;
    protected int id;
    protected Status status;
    protected LocalDateTime startTime;
    protected Duration duration;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm | dd.MM.yyyy");

    public Task(String taskName, String taskDescription) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
    }

    public Task(String taskName, String taskDescription, String start, int duration) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.startTime = getStartTimeOfTaskFromString(start);
        this.duration = getDurationOfTaskFromString(duration);
        setStatus(Status.NEW);
        getEndTime();
    }

    public LocalDateTime getStartTimeOfTaskFromString(String start) {
        return LocalDateTime.parse(start, formatter);
    }

    public Duration getDurationOfTaskFromString(int minutes) {
        Duration duration;
        if (minutes >= 60)
            duration = Duration.ofHours(minutes / 60).plusMinutes(minutes % 60);
        else
            duration = Duration.ofMinutes(minutes);
        return duration;
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
}
