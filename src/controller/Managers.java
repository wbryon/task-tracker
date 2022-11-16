package controller;

public class Managers {

    private TaskManager manager = new InMemoryTaskManager();
    TaskManager getDefault() {
        return manager;
    }
}
