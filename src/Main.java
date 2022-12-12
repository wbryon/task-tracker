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

        SimpleTask task1 = new SimpleTask("Задача 1", "Задача 1");
        taskManager.addNewSimpleTask(task1);

        SimpleTask task2 = new SimpleTask("Задача 2", "Задача 2");
        taskManager.addNewSimpleTask(task2);

        SimpleTask task3 = new SimpleTask("Задача 3", "Задача 3");
        taskManager.addNewSimpleTask(task3);

        Epic epic1 = new Epic("Эпик 1", "Эпик 1");
        int epicId1 = taskManager.addNewEpic(epic1);

        SubTask subtask1 = new SubTask("Подзадача 1", "Подзадача 1 эпика 1", epicId1);
        taskManager.addNewSubtask(subtask1);

        SubTask subtask2 = new SubTask("Подзадача 2", "Подзадача 2 эпика 1", epicId1);
        taskManager.addNewSubtask(subtask2);

        SubTask subtask3 = new SubTask("Подзадача 3", "Подзадача 3 эпика 1", epicId1);
        taskManager.addNewSubtask(subtask3);

        Epic epic2 = new Epic("Эпик 2", "Эпик без подзадач");
        taskManager.addNewEpic(epic2);

        taskManager.getSimpleTask(3);
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
        taskManager.getSubtask(6);
        taskManager.getSubtask(5);
        taskManager.getSubtask(6);
        taskManager.getEpic(8);
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
        taskManager.getEpic(4);
        taskManager.getSimpleTask(2);
        System.out.println("\nУдалён эпик с тремя подзадачами - подзадачи эпика также должны удалиться\n");
        taskManager.deleteEpic(4);
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
        System.out.println("\n----------end-------------");
    }
}
