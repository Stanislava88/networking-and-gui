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
import java.net.*;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class ClientTest {
    int port;
    Client client;
    InetAddress host;
    Socket clientSocket=null;

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Mock
    StatusBoard board;

    @Before
    public void setUp() throws IOException {
        port = 5050;
        host = InetAddress.getByName("localhost");
        client = new Client(board);
        connectToServer();
    }

    @Test
    public void receiveMessage() {
        String message = "You are number: 1";

        Socket socket = null;


        context.checking(new Expectations() {{
            oneOf(board).printStatus(message);
        }});
        try {
            socket = new Socket(host, port);
            sendMessageToClient(socket, message);
            socket.setSoTimeout(1000);
            client.run(socket);
        } catch (SocketTimeoutException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void sendMessageToClient(Socket clientSocket, String message) {
        new Thread(){
            @Override
            public void run(){
                try {
                    PrintWriter out= new PrintWriter(clientSocket.getOutputStream(), true);
                    out.write(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void connectToServer() {
        new Thread(){
            @Override
            public void run(){
                try {
                    ServerSocket serverSocket= new ServerSocket(port);
                    Socket clientSocket= serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
