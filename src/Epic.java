import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    List<Subtask> subtasks;
    public Epic(String taskName, String taskDescription, TaskStatus taskStatus) {
        super(taskName, taskDescription, taskStatus);
    }
    public void subTaskList(List<Subtask> subtasks) {
        this.subtasks = subtasks;
    }
}
