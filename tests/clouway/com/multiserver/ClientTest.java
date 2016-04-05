package clouway.com.multiserver;

import com.clouway.multiserver.Client;
import com.clouway.multiserver.Console;
import com.clouway.multiserver.Display;
import com.google.common.util.concurrent.AbstractExecutionThreadService;
import org.jmock.Expectations;
import org.jmock.States;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class ClientTest {
    class FakeMultiServer extends AbstractExecutionThreadService {
        private PrintWriter out;
        private BufferedReader in;

        @Override
        public synchronized void run() {
            try (ServerSocket server = new ServerSocket(port)) {
                Socket socket = server.accept();

                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private synchronized void sendMessageToClient(String message) {
            out.println(message);
        }

        private synchronized void receiveFromClient() throws IOException {
            in.readLine();
        }
    }

    public Synchroniser synchroniser = new Synchroniser();

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery() {{
        setThreadingPolicy(synchroniser);
    }};

    private Display display = context.mock(Display.class);
    private Console console = context.mock(Console.class);
    
    private final int port = 2020;
    private FakeMultiServer fakeMultiServer;
    private Client client;

    @Before
    public void setUp() throws Exception {
        fakeMultiServer = new FakeMultiServer();
        fakeMultiServer.startAsync().awaitRunning();

        client = new Client("localhost", port, display, console);
    }

    @After
    public void tearDown() throws Exception {
        fakeMultiServer.awaitTerminated();
    }

    @Test
    public void happyPath() throws Exception {
        final States status = context.states("status");
        context.checking(new Expectations() {{
            oneOf(console).write();
            will(returnValue("Hello Server"));
            when(status.isNot("finished"));

            oneOf(display).show("Hello new Client");
            then(status.is("finished"));
        }});

        client.start();
        fakeMultiServer.sendMessageToClient("Hello new Client");
        fakeMultiServer.receiveFromClient();

        synchroniser.waitUntil(status.is("finished"));
    }

    @Test
    public void multipleConnections() throws Exception {
        final States status = context.states("status");

        context.checking(new Expectations() {{
            oneOf(display).show("Hello client 1");
            when(status.isNot("finished"));

            oneOf(console).write();
            will(returnValue("Hello Server"));
            when(status.isNot("finished"));

            oneOf(display).show("Bye");
            then(status.is("finished"));
        }});
        client.start();

        fakeMultiServer.sendMessageToClient("Hello client 1");
        fakeMultiServer.receiveFromClient();
        fakeMultiServer.sendMessageToClient("Bye");

        synchroniser.waitUntil(status.is("finished"));
    }
}