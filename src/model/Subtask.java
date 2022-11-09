package model;

/**
 * Класс для создания объектов подзадач
 */
public class Subtask extends Task {

    protected int epicId;

    public Subtask(String subtaskName, String taskDescription, int epicId) {
        super(subtaskName, taskDescription);
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
        return "Подзадача {" + "название: " + taskName +
                "; описание: " + taskDescription +
                "; id: " + id + "; статус: " + status + '}';
    }
}
