package server;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

/**
 * Постман: https://www.getpostman.com/collections/a83b61d9e1c81c10575c
 */
public class KVServer {
    public static final int PORT = 8888;
    private final String apiToken;
    Gson gson;
    private final HttpServer server;
    private final Map<String, String> data = new HashMap<>();

    public KVServer() throws IOException {
        gson = new Gson();
        apiToken = generateApiToken();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/register", this::register);
        server.createContext("/save", this::save);
        server.createContext("/load", this::load);
    }

    /**
     * Метод, отвечающий за получение данных.
     * Доделайте логику работы сервера по комментариям (комментарии затем можно убрать).
     * После этого запустите сервер и проверьте, что получение значения по ключу работает.
     * Для начальной отладки можно делать запросы без авторизации, используя код DEBUG
     */
    private void load(HttpExchange httpExchange) throws IOException {
        if (!hasAuth(httpExchange)) {
            System.out.println("Запрос неавторизован, нужен параметр в query API_TOKEN со значением апи-ключа");
            httpExchange.sendResponseHeaders(403, 0);
            return;
        }
        if ("GET".equals(httpExchange.getRequestMethod())) {
            String key = httpExchange.getRequestURI().getPath().replaceFirst("/load/", "");
            if (data.containsKey(key)) {
                String value = data.get(key);
                sendText(httpExchange, value);
                httpExchange.close();
                return;
            }
            sendText(httpExchange, gson.toJson(null));
        }
        httpExchange.close();
    }

    private void save(HttpExchange httpExchange) throws IOException {
        try {
            System.out.println("\n/save");
            if (!hasAuth(httpExchange)) {
                System.out.println("Запрос неавторизован, нужен параметр в query API_TOKEN со значением апи-ключа");
                httpExchange.sendResponseHeaders(403, 0);
                return;
            }
            if ("POST".equals(httpExchange.getRequestMethod())) {
                String key = httpExchange.getRequestURI().getPath().substring("/save/".length());
                if (key.isEmpty()) {
                    System.out.println("Key для сохранения пустой. key указывается в пути: /save/{key}");
                    httpExchange.sendResponseHeaders(400, 0);
                    return;
                }
                String value = readText(httpExchange);
                if (value.isEmpty()) {
                    System.out.println("Value для сохранения пустой. value указывается в теле запроса");
                    httpExchange.sendResponseHeaders(400, 0);
                    return;
                }
                data.put(key, value);
                System.out.println("Значение для ключа " + key + " успешно обновлено!");
                httpExchange.sendResponseHeaders(200, 0);
            } else {
                System.out.println("/save ждёт POST-запрос, а получил: " + httpExchange.getRequestMethod());
                httpExchange.sendResponseHeaders(405, 0);
            }
        } finally {
            httpExchange.close();
        }
    }

    private void register(HttpExchange httpExchange) throws IOException {
        try {
            System.out.println("\n/register");
            if ("GET".equals(httpExchange.getRequestMethod())) {
                sendText(httpExchange, apiToken);
            } else {
                System.out.println("/register ждёт GET-запрос, а получил " + httpExchange.getRequestMethod());
                httpExchange.sendResponseHeaders(405, 0);
            }
        } finally {
            httpExchange.close();
        }
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        System.out.println("API_TOKEN: " + apiToken);
        server.start();
    }

    private String generateApiToken() {
        return "" + System.currentTimeMillis();
    }

    protected boolean hasAuth(HttpExchange h) {
        String rawQuery = h.getRequestURI().getRawQuery();
        return rawQuery != null && (rawQuery.contains("API_TOKEN=" + apiToken) || rawQuery.contains("API_TOKEN=DEBUG"));
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }
}