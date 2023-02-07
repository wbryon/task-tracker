package controller;

import com.google.gson.*;
import model.*;
import org.junit.jupiter.api.*;
import server.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskManagerTest extends FileBackedTasksManagerTest {

    private KVServer kvServer;
    HttpTaskManager taskManager;
    private HttpTaskServer server;
    private static HttpClient client;
    private Gson gson;

    SimpleTask simpleTask1;
    SimpleTask simpleTask2;
    SubTask subTask1;
    SubTask subTask2;
    Epic epic1;
    Epic epic2;

    @BeforeEach
    public void beforeEach() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        server = new HttpTaskServer();
        taskManager = (HttpTaskManager) server.getTaskManager();
        gson = new Gson();
        client = HttpClient.newHttpClient();
    }

    @AfterEach
    public void afterEach() {
        server.stop();
        kvServer.stop();
    }

    @Test
    public void shouldGetSimpleTaskByEndpointTest() throws IOException, InterruptedException {
        simpleTask1 = new SimpleTask("Задача 1", "Описание задачи 1",
                LocalDateTime.parse("09:00 | 09.01.2023", LocalDateAdapter.formatter), 60);
        simpleTask2 = new SimpleTask("Задача 2", "Описание задачи 2",
                LocalDateTime.parse("22:00 | 11.01.2023", LocalDateAdapter.formatter), 30);

        taskManager.createSimpleTask(simpleTask1);
        taskManager.createSimpleTask(simpleTask2);


        URI uri = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<SimpleTask> tasks = List.of(simpleTask1, simpleTask2);
        List<SimpleTask> tasksFromServer = new ArrayList<>();

        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        for (JsonElement element : jsonArray) {
            tasksFromServer.add(gson.fromJson(element, SimpleTask.class));
        }
        assertEquals(201, response.statusCode());
    }

