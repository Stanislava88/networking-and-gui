package com.clouway.info;

import com.google.common.util.concurrent.AbstractExecutionThreadService;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class ClientTest {
    private int port;
    private Client client;
    private InetAddress host;
    private FakeServer fakeServer;


    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Mock
    StatusBoard board;

    @Mock
    ConsoleMessage console;

    @Before
    public void setUp() throws IOException {
        port = 2314;
        host = InetAddress.getByName("localhost");
        client = new Client(board, console);
        fakeServer = new FakeServer();
        fakeServer.startAsync();
        fakeServer.awaitRunning();
    }

    @After
    public void tearDown() throws IOException {
        fakeServer.stopAsync();
        fakeServer.awaitTerminated();
    }


    @Test
    public void receivesMessageFromServer() throws IOException {
        String message = "You are number: 1";

        Socket socket = new Socket(host, port);

        fakeServer.messageToClient(message);

        context.checking(new Expectations() {{
            oneOf(board).printStatus(message);
            oneOf(console).readMessage();
            will(returnValue("test"));
        }});

        socket.setSoTimeout(100);
        client.run(socket);
    }

    @Test
    public void receivesSecondMessageFromServer() throws IOException {
        String messageOne = "one";
        String messageTwo = "two";

        Socket client = new Socket(host, port);

        fakeServer.messageToClient(messageOne);
        fakeServer.messageToClient(messageTwo);

        context.checking(new Expectations() {{
            oneOf(console).readMessage();
            will(returnValue("message"));
            oneOf(board).printStatus(messageOne);
            oneOf(board).printStatus(messageTwo);
        }});

        client.setSoTimeout(100);

        this.client.run(client);
    }

    @Test
    public void sendMessageToServer() throws IOException {
        String message = "You are number: 1";

        Socket socket = new Socket(host, port);

        fakeServer.messageToClient(message);

        String messageToServer="a message";

        context.checking(new Expectations() {{
            oneOf(console).readMessage();
            will(returnValue(messageToServer));
            oneOf(board).printStatus(message);

        }});

        socket.setSoTimeout(100);

        client.run(socket);
        String readFromServer= fakeServer.receiveFromClient();
        assertThat(messageToServer, is(equalTo(readFromServer)));
    }

    private class FakeServer extends AbstractExecutionThreadService {

        private PrintWriter out;
        private BufferedReader in;
        Socket clientSocket;

        @Override
        public synchronized void run() {
            try (ServerSocket serverSocket = new ServerSocket(port, 1, host)) {
                clientSocket = serverSocket.accept();
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in= new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public synchronized void messageToClient(String message) {
            out.println(message);
        }

        public synchronized String receiveFromClient() throws IOException {
            return in.readLine();
        }

    }

}
