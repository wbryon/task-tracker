public class Subtask extends Task {

    protected int epicId;

    public Subtask(String subtaskName, String taskDescription) {
        super(subtaskName, taskDescription);
    }
    public int getEpicId() {
        return epicId;
    }
    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
