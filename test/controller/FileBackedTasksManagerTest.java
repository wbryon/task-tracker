package controller;

import java.io.File;

import model.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends InMemoryTaskManagerTest {
    private FileBackedTasksManager loadedTaskManager;
        private InMemoryTaskManager taskManager;
    File file;
    private SimpleTask simpleTask1;
    private SimpleTask simpleTask2;
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
        simpleTask1 = new SimpleTask("Задача 1", "Описание задачи 1",
                "09:00 | 09.01.2023", 60);

        simpleTask2 = new SimpleTask("Задача 2", "Описание задачи 2",
                "22:00 | 11.01.2023", 30);
        epic1 = new Epic("Эпик 1", "Описание эпика 1");
        epic2 = new Epic("Эпик 1", "Описание эпика 2");
        subTask = new SubTask("Подзадача 1", "Описание подзадачи 1 эпика 1",
                "22:15 | 08.01.2023", 20, 1);
        expectedTask1 = "Задача {название: Задача 1; описание: Описание задачи 1; id: 1; статус: NEW}";
        expectedTask2 = "Задача {название: Задача 2; описание: Описание задачи 2; id: 1; статус: NEW}";
        expectedEpic = "Эпик {название: Эпик 1; описание: Описание эпика 1; id: 1; статус: NEW}";
        expectedSubTask = "2,SUBTASK,Подзадача 1,NEW,Описание подзадачи 1 эпика 1,2023-01-08T22:15,PT20M,2023-01-08T22:35,1";
    }

    @Test
    void shouldSaveSimpleTask() {
        loadedTaskManager.addNewSimpletask(simpleTask1);
        loadedTaskManager.getSimpleTaskWithoutHistory(1);
        FileBackedTasksManager.loadFromFile(file);
        Task savedTask = loadedTaskManager.getSimpleTaskWithoutHistory(simpleTask1.getId());
        assertEquals(simpleTask1, savedTask, "Задача сохранена неверно");
    }

    @Test
    void shouldSaveEpic() {
        loadedTaskManager.addNewEpic(epic1);
        loadedTaskManager.getEpicWithoutHistory(1);
        FileBackedTasksManager.loadFromFile(file);
        Epic savedEpic = loadedTaskManager.getEpicWithoutHistory(epic1.getId());
        assertEquals(epic1, savedEpic, "Эпик сохранён неверно");
    }

    @Test
    void shouldSaveSubTask() {
        loadedTaskManager.addNewEpic(epic1);
        loadedTaskManager.addNewSubtask(subTask);
        loadedTaskManager.getSubtaskWithoutHistory(2);
        FileBackedTasksManager.loadFromFile(file);
        SubTask savedSubTask = loadedTaskManager.getSubtaskWithoutHistory(subTask.getId());
        assertEquals(expectedSubTask, savedSubTask.toString(), "Подзадача сохранена неверно");
    }

    @Test
    void shouldLoadFromFileNewSimpleTask() {
        loadedTaskManager.addNewSimpletask(simpleTask1);
        loadedTaskManager.getSimpleTaskWithoutHistory(1);

        FileBackedTasksManager.loadFromFile(file);
        Task newTask = loadedTaskManager.getSimpleTaskWithoutHistory(1);

        Assertions.assertEquals(simpleTask1, newTask, "Новая задача не получена из файла");
    }

    @Test
    void shouldLoadFromFileSimpleTaskList() {
        loadedTaskManager.addNewSimpletask(simpleTask1);
        loadedTaskManager.getSimpleTaskWithoutHistory(1);
        taskManager.addNewSimpletask(simpleTask1);
        FileBackedTasksManager.loadFromFile(file);

        assertEquals(taskManager.getSimpletaskList(), loadedTaskManager.getSimpletaskList(),
                "Список задач после выгрузки не совпадает");
    }

    @Test
    void shouldLoadFromFileEpicList() {
        loadedTaskManager.addNewEpic(epic1);
        loadedTaskManager.getEpicWithoutHistory(1);
        taskManager.addNewEpic(epic1);

        FileBackedTasksManager.loadFromFile(file);

        Assertions.assertEquals(taskManager.getEpicList(), loadedTaskManager.getEpicList(),
                "Список задач после выгрузки не совпадает");
    }

    @Test
    void shouldLoadFromFileSubTaskList() {
        loadedTaskManager.addNewEpic(epic1);
        loadedTaskManager.addNewSubtask(subTask);
        loadedTaskManager.getSubtaskWithoutHistory(2);
        taskManager.addNewEpic(epic2);
        taskManager.addNewSubtask(subTask);

        FileBackedTasksManager.loadFromFile(file);

        Assertions.assertEquals(taskManager.getSubtaskList(), loadedTaskManager.getSubtaskList(),
                "Список задач после выгрузки не совпадает");
    }

    @Test
    void shouldLoadFromFileListOfPrioritized() {
        loadedTaskManager.addNewSimpletask(simpleTask1);
        loadedTaskManager.addNewSimpletask(simpleTask2);
        loadedTaskManager.getSimpleTaskWithoutHistory(1);
        taskManager.addNewSimpletask(simpleTask1);
        taskManager.addNewSimpletask(simpleTask2);
        FileBackedTasksManager.loadFromFile(file);
        Assertions.assertEquals(taskManager.getPrioritizedTasks(), loadedTaskManager.getPrioritizedTasks(),
                "Список отсортированных задач после выгрузки не совпадает");
    }

    @Test
    void shouldLoadListOfHistoryFromFile() {
        loadedTaskManager.addNewSimpletask(simpleTask1);
        loadedTaskManager.addNewSimpletask(simpleTask2);
        loadedTaskManager.getSimpleTask(2);
        loadedTaskManager.getSimpleTask(1);
        taskManager.addNewSimpletask(simpleTask1);
        taskManager.addNewSimpletask(simpleTask2);
        taskManager.getSimpleTask(2);
        taskManager.getSimpleTask(1);

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