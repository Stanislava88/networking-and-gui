package clouway.com.multiserver;

import com.clouway.multiserver.Client;
import com.clouway.multiserver.Console;
import com.clouway.multiserver.Display;
import org.jmock.Expectations;
import org.jmock.States;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class ClientTest {
    public Synchroniser synchroniser = new Synchroniser();

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery() {{
        setThreadingPolicy(synchroniser);
    }};

    private Display display = context.mock(Display.class);
    private Console console = context.mock(Console.class);

    public class FakeMultiServer extends Thread {


        public void run() {
            try (ServerSocket server = new ServerSocket(port)) {
                Socket socket = server.accept();

                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                for (String message : messages) {
                    out.println(message);
                }
                in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private final int port = 2020;
    private ArrayList<String> messages;
    
    @Test
    public void happyPath() throws Exception {
        final States status = context.states("status");

        final FakeMultiServer server = new FakeMultiServer();
        server.start();

        messages = new ArrayList<>();
        messages.add("Hello new Client");

        Client client = new Client("localhost", port, display, console);

        context.checking(new Expectations() {{
            oneOf(display).show("Hello new Client");
            when(status.isNot("finished"));

            oneOf(console).write();
            will(returnValue("Hello Server"));
            then(status.is("finished"));
        }});

        client.start();

        synchroniser.waitUntil(status.is("finished"));
    }

    @Test
    public void receiveSecondMessage() throws Exception {
        final States status = context.states("status");

        final FakeMultiServer server = new FakeMultiServer();
        server.start();

        messages = new ArrayList<>();
        messages.add("Hello Client");
        messages.add("You are client 1");

        Client client = new Client("localhost", port, display, console);

        context.checking(new Expectations() {{
            oneOf(display).show("Hello Client");
            when(status.isNot("finished"));

            oneOf(console).write();
            will(returnValue("Hello Server"));
            when(status.isNot("finished"));

            oneOf(display).show("You are client 1");
            then(status.is("finished"));
        }});

        client.start();

        synchroniser.waitUntil(status.is("finished"));
    }
}