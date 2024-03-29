package controller;

import http.KVServer;

import java.io.IOException;
import java.net.URI;

/**
 * Утилитарный класс, отвечающий за создание менеджера задач и менеджера истории задач
 */
public class Managers {

    /**
     * Метод, возвращающий объект-менеджер
     */
    public static TaskManager getDefault() {
        return new HttpTaskManager(URI.create("http://localhost:8078/register"));
    }

    /**
     * Метод, возвращающий объект InMemoryHistoryManager — историю просмотров
     */
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    /**
     * Метод, возвращающий экземпляр хранилища клиентов
     */
    public static KVServer getDefaultKVServer() throws IOException {
        return new KVServer();
    }
}
