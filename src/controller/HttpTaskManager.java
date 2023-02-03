package controller;

import java.io.File;

public class HttpTaskManager extends FileBackedTasksManager{

    /**
     * Конструктор HttpTaskManager должен вместо имени файла принимать URL к серверу KVServer.
     * Также HttpTaskManager создаёт KVTaskClient, из которого можно получить исходное состояние менеджера.
     * Нужно заменить вызовы сохранения состояния в файлах на вызов клиента.
     */
    public HttpTaskManager(File file) {
        super(file);
    }
}
