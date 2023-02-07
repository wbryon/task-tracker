package model;

import java.time.LocalDateTime;

/**
 * Класс для создания объектов подзадач
 */
public class SubTask extends Task {

    protected int epicId;

    public SubTask(String taskName, String taskDescription, int epicId) {
        super(taskName, taskDescription);
        this.epicId = epicId;
    }

    public SubTask(String taskName, String taskDescription, LocalDateTime start, int duration, int epicId) {
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
//    @Override
//    public String toString() {
//        return id + "," +
//                "SUBTASK" + "," +
//                taskName + "," +
//                status + "," +
//                taskDescription + "," +
//                startTime + "," +
//                duration + "," +
//                getEndTime() + "," +
//                epicId;
//    }
}
