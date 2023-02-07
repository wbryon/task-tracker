import http.KVServer;

import java.io.IOException;

/**
 * Класс, исполняющий программу
 * @author  Хабибула Тамирбудаев
 */
public class Main {

    /**
     * точка входа в программу
     */
    public static void main(String[] args) throws IOException {
        new KVServer().start();
    }
}
