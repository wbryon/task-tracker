package client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Класс KVTaskClient - это HTTP-клиент, который будет делегировать вызовы методов в HTTP-запросы.
 * Его будет использовать класс HttpTaskManager
 */

public class KVTaskClient {
    private String apiToken;
    HttpClient client;
    HttpResponse.BodyHandler<String> handler;
    HttpResponse<String> response;
    /**
     * Конструктор принимает URL к серверу хранилища и регистрируется.
     * При регистрации выдаётся токен (API_TOKEN), который нужен при работе с сервером.
     *
     * Конструктор создаёт объект, описывающий HTTP-запрос
     */
    public KVTaskClient(URI uri) throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();
        handler = HttpResponse.BodyHandlers.ofString();
        response = client.send(request, handler);
    }

    /**
     * Метод, сохраняющий состояние менеджера задач через запрос 'POST /save/<ключ>?API_TOKEN='
     */
    public void put(String key, String json) throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8888/save/" + key + "?" + "API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        client.send(request, handler);
    }

    /**
     * Метод, возвращающий состояние менеджера задач через запрос 'GET /load/<ключ>?API_TOKEN='
     */
    public String load(String key) throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8888/load/" + key + "?" + "API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request, handler);
        return response.body();
    }
}
