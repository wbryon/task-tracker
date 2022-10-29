import java.util.*;

public class Task {

    private String taskName;
    private int id;
    private String taskDescription;
    private TaskStatus taskStatus;
    public Task(String taskName, String taskDescription, TaskStatus taskStatus) {
        setId(TaskManager.getId());
        setTaskName(taskName);
        setTaskDescription(taskDescription);
        setTaskStatus(taskStatus);
    }

    public int getId() {
        return id;
    }
    public String getTaskName() {
        return taskName;
    }
    public String getTaskDescription() {
        return taskDescription;
    }
    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }
}
