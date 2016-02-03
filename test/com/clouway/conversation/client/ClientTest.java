package com.clouway.conversation.client;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class ClientTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Mock
    StatusBoard statusBoard;
    @Mock
    MessagePrinter messagePrinter;

    @Test
    public void successfullyReceiveMessage() throws IOException {
        int port = 1415;

        messageToClient(port);
        Client client = new Client(statusBoard, messagePrinter);
        context.checking(new Expectations() {{
            exactly(4).of(statusBoard).printStatus(with(any(String.class)));
            oneOf(messagePrinter).printMessage("Message!");
        }});
        client.run(InetAddress.getByName("localhost"), port);

    }

    private void messageToClient(final int port) {
        new Thread() {
            @Override
            public void run() {
                try {
                    ServerSocket serverSocket = new ServerSocket(port);
                    Socket clientSocket = serverSocket.accept();
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    out.println("Message!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
