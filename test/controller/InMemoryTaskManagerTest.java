package controller;

import org.junit.jupiter.api.Test;

class InMemoryTaskManagerTest extends TaskManagerTest {

    @Test
    void shouldUpdateEpicStatus() {
    }

    @Override
    TaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }
}