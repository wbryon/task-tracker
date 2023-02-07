package controller;

import model.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

class HistoryManagerTest {
    String expectedTask;
    private InMemoryHistoryManager historyManager;
    private InMemoryTaskManager taskManager;
    private Task task;
    private Epic epic;
    private SubTask subTask;

    HistoryManagerTest() {
    }

    @BeforeEach
    void updateTaskManager() {
        historyManager = new InMemoryHistoryManager();
        taskManager = new InMemoryTaskManager();

        task = new Task("Задача 1", "Описание задачи 1",
                LocalDateTime.parse("09:00 | 09.01.2023", LocalDateAdapter.formatter), Duration.ofMinutes(60));
        epic = new Epic("Эпик 1", "Описание эпика 1");
        subTask = new SubTask("Подзадача 1", "Описание подзадачи 1 эпика 1",
                LocalDateTime.parse("10:15 | 08.01.2023", LocalDateAdapter.formatter), Duration.ofMinutes(20), 1);
    }

    @Test
    void shouldAddTask() {
        taskManager.createTask(task);
        historyManager.add(task);

        expectedTask = historyManager.getHistory().get(0).toString();

        assertEquals(expectedTask, task.toString(), "Задачи не совпадают.");
    }

    @Test
    void shouldNotAddTask() {
        taskManager.createTask(task);

        List<Task> expectedEmptyList = List.of();

        assertEquals(expectedEmptyList, historyManager.getHistory(), "Список истории просмотра задач не пуст.");
    }

    @Test
    void shouldNotDuplicateTaskInHistory() {
        taskManager.createTask(task);
        historyManager.add(task);
        historyManager.add(task);

        int shouldContainTasks = 1;

        assertEquals(shouldContainTasks, historyManager.getHistory().
                size(), "Задача повторяется в истории просмотров");
    }

    @Test
    void shouldDeleteTask() {
        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask);
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subTask);
        historyManager.remove(1);

        int shouldContainTasks = 2;

        assertEquals(shouldContainTasks, historyManager.getHistory().size(), "Задача осталась в истории");
    }

    @Test
    void shouldDeleteEpic() {
        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask);
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subTask);
        historyManager.remove(2);

        int shouldContainTasks = 2;

        assertEquals(shouldContainTasks, historyManager.getHistory().size(), "Задача осталась в истории");
    }

    @Test
    void shouldDeleteSubTask() {
        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask);
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subTask);
        historyManager.remove(2);

        int shouldContainTasks = 2;

        assertEquals(shouldContainTasks, historyManager.getHistory().size(), "Задача осталась в истории");
    }
}
