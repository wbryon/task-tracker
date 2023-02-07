package server;

import com.google.gson.*;
import com.sun.net.httpserver.*;
import controller.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.*;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

import model.*;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final HttpServer httpServer;
    private final Gson gson;
    public final TaskManager taskManager;

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public HttpTaskServer() throws IOException {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/task", this::handle);
        taskManager = Managers.getDefault();
        gson  = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .create();
        start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
        System.out.println("http://localhost:" + PORT + "/task");
    }

    /**
     * Метод, определяющий логику работы эндпойнта
     */
    public void handle(HttpExchange exchange) {
        int taskId;
        String response;
        JsonElement jsonElement;

        try {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();
            String query = exchange.getRequestURI().getQuery();
            Endpoint endpoint = getEndpoint(path, query, method);

            switch (endpoint) {
                case GET_ALL_SIMPLETASKS:
                    response = gson.toJson(taskManager.getSimpleTaskList());
                    sendResponse(exchange, response);
                    break;
                case GET_ALL_SUBTASKS:
                    response = gson.toJson(taskManager.getSubTaskList());
                    sendResponse(exchange, response);
                    break;
                case GET_ALL_EPICS:
                    response = gson.toJson(taskManager.getEpicList());
                    sendResponse(exchange, response);
                    break;
                case GET_ALL_TASKS:
                    response = gson.toJson(taskManager.getPrioritizedTasks());
                    sendResponse(exchange, response);
                    break;
                case GET_SIMPLETASK_BY_ID:
                    taskId = Integer.parseInt(query.substring(3));
                    if (taskId != -1) {
                        response = gson.toJson(taskManager.getSimpleTask(taskId));
                        sendResponse(exchange, response);
                        return;
                    }
                    exchange.sendResponseHeaders(405, 0);
                    break;
                case GET_SUBTASK_BY_ID:
                    taskId = Integer.parseInt(query.substring(3));
                    if (taskId != -1) {
                        response = gson.toJson(taskManager.getSubTask(taskId));
                        sendResponse(exchange, response);
                        return;
                    }
                    exchange.sendResponseHeaders(405, 0);
                    break;
                case GET_EPIC_BY_ID:
                    taskId = Integer.parseInt(query.substring(3));
                    if (taskId != -1) {
                        response = gson.toJson(taskManager.getEpic(taskId));
                        sendResponse(exchange, response);
                        return;
                    }
                    exchange.sendResponseHeaders(405, 0);
                    break;
                case GET_EPIC_SUBTASKS:
                    taskId = Integer.parseInt(query.substring(3));
                    if (taskId != -1) {
                        response = gson.toJson(taskManager.getEpic(taskId).getSubtasksIds());
                        sendResponse(exchange, response);
                        return;
                    }
                    exchange.sendResponseHeaders(405, 0);
                    break;
                case GET_HISTORY:
                    response = gson.toJson(taskManager.getHistory());
                    sendResponse(exchange, response);
                    break;
                case POST_SIMPLE_TASK:
                    jsonElement = JsonParser.parseString(readBody(exchange));
                    if (!jsonElement.isJsonObject()) {
                        System.out.println("Получен некорректный JSON");
                        return;
                    }
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    taskId = jsonObject.get("id").getAsInt();
                    SimpleTask taskFromJson = gson.fromJson(readBody(exchange), SimpleTask.class);
                    if (taskId < taskManager.getGeneratorId() && taskId != 0)
                        taskManager.updateSimpleTask(taskFromJson);
                    else
                        taskManager.createSimpleTask(taskFromJson);
                    exchange.sendResponseHeaders(200, 0);
                    break;
                case POST_SUBTASK:
                    jsonElement = JsonParser.parseString(readBody(exchange));
                    if (!jsonElement.isJsonObject()) {
                        System.out.println("Получен некорректный JSON");
                        return;
                    }
                    jsonObject = jsonElement.getAsJsonObject();
                    taskId = jsonObject.get("id").getAsInt();
                    SubTask subtaskFromJson = gson.fromJson(readBody(exchange), SubTask.class);
                    if (taskId < taskManager.getGeneratorId() && taskId != 0)
                        taskManager.updateSubTask(subtaskFromJson);
                    else
                        taskManager.createSubTask(subtaskFromJson);
                    exchange.sendResponseHeaders(200, 0);
                    break;
                case POST_EPIC:
                    jsonElement = JsonParser.parseString(readBody(exchange));
                    if (!jsonElement.isJsonObject()) {
                        System.out.println("Получен некорректный JSON");
                        return;
                    }
                    jsonObject = jsonElement.getAsJsonObject();
                    taskId = jsonObject.get("id").getAsInt();
                    Epic epicFromJson = gson.fromJson(readBody(exchange), Epic.class);
                    if (taskId < taskManager.getGeneratorId() && taskId != 0)
                        taskManager.updateEpic(epicFromJson);
                    else
                        taskManager.createEpic(epicFromJson);
                    exchange.sendResponseHeaders(200, 0);
                    break;
                case DELETE_SIMPLE_TASK:
                    taskId = Integer.parseInt(query.substring(3));
                    if (taskId != -1) {
                        taskManager.deleteSimpleTask(taskId);
                        exchange.sendResponseHeaders(200, 0);
                    }
                    break;
                case DELETE_EPIC:
                    taskId = Integer.parseInt(query.substring(3));
                    if (taskId != -1) {
                        taskManager.deleteEpic(taskId);
                        exchange.sendResponseHeaders(200, 0);
                    }
                    break;
                case DELETE_SUBTASK:
                    taskId = Integer.parseInt(query.substring(3));
                    if (taskId != -1) {
                        taskManager.deleteSubTask(taskId);
                        exchange.sendResponseHeaders(200, 0);
                    }
                    break;
                case DELETE_ALL_SIMPLETASKS:
                    taskManager.deleteAllSimpleTasks();
                    exchange.sendResponseHeaders(200, 0);
                    break;
                case DELETE_ALL_EPICS:
                    taskManager.deleteAllEpics();
                    exchange.sendResponseHeaders(200, 0);
                    break;
                case DELETE_ALL_SUBTASKS:
                    taskManager.deleteAllSubTasks();
                    exchange.sendResponseHeaders(200, 0);
                    break;
                case DELETE_ALL_TASKS:
                    taskManager.deleteAllSimpleTasks();
                    taskManager.deleteAllEpics();
                    exchange.sendResponseHeaders(200, 0);
                    break;
                case UNKNOWN:
                    System.out.println("Некорректный запрос");
                    exchange.sendResponseHeaders(405, 0);
                default:
                    exchange.sendResponseHeaders(405, 0);
            }
        } catch (Exception e) {
            System.out.println("Во время выполнения запроса возникла ошибка");
        } finally {
            exchange.close();
        }
    }

    /**
     * Метод, обрабатывающий эндпойнт из запроса
     */
    private Endpoint getEndpoint(String path, String query, String requestMethod) {

        switch (requestMethod) {
            case "GET":
                if (!Pattern.matches("/tasks/task", path)) {
                    if (Pattern.matches("/tasks/subtask", path))
                        return Endpoint.GET_ALL_SUBTASKS;
                    if (Pattern.matches("/tasks/epic", path))
                        return Endpoint.GET_ALL_EPICS;
                    if (Pattern.matches("/tasks/", path))
                        return Endpoint.GET_ALL_TASKS;
                    if (Pattern.matches("/tasks/task/id=\\d*", path + query))
                        return Endpoint.GET_SIMPLETASK_BY_ID;
                    if (Pattern.matches("/tasks/subtask/id=\\d*", path + query))
                        return Endpoint.GET_SUBTASK_BY_ID;
                    if (Pattern.matches("/tasks/epic/id=\\d*", path + query))
                        return Endpoint.GET_EPIC_BY_ID;
                    if (Pattern.matches("/tasks/subtask/epic/id=\\d*", path + query))
                        return Endpoint.GET_EPIC_SUBTASKS;
                    if (Pattern.matches("/tasks/history", path))
                        return Endpoint.GET_HISTORY;
                } else
                    return Endpoint.GET_ALL_SIMPLETASKS;
                break;
            case "POST":
                if (Pattern.matches("/tasks/task", path))
                    return Endpoint.POST_SIMPLE_TASK;
                if (Pattern.matches("/tasks/subtask", path))
                    return Endpoint.POST_SUBTASK;
                if (Pattern.matches("/tasks/epic", path))
                    return Endpoint.POST_EPIC;
                break;
            case "DELETE":
                if (Pattern.matches("/tasks/task/id=\\d*", path + query))
                    return Endpoint.DELETE_SIMPLE_TASK;
                if (Pattern.matches("/tasks/subtask/id=\\d*", path + query))
                    return Endpoint.DELETE_SUBTASK;
                if (Pattern.matches("/tasks/epic/id=\\d*", path + query))
                    return Endpoint.DELETE_EPIC;
                if (Pattern.matches("/tasks/task", path))
                    return Endpoint.DELETE_ALL_SIMPLETASKS;
                if (Pattern.matches("/tasks/subtask", path))
                    return Endpoint.DELETE_ALL_SUBTASKS;
                if (Pattern.matches("/tasks/epic", path))
                    return Endpoint.DELETE_ALL_EPICS;
                if (Pattern.matches("/tasks/", path))
                    return Endpoint.DELETE_ALL_TASKS;
            default:
                System.out.println("Эндпойнт не найден");
        }
        return Endpoint.UNKNOWN;
    }

    /**
     * Метод, запускающий сервер
     */
    public void start() {
        httpServer.start();
    }

    /**
     * Метод, останавливающий сервер
     */
    public void stop() {
        httpServer.stop(0);
    }

    /**
     * Метод, обрабатывающий тело запроса
     */
    public String readBody(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
        inputStream.close();
        return body;
    }

    /**
     * Метод, создающий ответ сервера
     */
    public void sendResponse(HttpExchange exchange, String text) throws IOException {
        byte[] response = text.getBytes(DEFAULT_CHARSET);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }
}

