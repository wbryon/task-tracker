package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Класс для создания объектов эпиков
 */
public class Epic extends Task {
    protected List<Integer> subtasksIds = new ArrayList<>();
    private LocalDateTime endTime;
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
        return id + "," +
                "EPIC" + "," +
                taskName + "," +
                status + "," +
                taskDescription + "," +
                startTime + "," +
                duration + "," +
                getEndTime();
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public Duration getDuration() {
        return super.getDuration();
    }

    @Override
    public void setDuration(Duration duration) {
        super.setDuration(duration);
    }
}
