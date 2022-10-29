import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task = new Task("", "", TaskStatus.NEW);
        Epic epic = new Epic("", "", TaskStatus.NEW);
        String command;
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();
            command = scanner.nextLine();
            if (Integer.parseInt(command) == 2) {
                taskManager.createTask(task);
                System.out.print("Название задачи: ");
                task.setTaskName(scanner.nextLine());
                System.out.print("Описание задачи: ");
                task.setTaskName(scanner.nextLine());
                System.out.println("id: " + task.getId());
                System.out.println();
            } else if (Integer.parseInt(command) == 0) {
                System.out.println("Выход из программы");
                scanner.close();
                System.exit(0);
            }
        }
    }

    static void printMenu() {
        System.out.println("1 - Получить список всех задач");
        System.out.println("2 - Создать задачу");
        System.out.println("3 - Обновить список задач");
        System.out.println("4 - Вывести информацию о задаче по её идентификатору");
        System.out.println("5 - Удалить задачу по её идентификатору");
        System.out.println("6 - Получить список всех подзадач эпика");
        System.out.println("7 - Удалить все задачи");
        System.out.println("0 - Выйти из программы");
    }
}
