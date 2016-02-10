package com.clouway.info;

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
import java.net.SocketException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class ClientTest {
    int port;
    Client client;
    InetAddress host;
    FakeServer fakeServer;


    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Mock
    StatusBoard board;

    @Mock
    ConsoleMessage console;//TODO finish the task (the client sends messages to the server.)

    @Before
    public void setUp() throws IOException {
        port = 5050;
        host = InetAddress.getByName("localhost");
        client = new Client(board, console);
        fakeServer = new FakeServer();
        fakeServer.start();
    }

    @After
    public void tearDown() {
        fakeServer.interrupt();
    }


    @Test
    public void receiveMessage() throws IOException {
        String message = "You are number: 1";

        Socket socket = new Socket(host, port);

        fakeServer.messageClient(message);


        context.checking(new Expectations() {{
            oneOf(board).printStatus(message);
            oneOf(console).readMessage();
            will(returnValue("test"));
        }});

        socket.setSoTimeout(1000);


        client.run(socket);

    }

    @Test
    public void receiveSecondMessage() throws IOException {
        String messageOne = "one";
        String messageTwo = "two";

        Socket client = new Socket(host, port);

        fakeServer.messageClient(messageOne);
        fakeServer.messageClient(messageTwo);

        context.checking(new Expectations() {{
            oneOf(console).readMessage();
            will(returnValue("message"));
            oneOf(board).printStatus(messageOne);
            oneOf(board).printStatus(messageTwo);
        }});


        client.setSoTimeout(1000);

        this.client.run(client);

    }

    @Test
    public void sendMessageToServer() throws IOException {
        String message = "You are number: 1";

        Socket socket = new Socket(host, port);

        fakeServer.messageClient(message);

        String messageToServer="a message";

        context.checking(new Expectations() {{
            oneOf(console).readMessage();
            will(returnValue(messageToServer));
            oneOf(board).printStatus(message);

        }});

        socket.setSoTimeout(1000);

        client.run(socket);
        String readFromServer= fakeServer.receiveFromClient();
        assertThat(messageToServer, is(equalTo(readFromServer)));
    }

    private class FakeServer extends Thread {

        private PrintWriter out;
        private BufferedReader in;
        Socket clientSocket;

        @Override
        public synchronized void run() {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                clientSocket = serverSocket.accept();
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in= new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public synchronized void messageClient(String message) {
            out.println(message);
        }

        public synchronized String receiveFromClient() throws IOException {
            clientSocket.setSoTimeout(2000);
            return in.readLine();
        }
    }

}
