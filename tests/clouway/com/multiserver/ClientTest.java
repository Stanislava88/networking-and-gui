package clouway.com.multiserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class ClientTest {
    private final int port = 3030;
    private String host = "localhost";

    public class FakeMultiServer extends Thread {
        public void run() {
            try (ServerSocket server = new ServerSocket(port);
                 Socket client = server.accept()) {
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
