package clouway.com.multiserver;

import com.clouway.multiserver.Display;
import com.clouway.multiserver.MultiServer;

import com.clouway.multiserver.Notifier;
import org.jmock.Expectations;
import org.jmock.States;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class ServerTest {
    public Synchroniser synchroniser = new Synchroniser();

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery() {{
        setThreadingPolicy(synchroniser);
    }};

    private Display display = context.mock(Display.class);
    private Notifier notifier = context.mock(Notifier.class);

    public class FakeClient {
        public void connect() {
            try {
                Socket socket = new Socket("localhost", port);

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void send() {
            try {
                Socket socket = new Socket("localhost", port);

                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println("Hello from  client .");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private final int port = 3030;

    private MultiServer server;

    @Before
    public void setUp() throws Exception {
        server = new MultiServer(port, display, notifier);

        server.start();
    }

    @After
    public void tearDown() throws Exception {
        server.shutDown();
    }

    @Test
    public void happyPath() throws Exception {
        final States state = context.states("states");

        FakeClient client = new FakeClient();

        context.checking(new Expectations() {{
            oneOf(display).show("You are client 1");
            then(state.is("finished"));
        }});

        client.connect();
        synchroniser.waitUntil(state.is("finished"));
    }

    @Test
    public void multipleConnections() throws Exception {
        final States state = context.states("states");

        FakeClient client1 = new FakeClient();
        FakeClient client2 = new FakeClient();

        context.checking(new Expectations() {{
            oneOf(display).show("You are client 1");
            when(state.isNot("finished"));

            oneOf(notifier).notify("Client 2is connected");
            when(state.isNot("finished"));

            oneOf(display).show("You are client 2");
            then(state.is("finished"));
        }});

        client1.connect();
        client2.connect();

        synchroniser.waitUntil(state.is("finished"));
    }

    @Test
    public void receiveFromClient() throws Exception {
        final States state = context.states("states");

        FakeClient client = new FakeClient();

        context.checking(new Expectations() {{
            oneOf(display).show("You are client 1");
            when(state.isNot("finished"));

            oneOf(display).show("Hello from  client .");
            then(state.is("finished"));
        }});

        client.send();
        synchroniser.waitUntil(state.is("finished"));
    }
}




