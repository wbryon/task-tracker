package controller;

import java.io.File;

public class HttpTaskManager extends FileBackedTasksManager {
    private final String url;

    /**
     * Конструктор HttpTaskManager должен вместо имени файла принимать URL к серверу KVServer.
     * Также HttpTaskManager создаёт KVTaskClient, из которого можно получить исходное состояние менеджера.
     * Нужно заменить вызовы сохранения состояния в файлах на вызов клиента.
     */
    public HttpTaskManager(String url) {
        super(null);
        this.url = url;
    }
}
