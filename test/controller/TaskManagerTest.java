package controller;

import model.*;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    protected Task task;
    protected Epic epic;
    protected SubTask subTask;
    String taskContent;
    String epicContent;
    String subTaskContent;

    abstract T createTaskManager();

    @BeforeEach
    public void createTasksForTest() {
        taskManager = createTaskManager();
        task = new Task("Задача 1", "Описание задачи 1",
                LocalDateTime.parse("09:00 | 09.01.2023", LocalDateAdapter.formatter), Duration.ofMinutes(60));
        epic = new Epic("Эпик 1", "Описание эпика 1");
        taskContent = "1,TASK,Задача 2,IN_PROGRESS,Описание задачи 2,2023-01-11T22:00,PT30M,2023-01-11T22:30";
        epicContent = "1,EPIC,Эпик 2,NEW,Описание эпика 2,2023-01-10T10:00,PT15M,2023-01-10T10:15";
        subTaskContent = "2,SUBTASK,Подзадача 2,NEW,Описание подзадачи 2 эпика 1,2023-01-10T10:00,PT15M,2023-01-10T10:15,1";
    }

    @Test
    public void shouldGetTask() {
        taskManager.createTask(task);
        final List<Task> tasks = taskManager.getTaskList();
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void shouldGetEpic() {
        taskManager.createEpic(epic);
        final List<Epic> epics = taskManager.getEpicList();
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");
    }

    @Test
    public void shouldGetSubTask() {
        taskManager.createEpic(epic);
        subTask = new SubTask("Подзадача 1", "Описание подзадачи 1 эпика 1",
                LocalDateTime.parse("10:15 | 08.01.2023", LocalDateAdapter.formatter), Duration.ofMinutes(20), 1);
        taskManager.createSubTask(subTask);
        final List<SubTask> subTasks = taskManager.getSubTaskList();
        assertEquals(subTask, subTasks.get(0), "Подзадачи не совпадают.");
    }

    @Test
    public void shouldAddNewTask() {
        final int taskId = taskManager.createTask(task);
        final Task savedTask = taskManager.getTaskWithoutHistory(taskId);
        assertEquals(task, savedTask, "Задачи не совпадают.");
    }

    @Test
    public void shouldAddNewTaskWithUnknownId() {
        taskManager.createTask(task);
        final Task savedTask = taskManager.getTaskWithoutHistory(5);
        assertNull(savedTask, "Задача с таким id существует!");
    }

    @Test
    public void shouldAddNewTaskWithNotNull() {
        final int taskId = taskManager.createTask(task);
        final Task savedTask = taskManager.getTaskWithoutHistory(taskId);
        assertNotNull(savedTask, "Задача не найдена.");
    }

    @Test
    public void shouldAddNewEpic() {
        final int epicId = taskManager.createEpic(epic);
        final Epic savedEpic = taskManager.getEpicWithoutHistory(epicId);

        assertEquals(epic, savedEpic, "Эпики не совпадают.");
    }

    @Test
    public void shouldAddNewEpicWithUnknownId() {
        final Epic savedEpic = taskManager.getEpicWithoutHistory(2);
        assertNull(savedEpic, "Эпик не null.");
    }

    @Test
    public void shouldAddNewEpicWithNotNull() {
        final int epicId = taskManager.createEpic(epic);
        final Task savedEpic = taskManager.getEpicWithoutHistory(epicId);

        assertNotNull(savedEpic, "Эпик не найден.");
    }

    @Test
    public void shouldAddNewSubTask() {
        taskManager.createEpic(epic);
        subTask = new SubTask("Подзадача 1", "Описание подзадачи 1 эпика 1",
                LocalDateTime.parse("10:15 | 08.01.2023", LocalDateAdapter.formatter), Duration.ofMinutes(20), 1);
        final int subTaskId = taskManager.createSubTask(subTask);
        final SubTask savedSubTask = taskManager.getSubTaskWithoutHistory(subTaskId);
        assertEquals(subTask, savedSubTask, "Подзадачи не совпадают.");
    }

    @Test
    public void shouldAddNewSubTaskWithUnknownId() {
        final SubTask savedsubTask = taskManager.getSubTaskWithoutHistory(3);
        assertNull(savedsubTask, "Подзадача не null.");
    }

    @Test
    public void shouldDeleteTask() {
        taskManager.createTask(task);
        taskManager.deleteTask(task.getId());
        assertNull(taskManager.getTaskWithoutHistory(task.getId()), "Задача не удалилась");
    }

    @Test
    public void shouldDeleteEpic() {
        taskManager.createEpic(epic);
        taskManager.deleteEpic(epic.getId());
        assertNull(taskManager.getEpicWithoutHistory(epic.getId()), "Эпик не удалился");
    }

    @Test
    public void shouldDeleteSubTask() {
        taskManager.createEpic(epic);
        subTask = new SubTask("Подзадача 1", "Описание подзадачи 1 эпика 1",
                LocalDateTime.parse("10:15 | 08.01.2023", LocalDateAdapter.formatter), Duration.ofMinutes(20), 1);
        taskManager.createSubTask(subTask);
        taskManager.deleteSubTask(subTask.getId());
        assertNull(taskManager.getSubTaskWithoutHistory(subTask.getId()), "Подзадача не удалилась");
    }

    @Test
    public void shouldNotDeleteTask() {
        taskManager.createTask(task);
        taskManager.deleteTask(2);
        assertNotNull(taskManager.getTaskWithoutHistory(task.getId()), "Задача удалилась");
    }

    @Test
    public void shouldNotDeleteEpic() {
        taskManager.createEpic(epic);
        taskManager.deleteEpic(2);
        assertNotNull(taskManager.getEpicWithoutHistory(epic.getId()), "Эпик удалился");
    }

    @Test
    public void shouldNotDeleteSubTask() {
        taskManager.createEpic(epic);
        subTask = new SubTask("Подзадача 1", "Описание подзадачи 1 эпика 1",
                LocalDateTime.parse("10:15 | 08.01.2023", LocalDateAdapter.formatter), Duration.ofMinutes(20), 1);
        taskManager.createSubTask(subTask);
        taskManager.deleteSubTask(3);
        assertNotNull(taskManager.getSubTaskWithoutHistory(subTask.getId()), "Подзадача не удалилась");
    }

    @Test
    public void shouldGetTaskList() {
        taskManager.createTask(task);
        final List<Task> tasks = taskManager.getTaskList();
        assertEquals(1, tasks.size(), "Неверное количество задач.");
    }

    @Test
    public void shouldNotGetTaskList() {
        taskManager.createTask(task);
        final List<Task> tasks = taskManager.getTaskList();
        assertNotNull(tasks, "Задачи не возвращаются.");
    }

    @Test
    public void shouldGetEpicList() {
        taskManager.createEpic(epic);
        final List<Epic> epics = taskManager.getEpicList();
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
    }

    @Test
    public void shouldNotGetEpicList() {
        taskManager.createEpic(epic);
        final List<Epic> epics = taskManager.getEpicList();
        assertNotNull(epics, "Эпики не возвращаются.");
    }

    @Test
    public void shouldGetSubTaskList() {
        taskManager.createEpic(epic);
        subTask = new SubTask("Подзадача 1", "Описание подзадачи 1 эпика 1",
                LocalDateTime.parse("10:15 | 08.01.2023", LocalDateAdapter.formatter), Duration.ofMinutes(20), 1);
        taskManager.createSubTask(subTask);
        final List<SubTask> SubTasks = taskManager.getSubTaskList();
        assertEquals(1, SubTasks.size(), "Неверное количество задач.");
    }

    @Test
    public void shouldNotGetSubTaskList() {
        taskManager.createEpic(epic);
        subTask = new SubTask("Подзадача 1", "Описание подзадачи 1 эпика 1",
                LocalDateTime.parse("10:15 | 08.01.2023", LocalDateAdapter.formatter), Duration.ofMinutes(20), 1);
        taskManager.createSubTask(subTask);
        final List<SubTask> SubTasks = taskManager.getSubTaskList();
        assertNotNull(SubTasks, "Задачи не возвращаются.");
    }

    @Test
    public void shouldDeleteAllTasks() {
        taskManager.createTask(task);
        taskManager.deleteAllTasks();
        assertEquals(0, taskManager.getTaskList().size(), "Неверное количество задач.");
    }

    @Test
    public void willTheEpicListBeCleared() {
        taskManager.createEpic(epic);
        taskManager.deleteAllEpics();
        assertEquals(0, taskManager.getSubTaskList().size(), "Список подзадач не очищен.");
        assertEquals(0, taskManager.getEpicList().size(), "Список эпиков не очищен.");
    }

    @Test
    public void shouldDeleteAllSubTasks() {
        taskManager.createEpic(epic);
        subTask = new SubTask("Подзадача 1", "Описание подзадачи 1 эпика 1",
                LocalDateTime.parse("10:15 | 08.01.2023", LocalDateAdapter.formatter), Duration.ofMinutes(20), 1);
        taskManager.createSubTask(subTask);
        taskManager.deleteAllSubTasks();
        assertEquals(0, taskManager.getSubTaskList().size(), "Список подзадач не очищен.");
    }

    @Test
    public void shouldGetEpicSubtasks() {
        taskManager.createEpic(epic);
        subTask = new SubTask("Подзадача 1", "Описание подзадачи 1 эпика 1",
                LocalDateTime.parse("10:15 | 08.01.2023", LocalDateAdapter.formatter), Duration.ofMinutes(20), 1);
        taskManager.createSubTask(subTask);

        final List<Integer> subtaskIds = epic.getSubtasksIds();

        assertEquals(1, subtaskIds.size(), "Неверное количество задач.");
    }

    @Test
    public void shouldNotGetEpicSubtasks() {
        taskManager.createEpic(epic);
        subTask = new SubTask("Подзадача 1", "Описание подзадачи 1 эпика 1",
                LocalDateTime.parse("10:15 | 08.01.2023", LocalDateAdapter.formatter), Duration.ofMinutes(20), 1);
        taskManager.createSubTask(subTask);

        final List<Integer> subtasks = epic.getSubtasksIds();

        assertNotNull(subtasks, "Задачи не возвращаются.");
    }
}