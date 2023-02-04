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

        SimpleTask simpleTask1 = new SimpleTask("Задача 1", "Описание задачи 1",
                "09:00 | 09.01.2023", 60);
        taskManager.createSimpleTask(simpleTask1);

        SimpleTask simpleTask2 = new SimpleTask("Задача 2", "Описание задачи 2",
                "22:00 | 11.01.2023", 30);
        taskManager.createSimpleTask(simpleTask2);

        SimpleTask simpleTask3 = new SimpleTask("Задача 3", "Описание задачи 3",
                "12:00 | 16.01.2023", 57);
        taskManager.createSimpleTask(simpleTask3);

        Epic epic1 = new Epic("Эпик 1", "Эпик 1");
        int epicId1 = taskManager.createEpic(epic1);

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1 эпика1",
                "22:15 | 08.01.2023", 20, epic1.getId());

        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2 эпика1",
                "10:00 | 10.01.2023", 15, epic1.getId());

        SubTask subTask3 = new SubTask("Подзадача 1", "Описание подзадачи 1 эпика1",
                "11:15 | 18.01.2023", 40, epic1.getId());

        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        taskManager.createSubTask(subTask3);

        taskManager.getSimpleTaskWithoutHistory(3);
        taskManager.getSimpleTaskWithoutHistory(3);
        taskManager.getEpicWithoutHistory(8);
        taskManager.getSimpleTaskWithoutHistory(1);
        taskManager.getSimpleTaskWithoutHistory(2);
        taskManager.getSimpleTaskWithoutHistory(2);
        taskManager.getEpicWithoutHistory(8);
        taskManager.getSimpleTaskWithoutHistory(1);
        taskManager.getSimpleTaskWithoutHistory(3);
        taskManager.getEpicWithoutHistory(4);
        taskManager.getSubTaskWithoutHistory(7);
        taskManager.getSubTaskWithoutHistory(6);
        taskManager.getSubTaskWithoutHistory(5);
        taskManager.getSubTaskWithoutHistory(6);
        taskManager.getEpicWithoutHistory(8);
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
        taskManager.getEpicWithoutHistory(4);
        taskManager.getSimpleTaskWithoutHistory(2);
        System.out.println("\nУдалён эпик с тремя подзадачами - подзадачи эпика также должны удалиться\n");
        taskManager.deleteEpic(4);
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
        System.out.println("\n----------end-------------");
    }
}
