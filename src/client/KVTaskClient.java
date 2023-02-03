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
                .GET()    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build(); // заканчиваем настройку и создаём ("строим") HTTP-запрос
        handler = HttpResponse.BodyHandlers.ofString(); // получаем стандартный обработчик тела ответа с конвертацией содержимого в строку
        response = client.send(request, handler);
    }

    /**
     * Метод должен сохранять состояние менеджера задач через запрос 'POST /save/<ключ>?API_TOKEN='
     */
    void put(String key, String json) {}

    /**
     * Метод должен возвращать состояние менеджера задач через запрос 'GET /load/<ключ>?API_TOKEN='
     */
    String load(String key) {
        return null;
    }

}
