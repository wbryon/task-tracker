package controller;

import server.KVServer;

/**
 * Утилитарный класс, отвечающий за создание менеджера задач
 */
public class Managers {

    /**
     * Метод, возвращающий объект-менеджер
     */
    public static TaskManager getDefault() {
        return new HttpTaskManager("http://localhost:" + KVServer.PORT + "/");
    }

    /**
     * Метод, возвращающий объект InMemoryHistoryManager — историю просмотров
     */
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
