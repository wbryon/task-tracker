import controller.Managers;
import controller.TaskManager;
import model.*;

/**
 * Класс, исполняющий программу
 * @author  Хабибула Тамирбудаев
 */
public class Main {

    /**
     * точка входа в программу
     */
    public static void main(String[] args) {
        Managers manager = new Managers();
        TaskManager taskManager = manager.getDefault();

        System.out.println("---- Создание задач ----\n");

        SimpleTask task1 = new SimpleTask("Задача 1", "Описание задачи 1");
        taskManager.addNewSimpleTask(task1);
        taskManager.getHistory();

        SimpleTask task2 = new SimpleTask("Задача 2", "Описание задачи 2");
        taskManager.addNewSimpleTask(task2);

        SimpleTask task3 = new SimpleTask("Задача 3", "Описание задачи 3");
        taskManager.addNewSimpleTask(task3);

        SimpleTask task4 = new SimpleTask("Задача 4", "Описание задачи 4");
        taskManager.addNewSimpleTask(task4);

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        int epicId1 = taskManager.addNewEpic(epic1);

        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        int epicId2 = taskManager.addNewEpic(epic2);

        SimpleTask task5 = new SimpleTask("Задача 5", "Описание задачи 5");
        taskManager.addNewSimpleTask(task5);

        SimpleTask task6 = new SimpleTask("Задача 6", "Описание задачи 6");
        taskManager.addNewSimpleTask(task6);
        taskManager.getEpicList();

        SimpleTask task7 = new SimpleTask("Задача 7", "Описание задачи 7");
        taskManager.addNewSimpleTask(task7);

        SubTask subtask1 = new SubTask("Подзадача 1", "Описание подзадачи 1", epicId1);
        taskManager.addNewSubtask(subtask1);

        SubTask subtask2 = new SubTask("Подзадача 2", "Описание подзадачи 2", epicId1);
        taskManager.addNewSubtask(subtask2);

        SubTask subtask3 = new SubTask("Подзадача 3", "Описание подзадачи 3", epicId2);
        taskManager.addNewSubtask(subtask3);

        taskManager.getSimpleTask(1);
        taskManager.getSimpleTask(2);
        taskManager.getSimpleTask(3);
        taskManager.getSimpleTask(4);
        taskManager.getEpic(5);
        taskManager.getEpic(6);
        taskManager.getSimpleTask(7);
        taskManager.getSimpleTask(8);
        taskManager.getSimpleTask(9);
        taskManager.getSubtask(10);
        taskManager.getHistory();
        taskManager.getSubtask(11);
        taskManager.getHistory();
        taskManager.getSubtask(12);
        taskManager.getHistory();
        System.out.println("\n----------end-------------");
    }
}
