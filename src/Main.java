import controller.InMemoryHistoryManager;
import controller.Managers;
import controller.TaskManager;
import model.*;

import java.util.ArrayList;
import java.util.List;

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

        SimpleTask task2 = new SimpleTask("Задача 2", "Описание задачи 2");
        taskManager.addNewSimpleTask(task2);

        SimpleTask task3 = new SimpleTask("Задача 3", "Описание задачи 3");
        taskManager.addNewSimpleTask(task3);

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        int epicId1 = taskManager.addNewEpic(epic1);

        SubTask subtask1 = new SubTask("Подзадача 1", "Описание подзадачи 1", epicId1);
        taskManager.addNewSubtask(subtask1);

        SubTask subtask2 = new SubTask("Подзадача 2", "Описание подзадачи 2", epicId1);
        taskManager.addNewSubtask(subtask2);

        SubTask subtask3 = new SubTask("Подзадача 3", "Описание подзадачи 3", epicId1);
        taskManager.addNewSubtask(subtask2);

        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        int epicId2 = taskManager.addNewEpic(epic2);

        taskManager.getSimpleTask(3);
        taskManager.getEpic(8);
        taskManager.getSimpleTask(1);
        taskManager.getSimpleTask(2);
        taskManager.getSimpleTask(2);
        taskManager.getEpic(8);
        taskManager.getSimpleTask(1);
        taskManager.getSimpleTask(3);
        taskManager.getEpic(4);
        taskManager.getSubtask(7);
        taskManager.getEpic(4);
        taskManager.getHistory();
        System.out.println("\n----------end-------------");
    }
}
