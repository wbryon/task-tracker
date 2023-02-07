package controller;

import model.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

class HistoryManagerTest {
    String expectedSimpleTask;
    private InMemoryHistoryManager historyManager;
    private InMemoryTaskManager taskManager;
    private SimpleTask simpleTask;
    private Epic epic;
    private SubTask subTask;

    HistoryManagerTest() {
    }

//    @BeforeEach
//    void updateTaskManager() {
//        historyManager = new InMemoryHistoryManager();
//        taskManager = new InMemoryTaskManager();
//
//        simpleTask = new SimpleTask("Задача 1", "Описание задачи 1",
//                "09:00 | 09.01.2023", 60);
//        epic = new Epic("Эпик 1", "Описание эпика 1");
//        subTask = new SubTask("Подзадача 1", "Описание подзадачи 1 эпика1",
//                "22:15 | 08.01.2023", 20, 2);
//    }
//
//    @Test
//    void shouldAddTask() {
//        taskManager.createSimpleTask(simpleTask);
//        historyManager.add(simpleTask);
//
//        expectedSimpleTask = historyManager.getHistory().get(0).toString();
//
//        assertEquals(expectedSimpleTask, simpleTask.toString(), "Задачи не совпадают.");
//    }
//
//    @Test
//    void shouldNotAddTask() {
//        taskManager.createSimpleTask(simpleTask);
//
//        List<SimpleTask> expectedEmptyList = List.of();
//
//        assertEquals(expectedEmptyList, historyManager.getHistory(), "Список истории просмотра задач не пуст.");
//    }
//
//    @Test
//    void shouldNotDuplicateSimpleTaskInHistory() {
//        taskManager.createSimpleTask(simpleTask);
//        historyManager.add(simpleTask);
//        historyManager.add(simpleTask);
//
//        int shouldContainTasks = 1;
//
//        assertEquals(shouldContainTasks, historyManager.getHistory().
//                size(), "Задача повторяется в истории просмотров");
//    }
//
//    @Test
//    void shouldDeleteSimpleTask() {
//        taskManager.createSimpleTask(simpleTask);
//        taskManager.createEpic(epic);
//        taskManager.createSubTask(subTask);
//        historyManager.add(simpleTask);
//        historyManager.add(epic);
//        historyManager.add(subTask);
//        historyManager.remove(1);
//
//        int shouldContainTasks = 2;
//
//        assertEquals(shouldContainTasks, historyManager.getHistory().size(), "Задача осталась в истории");
//    }
//
//    @Test
//    void shouldDeleteEpic() {
//        taskManager.createSimpleTask(simpleTask);
//        taskManager.createEpic(epic);
//        taskManager.createSubTask(subTask);
//        historyManager.add(simpleTask);
//        historyManager.add(epic);
//        historyManager.add(subTask);
//        historyManager.remove(2);
//
//        int shouldContainTasks = 2;
//
//        assertEquals(shouldContainTasks, historyManager.getHistory().size(), "Задача осталась в истории");
//    }
//
//    @Test
//    void shouldDeleteSubTask() {
//        taskManager.createSimpleTask(simpleTask);
//        taskManager.createEpic(epic);
//        taskManager.createSubTask(subTask);
//        historyManager.add(simpleTask);
//        historyManager.add(epic);
//        historyManager.add(subTask);
//        historyManager.remove(3);
//
//        int shouldContainTasks = 2;
//
//        assertEquals(shouldContainTasks, historyManager.getHistory().size(), "Задача осталась в истории");
//    }
}
