package com.clouway.info;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

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

    @Before
    public void setUp() throws IOException {
        port = 5050;
        host = InetAddress.getByName("localhost");
        client = new Client(board);
        fakeServer= new FakeServer();
        fakeServer.start();
    }

@After
public void tearDown(){
    fakeServer.interrupt();
}


    @Test
    public void receiveMessage() throws IOException {
        String message = "You are number: 1";

        Socket socket = new Socket(host, port);

        fakeServer.messageClient(message);



        context.checking(new Expectations() {{
            oneOf(board).printStatus(message);
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
            oneOf(board).printStatus(messageOne);
            oneOf(board).printStatus(messageTwo);
        }});


        client.setSoTimeout(1000);

        this.client.run(client);


    }

    private class FakeServer extends Thread {

        private PrintWriter out;

        @Override
        public synchronized void run() {
            try (ServerSocket serverSocket = new ServerSocket(port, 1, host)) {
                Socket clientSocket = serverSocket.accept();
                out = new PrintWriter(clientSocket.getOutputStream(), true);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public synchronized void messageClient(String message) {
            out.println(message);
        }

    }

}
