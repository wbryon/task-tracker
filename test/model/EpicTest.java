package model;

import controller.Managers;
import controller.TaskManager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

class EpicTest {
    private final TaskManager manager = Managers.getDefault();

    @Test
    void shouldGetEpicStatusWithStandardWork() {
        Epic epic1 = new Epic("epic 1", "description epic 1");
        Epic epic2 = new Epic("epic 2", "description epic 2");
        manager.addNewEpic(epic1);
        manager.addNewEpic(epic2);
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1 эпика 1",
                "22:15 | 08.01.2023", 20, epic1.getId());
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2 эпика 1",
                "10:00 | 10.01.2023", 15, epic1.getId());
        SubTask subTask3 = new SubTask("Подзадача 1", "Описание подзадачи 1 эпика 2",
                "12:45 | 12.01.2023", 45, epic2.getId());


        manager.addNewSubtask(subTask1);
        manager.addNewSubtask(subTask2);
        manager.addNewSubtask(subTask3);
        subTask2.setStatus(Status.DONE);
        subTask3.setStatus(Status.IN_PROGRESS);
        Set<Status> statuses = new HashSet<>();
        for (SubTask subTask : manager.getSubtaskList()) {
            statuses.add(subTask.getStatus());
        }
        assertNotEquals(0, statuses.size(), "Количество подзадач не совпадает");
    }

    @Test
    void shouldGetEpicStatusWhenNoSubTasks() {
        Epic epic = new Epic("epic 1", "description epic 1");
        manager.addNewEpic(epic);
        assertTrue(epic.subtasksIds.isEmpty());
    }

    @Test
    void shouldGetEpicStatusWhenAllSubTasksIsNew() {
        Epic epic1 = new Epic("epic 1", "description epic 1");
        Epic epic2 = new Epic("epic 2", "description epic 2");
        manager.addNewEpic(epic1);
        manager.addNewEpic(epic2);
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1 эпика 1",
                "22:15 | 08.01.2023", 20, epic1.getId());
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2 эпика 1",
                "10:00 | 10.01.2023", 15, epic1.getId());
        SubTask subTask3 = new SubTask("Подзадача 1", "Описание подзадачи 1 эпика 2",
                "12:45 | 12.01.2023", 45, epic2.getId());
        manager.addNewSubtask(subTask1);
        manager.addNewSubtask(subTask2);
        manager.addNewSubtask(subTask3);
        assertEquals(Status.NEW, epic1.getStatus(), "Подзадачи в работе или завершены");
    }

    @Test
    void shouldGetEpicStatusWhenAllSubTasksIsInProgress() {
        Epic epic = new Epic("epic 1", "description epic 1");
        manager.addNewEpic(epic);
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1 эпика 1",
                "22:15 | 08.01.2023", 20, epic.getId());
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2 эпика 1",
                "10:00 | 10.01.2023", 15, epic.getId());
        manager.addNewSubtask(subTask1);
        manager.addNewSubtask(subTask2);
        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.IN_PROGRESS);
        manager.updateEpic(epic);
        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Подзадачи только созданы или уже завершены");
    }

    @Test
    void shouldGetEpicStatusWhenAllSubTasksIsDone() {
        Epic epic = new Epic("epic 1", "description epic 1");
        manager.addNewEpic(epic);
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1 эпика 1",
                "22:15 | 08.01.2023", 20, epic.getId());
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2 эпика 1",
                "10:00 | 10.01.2023", 15, epic.getId());
        manager.addNewSubtask(subTask1);
        manager.addNewSubtask(subTask2);
        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.DONE);
        manager.updateEpic(epic);
        assertEquals(Status.DONE, epic.getStatus(), "Подзадачи только созданы или ещё в работе");
    }

    @Test
    void shouldGetEpicStatusWhenAllSubTasksIsNewAndDone() {
        Epic epic = new Epic("epic 1", "description epic 1");
        manager.addNewEpic(epic);
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1 эпика 1",
                "22:15 | 08.01.2023", 20, epic.getId());
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2 эпика 1",
                "10:00 | 10.01.2023", 15, epic.getId());
        manager.addNewSubtask(subTask1);
        manager.addNewSubtask(subTask2);
        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.NEW);
        manager.updateEpic(epic);
        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статус эпика подсчитан неверно");
    }
}