public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        System.out.println("---- Создание задач ----\n");

        Task task1 = new Task("Задача 1", "Описание задачи 1");
        int task1Id = taskManager.addNewTask(task1);

        Task task2 = new Task("Задача 2", "Описание задачи 2");
        int task2Id = taskManager.addNewTask(task2);

        for (int id : taskManager.taskRepo.keySet()) {
            System.out.println(taskManager.taskRepo.get(id));
        }
        System.out.println("\n---- Создание эпиков ----\n");

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");

        final int epicId1 = taskManager.addNewEpic(epic1);
        final int epicId2 = taskManager.addNewEpic(epic2);

        for (int epicId : taskManager.epicRepo.keySet()) {
            System.out.println(taskManager.epicRepo.get(epicId));
        }

        System.out.println("\n---- Создание подзадач ----\n");

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", epicId1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", epicId1);
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3", epicId2);

        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        taskManager.addNewSubtask(subtask3);

        for (Integer epicId : taskManager.epicRepo.keySet()) {
            System.out.println("epicId: " + epicId + "; status: " + taskManager.epicRepo.get(epicId).status);
            for (Subtask subtask : taskManager.epicRepo.get(epicId).subtaskList) {
                System.out.println(subtask);
            }
        }

        System.out.println("\n---- Проверка изменения статусов ----\n");

        task2.setStatus(Status.IN_PROGRESS);

        for (int id : taskManager.taskRepo.keySet()) {
            System.out.println(taskManager.taskRepo.get(id));
        }
        subtask1.setStatus(Status.DONE);
        TaskManager.updateEpicStatus(epic1);
        for (Integer epicId : taskManager.epicRepo.keySet()) {
            System.out.println("epicId: " + epicId + "; status: " + taskManager.epicRepo.get(epicId).getStatus());
            for (Subtask subtask : taskManager.epicRepo.get(epicId).subtaskList) {
                System.out.println(subtask);
            }
        }

        System.out.println("\n---- Удаление задачи и эпика ----\n");

        taskManager.deleteTask(task1Id);
        for (int id : taskManager.taskRepo.keySet()) {
            System.out.println(taskManager.taskRepo.get(id));
        }
        taskManager.deleteEpic(epicId2);
        for (Integer epicId : taskManager.epicRepo.keySet()) {
            System.out.println("epicId: " + epicId + "; status: " + taskManager.epicRepo.get(epicId).getStatus());
            for (Subtask subtask : taskManager.epicRepo.get(epicId).subtaskList) {
                System.out.println(subtask);
            }
        }
    }
}