//    @Test
//    public void shouldGetSubTaskByEndpointTest() throws IOException, InterruptedException {
//        epic1 = new Epic("Эпик 1", "Описание эпика 1");
//        subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1 эпика 1",
//                "10:15 | 08.01.2023", 20, 1);
//        subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2 эпика 1",
//                "22:00 | 09.01.2023", 30, 1);
//
//        taskManager.createEpic(epic1);
//        taskManager.createSubTask(subTask1);
//        taskManager.createSubTask(subTask2);
//
//        URI url = URI.create("http://localhost:8080/tasks/subtask");
//        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//        List<Task> tasks = List.of(subTask1, subTask2);
//        List<Task> tasksFromServer = new ArrayList<>();
//
//        JsonElement jsonElement = JsonParser.parseString(response.body());
//        JsonArray jsonArray = jsonElement.getAsJsonArray();
//        for (JsonElement element : jsonArray)
//            tasksFromServer.add(gson.fromJson(element, SubTask.class));
//        assertEquals(tasks, tasksFromServer);
//    }
//
//    @Test
//    public void shouldGetEpicByEndpointTest() throws IOException, InterruptedException {
//        epic1 = new Epic("Эпик 1", "Описание эпика 1");
//        epic2 = new Epic("Эпик 1", "Описание эпика 2");
//        subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1 эпика 1",
//                "10:15 | 08.01.2023", 20, 1);
//        subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2 эпика 2",
//                "22:00 | 09.01.2023", 30, 2);
//
//        taskManager.createEpic(epic1);
//        taskManager.createEpic(epic2);
//        taskManager.createSubTask(subTask1);
//        taskManager.createSubTask(subTask2);
//
//        List<Task> epics = List.of(epic1, epic2);
//        List<Task> epicsFromServer = new ArrayList<>();
//
//        URI url = URI.create("http://localhost:8080/tasks/epic");
//        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//        JsonElement jsonElement = JsonParser.parseString(response.body());
//        JsonArray jsonArray = jsonElement.getAsJsonArray();
//        for (JsonElement element : jsonArray)
//            epicsFromServer.add(gson.fromJson(element, Epic.class));
//        assertEquals(epics, epicsFromServer);
//    }
//
//    @Test
//    public void shouldPostSimpleTaskTest() throws IOException, InterruptedException {
//        simpleTask1 = new SimpleTask("Задача 1", "Описание задачи 1",
//                "09:00 | 09.01.2023", 60);
//        simpleTask2 = new SimpleTask("Задача 2", "Описание задачи 2",
//                "22:00 | 11.01.2023", 30);
//
//        taskManager.createSimpleTask(simpleTask1);
//        URI url = URI.create("http://localhost:8080/tasks/task");
//        String json = gson.toJson(simpleTask2);
//        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
//        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
//        client.send(request, HttpResponse.BodyHandlers.ofString());
//        assertEquals(2, taskManager.getSimpleTaskList().size());
//    }
//
//    @Test
//    public void shouldPostSubTaskTest() throws IOException, InterruptedException {
//        epic1 = new Epic("Эпик 1", "Описание эпика 1");
//        subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1 эпика 1",
//                "10:15 | 08.01.2023", 20, 1);
//        subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2 эпика 1",
//                "22:00 | 09.01.2023", 30, 1);
//
//        taskManager.createSubTask(subTask1);
//
//        URI url = URI.create("http://localhost:8080/tasks/subtask");
//        String json = gson.toJson(subTask2);
//        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
//        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
//        client.send(request, HttpResponse.BodyHandlers.ofString());
//
//        assertEquals(2, taskManager.getSubTaskList().size());
//    }
//
//    @Test
//    public void shouldPostEpicTest() throws IOException, InterruptedException {
//        epic1 = new Epic("Эпик 1", "Описание эпика 1");
//        epic2 = new Epic("Эпик 1", "Описание эпика 2");
//
//        taskManager.createEpic(epic1);
//        URI url = URI.create("http://localhost:8080/tasks/epic");
//        String json = gson.toJson(epic2);
//        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
//        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
//        client.send(request, HttpResponse.BodyHandlers.ofString());
//        assertEquals(2, taskManager.getEpicList().size());
//    }
//
//    @Test
//    public void shouldDeleteSimpleTaskTest() throws IOException, InterruptedException {
//        simpleTask1 = new SimpleTask("Задача 1", "Описание задачи 1",
//                "09:00 | 09.01.2023", 60);
//        simpleTask2 = new SimpleTask("Задача 2", "Описание задачи 2",
//                "22:00 | 11.01.2023", 30);
//
//        taskManager.createSimpleTask(simpleTask1);
//        taskManager.createSimpleTask(simpleTask2);
//        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
//        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
//        client.send(request, HttpResponse.BodyHandlers.ofString());
//        assertEquals(1, taskManager.getSimpleTaskList().size());
//    }
//
//    @Test
//    public void shouldDeleteSubTaskTest() throws IOException, InterruptedException {
//        epic1 = new Epic("Эпик 1", "Описание эпика 1");
//        subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1 эпика 1",
//                "10:15 | 08.01.2023", 20, 1);
//        subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2 эпика 1",
//                "22:00 | 09.01.2023", 30, 1);
//
//        taskManager.createEpic(epic1);
//        taskManager.createSubTask(subTask1);
//        taskManager.createSubTask(subTask2);
//
//        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=1");
//        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
//        client.send(request, HttpResponse.BodyHandlers.ofString());
//
//        assertEquals(1, taskManager.getSubTaskList().size());
//    }
//
//    @Test
//    public void shouldDeleteEpicTest() throws IOException, InterruptedException {
//        epic1 = new Epic("Эпик 1", "Описание эпика 1");
//        epic2 = new Epic("Эпик 1", "Описание эпика 2");
//
//        taskManager.createEpic(epic1);
//        taskManager.createEpic(epic2);
//
//        URI url = URI.create("http://localhost:8080/tasks/epic/?id=1");
//        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
//        client.send(request, HttpResponse.BodyHandlers.ofString());
//
//        assertEquals(1, taskManager.getEpicList().size());
//    }
//
//    @Test
//    public void shouldDeleteAllTasksTest() throws IOException, InterruptedException {
//        simpleTask1 = new SimpleTask("Задача 1", "Описание задачи 1",
//                "09:00 | 09.01.2023", 60);
//        epic1 = new Epic("Эпик 1", "Описание эпика 1");
//        subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1 эпика 1",
//                "10:15 | 08.01.2023", 20, 1);
//
//        taskManager.createSimpleTask(simpleTask1);
//        taskManager.createEpic(epic1);
//        taskManager.createSubTask(subTask1);
//        URI url = URI.create("http://localhost:8080/tasks/");
//        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
//        client.send(request, HttpResponse.BodyHandlers.ofString());
//        assertEquals(0, taskManager.getPrioritizedTasks().size());
//
//    }

//
}
