package http;

import com.google.gson.*;
import com.sun.net.httpserver.*;
import controller.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.*;
import java.util.regex.Pattern;

import model.*;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final HttpServer httpServer;
    public final TaskManager taskManager;
    private final Gson gson;

    public HttpTaskServer() throws IOException {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/task", new Handler());
        taskManager = Managers.getDefault();
        gson = new Gson();
        start();
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    class Handler implements HttpHandler {
        private Endpoint getEndpoint(String path, String query, String requestMethod) {

            switch (requestMethod) {
                case "GET":
                    if (Pattern.matches("/tasks/task", path)) {
                        return Endpoint.GET_SIMPLETASKS;
                    } else if (Pattern.matches("/tasks/subtask", path)) {
                        return Endpoint.GET_SUBTASKS;
                    } else if (Pattern.matches("/tasks/epic", path)) {
                        return Endpoint.GET_EPICS;
                    } else if (Pattern.matches("/tasks/*", path)) {
                        return Endpoint.GET_ALL_TASKS;
                    } else if (Pattern.matches("/tasks/task/id=\\d*", path + query)) {
                        return Endpoint.GET_SIMPLETASK_BY_ID;
                    } else if (Pattern.matches("/tasks/subtask/id=\\d*", path + query)) {
                        return Endpoint.GET_SUBTASK_BY_ID;
                    } else if (Pattern.matches("/tasks/epic/id=\\d*", path + query)) {
                        return Endpoint.GET_EPIC_BY_ID;
                    } else if (Pattern.matches("/tasks/subtask/epic/id=\\d*", path + query)) {
                        return Endpoint.GET_EPIC_SUBTASKS;
                    } else if (Pattern.matches("/tasks/history", path)) {
                        return Endpoint.GET_HISTORY;
                    }
                    break;
                case "POST":
                    if (Pattern.matches("/tasks/task", path)) {
                        return Endpoint.POST_SIMPLE_TASK;
                    } else if (Pattern.matches("/tasks/subtask", path)) {
                        return Endpoint.POST_SUBTASK;
                    } else if (Pattern.matches("/tasks/epic", path)) {
                        return Endpoint.POST_EPIC;
                    }
                    break;
                case "DELETE":
                    if (Pattern.matches("/tasks/task/id=\\d*", path + query)) {
                        return Endpoint.DELETE_SIMPLE_TASK;
                    } else if (Pattern.matches("/tasks/subtask/id=\\d*", path + query)) {
                        return Endpoint.DELETE_SUBTASK;
                    } else if (Pattern.matches("/tasks/epic/id=\\d*", path + query)) {
                        return Endpoint.DELETE_EPIC;
                    } else if (Pattern.matches("/tasks/task", path)) {
                        return Endpoint.DELETE_SIMPLETASKS;
                    } else if (Pattern.matches("/tasks/subtask", path)) {
                        return Endpoint.DELETE_SUBTASKS;
                    } else if (Pattern.matches("/tasks/epic", path)) {
                        return Endpoint.DELETE_EPICS;
                    } else if (Pattern.matches("/tasks/", path)) {
                        return Endpoint.DELETE_ALL_TASKS;
                    }
                default:
                    System.out.println("Эндпойнт не найден");
            }
            return Endpoint.UNKNOWN;
        }

        @Override
        public void handle(HttpExchange exchange) {
            int taskId;
            String response;
            String body;
            JsonElement jsonElement;

            try {
                String path = exchange.getRequestURI().getPath();
                String method = exchange.getRequestMethod();
                String query = exchange.getRequestURI().getQuery();
                Endpoint endpoint = getEndpoint(path, query, method);

                switch (endpoint) {
                    case GET_SIMPLETASKS:
                        response = gson.toJson(taskManager.getTaskList());
                        sendText(exchange, response);
                        break;
                    case GET_SUBTASKS:
                        response = gson.toJson(taskManager.getSubTaskList());
                        sendText(exchange, response);
                        break;
                    case GET_EPICS:
                        response = gson.toJson(taskManager.getEpicList());
                        sendText(exchange, response);
                        break;
                    case GET_ALL_TASKS:
                        response = gson.toJson(taskManager.getPrioritizedTasks());
                        sendText(exchange, response);
                        break;
                    case GET_SIMPLETASK_BY_ID:
                        taskId = Integer.parseInt(query.substring(3));
                        if (taskId != -1) {
                            response = gson.toJson(taskManager.getTask(taskId));
                            sendText(exchange, response);
                            return;
                        }
                        exchange.sendResponseHeaders(405, 0);
                        break;
                    case GET_SUBTASK_BY_ID:
                        taskId = Integer.parseInt(query.substring(3));
                        if (taskId != -1) {
                            response = gson.toJson(taskManager.getSubTask(taskId));
                            sendText(exchange, response);
                            return;
                        }
                        exchange.sendResponseHeaders(405, 0);
                        break;
                    case GET_EPIC_BY_ID:
                        taskId = Integer.parseInt(query.substring(3));
                        if (taskId != -1) {
                            response = gson.toJson(taskManager.getEpic(taskId));
                            sendText(exchange, response);
                            return;
                        }
                        exchange.sendResponseHeaders(405, 0);
                        break;
                    case GET_EPIC_SUBTASKS:
                        taskId = Integer.parseInt(query.substring(3));
                        if (taskId != -1) {
                            response = gson.toJson(taskManager.getEpic(taskId).getSubtasksIds());
                            sendText(exchange, response);
                            return;
                        }
                        exchange.sendResponseHeaders(405, 0);
                        break;
                    case GET_HISTORY:
                        response = gson.toJson(taskManager.getHistory());
                        sendText(exchange, response);
                        break;
                    case POST_SIMPLE_TASK:
                        InputStream inputStream = exchange.getRequestBody();
                        body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        inputStream.close();
                        jsonElement = JsonParser.parseString(body);
                        if (!jsonElement.isJsonObject()) {
                            System.out.println("Ответ от сервера не соответствует ожидаемому.");
                            return;
                        }
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        taskId = jsonObject.get("id").getAsInt();
                        Task taskFromJson = gson.fromJson(body, Task.class);
                        if (taskId < taskManager.getGeneratorId() && taskId != 0) {
                            taskManager.updateTask(taskFromJson);
                        } else {
                            taskManager.createTask(taskFromJson);
                        }
                        exchange.sendResponseHeaders(200, 0);
                        break;
                    case POST_SUBTASK:
                        inputStream = exchange.getRequestBody();
                        body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        inputStream.close();
                        jsonElement = JsonParser.parseString(body);
                        if (!jsonElement.isJsonObject()) {
                            System.out.println("Ответ от сервера не соответствует ожидаемому.");
                            return;
                        }
                        jsonObject = jsonElement.getAsJsonObject();
                        taskId = jsonObject.get("id").getAsInt();
                        SubTask subtaskFromJson = gson.fromJson(body, SubTask.class);
                        if (taskId < taskManager.getGeneratorId() && taskId != 0) {
                            taskManager.updateSubTask(subtaskFromJson);
                        } else {
                            taskManager.createSubTask(subtaskFromJson);
                        }
                        exchange.sendResponseHeaders(200, 0);
                        break;
                    case POST_EPIC:
                        inputStream = exchange.getRequestBody();
                        body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        inputStream.close();
                        jsonElement = JsonParser.parseString(body);
                        if (!jsonElement.isJsonObject()) {
                            System.out.println("Ответ от сервера не соответствует ожидаемому.");
                            return;
                        }
                        jsonObject = jsonElement.getAsJsonObject();
                        taskId = jsonObject.get("id").getAsInt();
                        Epic epicFromJson = gson.fromJson(body, Epic.class);
                        if (taskId < taskManager.getGeneratorId() && taskId != 0) {
                            taskManager.updateEpic(epicFromJson);
                        } else {
                            taskManager.createEpic(epicFromJson);
                        }
                        exchange.sendResponseHeaders(200, 0);
                        break;
                    case DELETE_SIMPLE_TASK:
                        taskId = Integer.parseInt(query.substring(3));
                        if (taskId != -1) {
                            taskManager.deleteTask(taskId);
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
                    case DELETE_SIMPLETASKS:
                        taskManager.deleteAllTasks();
                        exchange.sendResponseHeaders(200, 0);
                        break;
                    case DELETE_EPICS:
                        taskManager.deleteAllEpics();
                        exchange.sendResponseHeaders(200, 0);
                        break;
                    case DELETE_SUBTASKS:
                        taskManager.deleteAllSubTasks();
                        exchange.sendResponseHeaders(200, 0);
                        break;
                    case DELETE_ALL_TASKS:
                        taskManager.deleteAllTasks();
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
                e.printStackTrace();
                System.out.println("Во время выполнения запроса возникла ошибка");
            } finally {
                exchange.close();
            }
        }
    }

    public void start() {
        System.out.println("Запустили сервер на порту " + PORT);
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("Остановили сервер на порту " + PORT);
    }

    public void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] response = text.getBytes(DEFAULT_CHARSET);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(201, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }
}