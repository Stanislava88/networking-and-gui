package com.clouway.info;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
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


    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Mock
    StatusBoard board;

    @Before
    public void setUp() throws IOException {
        port = 5050;
        host = InetAddress.getByName("localhost");
        client = new Client(board);
    }


//    @Test
//    public void receiveMessage() {
//        String message = "You are number: 1";
//
//        Socket socket = null;
//        sendMessageToClient(message);
//
//
//
//        context.checking(new Expectations() {{
//            oneOf(board).printStatus(message);
//        }});
//        try {
//            socket = new Socket(host, port);
//            socket.setSoTimeout(1000);
//
//
//            client.run(socket);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

    @Test
    public void receiveSecondMessage() throws IOException {
        String messageOne= "one";
        String messageTwo= "two";

        FakeServer fakeServer= new FakeServer();

        fakeServer.run();
        Socket client= new Socket(host, port);


        context.checking(new Expectations(){{
        oneOf(board).printStatus(messageOne);
        oneOf(board).printStatus(messageTwo);
        }});

            fakeServer.messageClient(messageOne);
            fakeServer.messageClient(messageTwo);

            client.setSoTimeout(1000);

            this.client.run(client);

    }

private class FakeServer extends Thread{

    private PrintWriter out;

    @Override
    public void run(){
        try (ServerSocket serverSocket= new ServerSocket(port, 1, host)){
            Socket clientSocket= serverSocket.accept();
            out=new PrintWriter(clientSocket.getOutputStream(), true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void messageClient(String message){
        out.println(message);
    }
    
    }

}
