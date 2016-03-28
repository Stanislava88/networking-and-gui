package clouway.com.multiserver;

import com.clouway.multiserver.MultiServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class ServerTest {
    public final int port = 3030;

    class FakeClient extends Thread {
        public String connect() throws IOException {
            String read;
            String host = "localhost";
            Socket socket = new Socket(host, port);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String message = null;
            while ((read = input.readLine()) != null) {
                message = read;
                System.out.println(read);
            }
            input.close();
            socket.close();

            return message;
        }
    }

    private MultiServer server;

    @Before
    public void setUp() throws Exception {
        server = new MultiServer(port);

        server.start();
    }

    @After
    public void tearDown() throws Exception {
        server.shutDown();
    }

    @Test
    public void happyPath() throws Exception {
        FakeClient client1 = new FakeClient();

        String expected1 = "You are client #: 1";
        String actual1 = client1.connect();

        assertThat(expected1, is(actual1));
    }

    @Test
    public void multipleConnection() throws Exception {
        FakeClient client1 = new FakeClient();

        String expected1 = "You are client #: 1";
        String actual1 = client1.connect();

        FakeClient client2 = new FakeClient();
        String expected2 = "You are client #: 2";
        String actual2 = client2.connect();

        FakeClient client3 = new FakeClient();
        String expected3 = "You are client #: 3";
        String actual3 = client3.connect();

        assertThat(expected1, is(actual1));
        assertThat(expected2, is(actual2));
        assertThat(expected3, is(actual3));
    }

    @Test
    public void sendMessageToAll() throws Exception {
        FakeClient client1 = new FakeClient();
        String expected1 = "You are client #: 1" + "Client # 2 is connected " + "Client #3 is connected";
        String actual1 = client1.connect();

        FakeClient client2 = new FakeClient();
        String expected2 = "You are client #: 2" + "Clint # 3 is connected";
        String actual2 = client2.connect();

        assertThat(actual1, is(expected1));
        assertThat(actual2, is(expected2));
    }
}