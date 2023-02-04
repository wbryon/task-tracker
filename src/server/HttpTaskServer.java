package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import controller.Managers;
import controller.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.regex.Pattern;

import model.*;

public class HttpTaskServer {
    public static final int PORT = 8888;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final HttpServer httpServer;
    private final Gson gson;
    public final TaskManager taskManager;

    public HttpTaskServer() throws IOException {
        taskManager = Managers.getDefault();
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress("localhost", PORT), 0);
        httpServer.createContext("/tasks", this::handle);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        gson = gsonBuilder.create();
    }

    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        String query = exchange.getRequestURI().getQuery();
        Endpoint endpoint = getEndpoint(path, query, method);
    }

    public void start() {
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }

    private void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
        if(responseString.isBlank()) {
            exchange.sendResponseHeaders(responseCode, 0);
        } else {
            byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
            exchange.sendResponseHeaders(responseCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        exchange.close();
    }

    public void getAllTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8888/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void createTask() {
        URI url = URI.create("http://localhost:8888/tasks/task/");
        Gson gson = new Gson();
//        String json = gson.toJson(newTask);
//        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
//        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void getTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8888/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private Endpoint getEndpoint(String path, String query, String requestMethod) {

        switch (requestMethod) {
            case "GET":
                if (!Pattern.matches("^/tasks/task$", path)) {
                    if (Pattern.matches("^/tasks/subtask$", path))
                        return Endpoint.GET_ALL_SUBTASKS;
                    if (Pattern.matches("^/tasks/epic$", path))
                        return Endpoint.GET_ALL_EPICS;
                    if (Pattern.matches("^/tasks/$", path))
                        return Endpoint.GET_ALL_TASKS;
                    if (Pattern.matches("^/tasks/task/id=\\d+$", path + query))
                        return Endpoint.GET_SIMPLETASK_BY_ID;
                    if (Pattern.matches("^/tasks/subtask/id=\\d+$", path + query))
                        return Endpoint.GET_SUBTASK_BY_ID;
                    if (Pattern.matches("^/tasks/epic/id=\\d+$", path + query))
                        return Endpoint.GET_EPIC_BY_ID;
                    if (Pattern.matches("^/tasks/subtask/epic/id=\\d+$", path + query))
                        return Endpoint.GET_EPIC_SUBTASKS;
                    if (Pattern.matches("^/tasks/history$", path))
                        return Endpoint.GET_HISTORY;
                } else
                    return Endpoint.GET_ALL_SIMPLETASKS;
                break;
            case "POST":
                if (Pattern.matches("^/tasks/task$", path))
                    return Endpoint.POST_SIMPLE_TASK;
                if (Pattern.matches("^/tasks/subtask$", path))
                    return Endpoint.POST_SUBTASK;
                if (Pattern.matches("^/tasks/epic$", path))
                    return Endpoint.POST_EPIC;
                break;
            case "DELETE":
                if (Pattern.matches("^/tasks/task/id=\\d+$", path + query))
                    return Endpoint.DELETE_SIMPLE_TASK;
                if (Pattern.matches("^/tasks/subtask/id=\\d+$", path + query))
                    return Endpoint.DELETE_SUBTASK;
                if (Pattern.matches("^/tasks/epic/id=\\d+$", path + query))
                    return Endpoint.DELETE_EPIC;
                if (Pattern.matches("^/tasks/task$", path))
                    return Endpoint.DELETE_ALL_SIMPLETASKS;
                if (Pattern.matches("^/tasks/subtask$", path))
                    return Endpoint.DELETE_ALL_SUBTASKS;
                if (Pattern.matches("^/tasks/epic$", path))
                    return Endpoint.DELETE_ALL_EPICS;
                if (Pattern.matches("^/tasks/$", path))
                    return Endpoint.DELETE_ALL_TASKS;
            default:
                System.out.println("Эндпойнт не найден");
        }
        return Endpoint.UNKNOWN;
    }
}
