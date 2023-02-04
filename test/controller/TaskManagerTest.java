package controller;

import model.*;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    protected SimpleTask simpleTask;
    protected Epic epic;
    protected SubTask subTask;
    String taskContent;
    String epicContent;
    String subTaskContent;

    abstract T createTaskManager();

    @BeforeEach
    public void createTasksForTest() {
        taskManager = createTaskManager();
        simpleTask = new SimpleTask("Задача 1", "Описание задачи 1",
                "09:00 | 09.01.2023", 60);
        epic = new Epic("Эпик 1", "Описание эпика 1");
        taskContent = "1,SIMPLETASK,Задача 2,IN_PROGRESS,Описание задачи 2,2023-01-11T22:00,PT30M,2023-01-11T22:30";
        epicContent = "1,EPIC,Эпик 2,NEW,Описание эпика 2,2023-01-10T10:00,PT15M,2023-01-10T10:15";
        subTaskContent = "2,SUBTASK,Подзадача 2,NEW,Описание подзадачи 2 эпика 1,2023-01-10T10:00,PT15M,2023-01-10T10:15,1";
    }

    @Test
    public void shouldGetSimpleTask() {
        taskManager.createSimpleTask(simpleTask);
        final List<SimpleTask> tasks = taskManager.getSimpleTaskList();
        assertEquals(simpleTask, tasks.get(0), "Задачи не совпадают.");
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
        subTask = new SubTask("Подзадача 2", "Описание подзадачи 2 эпика1",
                "10:00 | 10.01.2023", 15, epic.getId());
        taskManager.createSubTask(subTask);
        final List<SubTask> subTasks = taskManager.getSubTaskList();
        assertEquals(subTask, subTasks.get(0), "Подзадачи не совпадают.");
    }

    @Test
    public void shouldAddNewSimpleTask() {
        final int taskId = taskManager.createSimpleTask(simpleTask);
        final Task savedTask = taskManager.getSimpleTaskWithoutHistory(taskId);
        assertEquals(simpleTask, savedTask, "Задачи не совпадают.");
    }

    @Test
    public void shouldAddNewSimpleTaskWithUnknownId() {
        taskManager.createSimpleTask(simpleTask);
        final Task savedTask = taskManager.getSimpleTaskWithoutHistory(5);
        assertNull(savedTask, "Задача с таким id существует!");
    }

    @Test
    public void shouldAddNewSimpleTaskWithNotNull() {
        final int taskId = taskManager.createSimpleTask(simpleTask);
        final Task savedTask = taskManager.getSimpleTaskWithoutHistory(taskId);
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
                "10:00 | 10.01.2023", 15, epic.getId());
        final int subTaskId = taskManager.createSubTask(subTask);
        final SubTask savedSubTask = taskManager.getSubTaskWithoutHistory(subTaskId);
        assertEquals(subTask, savedSubTask, "Подзадачи не совпадают.");
    }

    @Test
    public void shouldAddNewSubTaskWithNotNull() {
        taskManager.createEpic(epic);
        subTask = new SubTask("Подзадача 1", "Описание подзадачи 1 эпика 1",
                "10:00 | 10.01.2023", 15, epic.getId());
        final int subTaskId = taskManager.createSubTask(subTask);
        final SubTask savedsubTask = taskManager.getSubTaskWithoutHistory(subTaskId);
        assertNotNull(savedsubTask, "Задача не найдена.");
    }

    @Test
    public void shouldAddNewSubTaskWithUnknownId() {
        final SubTask savedsubTask = taskManager.getSubTaskWithoutHistory(3);
        assertNull(savedsubTask, "Подзадача не null.");
    }

    @Test
    public void shouldDeleteSimpleTask() {
        taskManager.createSimpleTask(simpleTask);
        taskManager.deleteSimpleTask(simpleTask.getId());
        assertNull(taskManager.getSimpleTaskWithoutHistory(simpleTask.getId()), "Задача не удалилась");
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
                "10:00 | 10.01.2023", 15, epic.getId());
        taskManager.createSubTask(subTask);
        taskManager.deleteSubTask(subTask.getId());
        assertNull(taskManager.getSubTaskWithoutHistory(subTask.getId()), "Подзадача не удалилась");
    }

    @Test
    public void shouldNotDeleteSimpleTask() {
        taskManager.createSimpleTask(simpleTask);
        taskManager.deleteSimpleTask(2);
        assertNotNull(taskManager.getSimpleTaskWithoutHistory(simpleTask.getId()), "Задача удалилась");
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
        subTask = new SubTask("Подзадача 21", "Описание подзадачи 1 эпика1",
                "10:00 | 10.01.2023", 15, epic.getId());
        taskManager.createSubTask(subTask);
        taskManager.deleteSubTask(3);
        assertNotNull(taskManager.getSubTaskWithoutHistory(subTask.getId()), "Подзадача не удалилась");
    }

    @Test
    public void shouldGetSimpleTaskList() {
        taskManager.createSimpleTask(simpleTask);
        final List<SimpleTask> tasks = taskManager.getSimpleTaskList();
        assertEquals(1, tasks.size(), "Неверное количество задач.");
    }

    @Test
    public void shouldNotGetSimpleTaskList() {
        taskManager.createSimpleTask(simpleTask);
        final List<SimpleTask> tasks = taskManager.getSimpleTaskList();
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
                "10:00 | 10.01.2023", 15, epic.getId());
        taskManager.createSubTask(subTask);
        final List<SubTask> SubTasks = taskManager.getSubTaskList();
        assertEquals(1, SubTasks.size(), "Неверное количество задач.");
    }

    @Test
    public void shouldNotGetSubTaskList() {
        taskManager.createEpic(epic);
        subTask = new SubTask("Подзадача 1", "Описание подзадачи 1 эпика 1",
                "10:00 | 10.01.2023", 15, epic.getId());
        taskManager.createSubTask(subTask);
        final List<SubTask> SubTasks = taskManager.getSubTaskList();
        assertNotNull(SubTasks, "Задачи не возвращаются.");
    }

    @Test
    public void shouldDeleteAllSimpleTasks() {
        taskManager.createSimpleTask(simpleTask);
        taskManager.deleteAllSimpleTasks();
        assertEquals(0, taskManager.getSimpleTaskList().size(), "Неверное количество задач.");
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
                "10:00 | 10.01.2023", 15, epic.getId());
        taskManager.createSubTask(subTask);
        taskManager.deleteAllSubTasks();
        assertEquals(0, taskManager.getSubTaskList().size(), "Список подзадач не очищен.");
    }

    @Test
    public void shouldGetEpicSubtasks() {
        taskManager.createEpic(epic);
        subTask = new SubTask("Подзадача 1", "Описание подзадачи 1 эпика 1",
                "10:00 | 10.01.2023", 15, epic.getId());
        taskManager.createSubTask(subTask);

        final List<Integer> subtaskIds = epic.getSubtasksIds();

        assertEquals(1, subtaskIds.size(), "Неверное количество задач.");
    }

    @Test
    public void shouldNotGetEpicSubtasks() {
        taskManager.createEpic(epic);
        subTask = new SubTask("Подзадача 1", "Описание подзадачи 1 эпика 1",
                "10:00 | 10.01.2023", 15, epic.getId());
        taskManager.createSubTask(subTask);

        final List<Integer> subtasks = epic.getSubtasksIds();

        assertNotNull(subtasks, "Задачи не возвращаются.");
    }

    @Test
    void shouldUpdateSimpletask() {
        taskManager.createSimpleTask(simpleTask);
        SimpleTask newSimpletask = new SimpleTask("Задача 2", "Описание задачи 2",
                "22:00 | 11.01.2023", 30);
        newSimpletask.setId(simpleTask.getId());
        newSimpletask.setStatus(Status.IN_PROGRESS);
        simpleTask = taskManager.updateSimpleTask(newSimpletask);
        assertEquals(taskContent, simpleTask.toString(), "Задача не обновлена");
    }

    @Test
    void shouldUpdateEpic() {
        taskManager.createEpic(epic);
        Epic newEpic = new Epic("Эпик 2", "Описание эпика 2");
        taskManager.createSubTask(new SubTask("Подзадача 2", "Описание подзадачи 2 эпика1",
                "10:00 | 10.01.2023", 15, epic.getId()));
        newEpic.setId(epic.getId());
        newEpic.setStatus(Status.IN_PROGRESS);
        taskManager.updateEpic(newEpic);
        assertEquals(epicContent, epic.toString(), "Эпик не обновился");
    }

    @Test
    void shouldUpdateSubtask() {
        taskManager.createEpic(epic);
        subTask = new SubTask("Подзадача 1", "Описание подзадачи 1 эпика 1",
                "10:00 | 10.01.2023", 15, epic.getId());
        taskManager.createSubTask(subTask);
        SubTask newsubTask = new SubTask("Подзадача 2", "Описание подзадачи 2 эпика 1",
                "10:00 | 10.01.2023", 15, epic.getId());
        newsubTask.setId(subTask.getId());
        taskManager.updateSubTask(newsubTask);

        assertEquals(subTaskContent, subTask.toString());
    }
}