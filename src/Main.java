import controller.InMemoryTaskManager;
import model.*;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        System.out.println("---- Создание задач ----\n");

        Task task1 = new Task("Задача 1", "Описание задачи 1");
        int task1Id = taskManager.addNewTask(task1);

        Task task2 = new Task("Задача 2", "Описание задачи 2");
        int task2Id = taskManager.addNewTask(task2);

        Task task3 = new Task("Задача 3", "Описание задачи 3");
        int task3Id = taskManager.addNewTask(task3);

        Task task4 = new Task("Задача 4", "Описание задачи 4");
        int task4Id = taskManager.addNewTask(task4);

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        int epicId1 = taskManager.addNewEpic(epic1);

        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        int epicId2 = taskManager.addNewEpic(epic2);

        Task task5 = new Task("Задача 5", "Описание задачи 5");
        int task5Id = taskManager.addNewTask(task5);

        Task task6 = new Task("Задача 6", "Описание задачи 6");
        int taskId = taskManager.addNewTask(task6);

        Task task7 = new Task("Задача 7", "Описание задачи 7");
        int task7Id = taskManager.addNewTask(task7);

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", epicId1);
        taskManager.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", epicId1);
        taskManager.addNewSubtask(subtask2);

        Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3", epicId2);
        taskManager.addNewSubtask(subtask3);

        taskManager.getTask(1);
        taskManager.getHistory();
        taskManager.getTask(2);
        taskManager.getHistory();
        taskManager.getTask(3);
        taskManager.getHistory();
        taskManager.getTask(4);
        taskManager.getHistory();
        taskManager.getEpic(5);
        taskManager.getHistory();
        taskManager.getEpic(6);
        taskManager.getHistory();
        taskManager.getTask(7);
        taskManager.getHistory();
        taskManager.getTask(8);
        taskManager.getHistory();
        taskManager.getTask(9);
        taskManager.getHistory();
        taskManager.getSubtask(10);
        taskManager.getHistory();
        taskManager.getSubtask(11);
        taskManager.getHistory();
        taskManager.getSubtask(12);
        taskManager.getHistory();
        System.out.println("\n----------end-------------");
    }
}
