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
        SubTask subTask1 = new SubTask("subTask 1", "description subTask 1", epic1.getId());
        SubTask subTask2 = new SubTask("subTask 2", "description subTask 2", epic1.getId());
        SubTask subTask3 = new SubTask("subTask 3", "description subTask 3", epic2.getId());
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
    void shouldGetEpicStatusWhenNoSubtasks() {
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
        SubTask subTask1 = new SubTask("subTask 1", "description subTask 1", epic1.getId());
        SubTask subTask2 = new SubTask("subTask 2", "description subTask 2", epic2.getId());
        manager.addNewSubtask(subTask1);
        manager.addNewSubtask(subTask2);
        assertEquals(Status.NEW, epic1.getStatus(), "Подзадачи в работе или завершены");
    }

    @Test
    void shouldGetEpicStatusWhenAllSubTasksIsInProgress() {
        Epic epic = new Epic("epic 1", "description epic 1");
        manager.addNewEpic(epic);
        SubTask subTask1 = new SubTask("subTask 1", "description subTask 1", epic.getId());
        SubTask subTask2 = new SubTask("subTask 2", "description subTask 2", epic.getId());
        epic.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Подзадачи только созданы или уже завершены");
    }

    @Test
    void shouldGetEpicStatusWhenAllSubTasksIsDone() {
        Epic epic1 = new Epic("epic 1", "description epic 1");
        manager.addNewEpic(epic1);
        SubTask subTask1 = new SubTask("subTask 1", "description subTask 1", epic1.getId());
        SubTask subTask2 = new SubTask("subTask 2", "description subTask 2", epic1.getId());
        epic1.setStatus(Status.DONE);
        assertEquals(Status.DONE, epic1.getStatus(), "Подзадачи только созданы или ещё в работе");
    }

    @Test
    void shouldGetEpicStatusWhenAllSubTasksIsNewAndDone() {
        Epic epic1 = new Epic("epic 1", "description epic 1");
        Epic epic2 = new Epic("epic 2", "description epic 2");
        manager.addNewEpic(epic1);
        manager.addNewEpic(epic2);
        SubTask subTask1 = new SubTask("subTask 1", "description subTask 1", epic1.getId());
        SubTask subTask2 = new SubTask("subTask 2", "description subTask 2", epic1.getId());
        SubTask subTask3 = new SubTask("subTask 3", "description subTask 3", epic2.getId());
        manager.addNewSubtask(subTask1);
        manager.addNewSubtask(subTask2);
        manager.addNewSubtask(subTask3);
        subTask2.setStatus(Status.DONE);
        Set<Status> statuses = new HashSet<>();
        for (SubTask subTask : manager.getSubtaskList()) {
            statuses.add(subTask.getStatus());
        }
        assertFalse(statuses.contains(Status.IN_PROGRESS), "Статус подзадач не совпадает");
    }
}