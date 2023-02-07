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
    public void addSubtaskId(SubTask subTask) {
        subtasksIds.add(subTask.id);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasksIds, epic.subtasksIds) && Objects.equals(endTime, epic.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasksIds, endTime);
    }
}
