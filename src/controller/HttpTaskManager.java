package controller;

import client.KVTaskClient;
import com.google.gson.*;
import model.*;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;

public class HttpTaskManager extends FileBackedTasksManager {
    private KVTaskClient taskClient;
    private Gson gson;

    /**
     * Конструктор HttpTaskManager должен вместо имени файла принимать URL к серверу KVServer.
     * Также HttpTaskManager создаёт KVTaskClient, из которого можно получить исходное состояние менеджера.
     * Нужно заменить вызовы сохранения состояния в файлах на вызов клиента.
     */
    public HttpTaskManager(URI uri) {
        try {
            gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                    .create();
            taskClient = new KVTaskClient(uri);
            load();
        } catch (Exception e) {
            System.out.println("При создании класса HttpTaskManager возникла ошибка");
        }
    }

    @Override
    protected void save() {
        try {
            taskClient.put("task", gson.toJson(List.of(taskRepo.values())));
            taskClient.put("subtask", gson.toJson(List.of(subtaskRepo.values())));
            taskClient.put("epic", gson.toJson(List.of(epicRepo.values())));
            taskClient.put("history", gson.toJson(getHistory()));
            taskClient.put("prioritizedTasks", gson.toJson(getPrioritizedTasks()));
        } catch (Exception e) {
            System.out.println("Ошибка при попытке сохранения задач");
        }
    }

    private void load() throws IOException, InterruptedException {
        List<Task> simpleTaskList = getTasksFromJson(taskClient.load("task"));
        List<Task> subTaskList = getTasksFromJson(taskClient.load("subtask"));
        List<Task> epicList = getTasksFromJson(taskClient.load("epic"));
        List<Task> history = getTasksFromJson(taskClient.load("history"));
        List<Task> prioritizedList = getTasksFromJson(taskClient.load("prioritizedTasks"));
        simpleTaskList.forEach(task -> taskRepo.put(task.getId(), (SimpleTask) task));
        subTaskList.forEach(subtask -> subtaskRepo.put(subtask.getId(), (SubTask) subtask));
        epicList.forEach(epic -> epicRepo.put(epic.getId(), (Epic) epic));
        history.forEach(historyManager::add);
        allTasks.addAll(prioritizedList);
    }

    private List<Task> getTasksFromJson(String json) {
        List<Task> tasks = new ArrayList<>();
        JsonElement jsonElement = JsonParser.parseString(json);
        if (!jsonElement.isJsonNull()) {
            if (jsonElement.isJsonObject() || jsonElement.isJsonNull()) {
                System.out.println("Ответ сервера не соответствует ожидаемому");
                return tasks;
            }
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            if (jsonArray != null) {
                for (JsonElement element : jsonArray) {
                    if (json.contains("subTaskIds"))
                        tasks.add(gson.fromJson(element, Epic.class));
                    else if (json.contains("epicId")) {
                        tasks.add(gson.fromJson(element, SubTask.class));
                    }
                    tasks.add(gson.fromJson(element, Task.class));
                }
            }
        }
        return tasks;
    }
}
