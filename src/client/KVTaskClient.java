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
    private final HttpClient client;
    private final HttpResponse.BodyHandler<String> handler;
    private HttpResponse<String> response;

    public KVTaskClient(URI uri) {
        client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json")
                .build();
        handler = HttpResponse.BodyHandlers.ofString();
        try {
            response = client.send(request, handler);
            apiToken = response.body();
        } catch (InterruptedException | IOException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
            e.printStackTrace();
        }
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8078/save/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json")
                .build();
        client.send(request, handler);
    }

    public String load(String key) throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8078/load/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json")
                .build();
        response = client.send(request, handler);
        return response.body();
    }
}
