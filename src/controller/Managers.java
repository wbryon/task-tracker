package controller;

import server.KVServer;

import java.net.URI;

/**
 * Утилитарный класс, отвечающий за создание менеджера задач и менеджера истории задач
 */
public class Managers {

    /**
     * Метод, возвращающий объект-менеджер
     */
    public static TaskManager getDefault() {
        return new HttpTaskManager(URI.create("http://localhost:8888/"));
    }

    /**
     * Метод, возвращающий объект InMemoryHistoryManager — историю просмотров
     */
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
