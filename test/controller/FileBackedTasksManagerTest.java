package controller;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

import model.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends InMemoryTaskManagerTest {
    private FileBackedTasksManager loadedTaskManager;
        private InMemoryTaskManager taskManager;
    File file;
    private Task task1;
    private Task task2;
    private Epic epic1;
    private Epic epic2;
    private SubTask subTask;
    String expectedTask1;
    String expectedTask2;
    String expectedEpic;
    String expectedSubTask;

    @BeforeEach
    public void doBeforeEachTest() {
        file = new File("resources/task.csv");
        loadedTaskManager = new FileBackedTasksManager(file);
        taskManager = new InMemoryTaskManager();
        task1 = new Task("Задача 1", "Описание задачи 1",
                LocalDateTime.parse("09:00 | 09.01.2023", LocalDateAdapter.formatter), Duration.ofMinutes(60));
        task2 = new Task("Задача 2", "Описание задачи 2",
                LocalDateTime.parse("22:00 | 11.01.2023", LocalDateAdapter.formatter), Duration.ofMinutes(30));
        epic1 = new Epic("Эпик 1", "Описание эпика 1");
        epic2 = new Epic("Эпик 1", "Описание эпика 2");
        epic1 = new Epic("Эпик 1", "Описание эпика 1");
        subTask = new SubTask("Подзадача 1", "Описание подзадачи 1 эпика 1",
                LocalDateTime.parse("10:15 | 08.01.2023", LocalDateAdapter.formatter), Duration.ofMinutes(20), 1);
        expectedTask1 = "Задача {название: Задача 1; описание: Описание задачи 1; id: 1; статус: NEW}";
        expectedTask2 = "Задача {название: Задача 2; описание: Описание задачи 2; id: 1; статус: NEW}";
        expectedEpic = "Эпик {название: Эпик 1; описание: Описание эпика 1; id: 1; статус: NEW}";
        expectedSubTask = "2,SUBTASK,Подзадача 1,NEW,Описание подзадачи 1 эпика 1,2023-01-08T22:15,PT20M,2023-01-08T22:35,1";
    }

    @Test
    void shouldSaveTask() {
        loadedTaskManager.createTask(task1);
        loadedTaskManager.getTaskWithoutHistory(1);
        FileBackedTasksManager.loadFromFile(file);
        Task savedTask = loadedTaskManager.getTaskWithoutHistory(task1.getId());
        assertEquals(task1, savedTask, "Задача сохранена неверно");
    }

    @Test
    void shouldSaveEpic() {
        loadedTaskManager.createEpic(epic1);
        loadedTaskManager.getEpicWithoutHistory(1);
        FileBackedTasksManager.loadFromFile(file);
        Epic savedEpic = loadedTaskManager.getEpicWithoutHistory(epic1.getId());
        assertEquals(epic1, savedEpic, "Эпик сохранён неверно");
    }

    @Test
    void shouldLoadFromFileNewTask() {
        loadedTaskManager.createTask(task1);
        loadedTaskManager.getTaskWithoutHistory(1);

        FileBackedTasksManager.loadFromFile(file);
        Task newTask = loadedTaskManager.getTaskWithoutHistory(1);

        Assertions.assertEquals(task1, newTask, "Новая задача не получена из файла");
    }

    @Test
    void shouldLoadFromFileTaskList() {
        loadedTaskManager.createTask(task1);
        loadedTaskManager.getTaskWithoutHistory(1);
        taskManager.createTask(task1);
        FileBackedTasksManager.loadFromFile(file);

        assertEquals(taskManager.getTaskList(), loadedTaskManager.getTaskList(),
                "Список задач после выгрузки не совпадает");
    }

    @Test
    void shouldLoadFromFileEpicList() {
        loadedTaskManager.createEpic(epic1);
        loadedTaskManager.getEpicWithoutHistory(1);
        taskManager.createEpic(epic1);

        FileBackedTasksManager.loadFromFile(file);

        Assertions.assertEquals(taskManager.getEpicList(), loadedTaskManager.getEpicList(),
                "Список задач после выгрузки не совпадает");
    }

    @Test
    void shouldLoadFromFileSubTaskList() {
        loadedTaskManager.createEpic(epic1);
        loadedTaskManager.createSubTask(subTask);
        loadedTaskManager.getSubTaskWithoutHistory(2);
        taskManager.createEpic(epic2);
        taskManager.createSubTask(subTask);

        FileBackedTasksManager.loadFromFile(file);

        Assertions.assertEquals(taskManager.getSubTaskList(), loadedTaskManager.getSubTaskList(),
                "Список задач после выгрузки не совпадает");
    }

    @Test
    void shouldLoadFromFileListOfPrioritized() {
        loadedTaskManager.createTask(task1);
        loadedTaskManager.createTask(task2);
        loadedTaskManager.getTaskWithoutHistory(1);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        FileBackedTasksManager.loadFromFile(file);
        Assertions.assertEquals(taskManager.getPrioritizedTasks(), loadedTaskManager.getPrioritizedTasks(),
                "Список отсортированных задач после выгрузки не совпадает");
    }

    @Test
    void shouldLoadListOfHistoryFromFile() {
        loadedTaskManager.createTask(task1);
        loadedTaskManager.createTask(task2);
        loadedTaskManager.getTask(2);
        loadedTaskManager.getTask(1);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.getTask(2);
        taskManager.getTask(1);

        FileBackedTasksManager.loadFromFile(file);

        Assertions.assertEquals(taskManager.historyManager.getHistory(),
                loadedTaskManager.historyManager.getHistory(),
                "Список задач в истории после выгрузки не совпадает");
    }

    @Override
    TaskManager createTaskManager() {
        File file = new File("resources/task.csv");
        return new FileBackedTasksManager(file);
    }
}