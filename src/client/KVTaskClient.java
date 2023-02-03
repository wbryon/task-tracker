package client;

/**
 * Класс KVTaskClient - это HTTP-клиент, который будет делегировать вызовы методов в HTTP-запросы.
 * Его будет использовать класс HttpTaskManager
 */

public class KVTaskClient {
    /**
     * Конструктор принимает URL к серверу хранилища и регистрируется.
     * При регистрации выдаётся токен (API_TOKEN), который нужен при работе с сервером.
     */
    public KVTaskClient() {
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
