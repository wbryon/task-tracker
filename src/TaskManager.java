public class TaskManager {
    private static int id;

    public TaskManager() {
    }

    public static int getId() {
        return id;
    }
    public Task getTaskList(Task task) {
        return task;
    }

    public void deleteAllTasks() {}
    public void getTaskById() {}
    public void createTask(Task task) {
        System.out.println("Название задачи: " + task.getTaskName());
        System.out.println("Задача создана");
        task.setId(++id);
    }
    public void updateTask(Task task) {}
    public void deleteTaskById(int id) {}
    public void storeTasks() {}
}
