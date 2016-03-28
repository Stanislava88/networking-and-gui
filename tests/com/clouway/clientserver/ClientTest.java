package com.clouway.clientserver;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class ClientTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private Display display = context.mock(Display.class);

    class FakeDateServer extends Thread {
        @Override
        public void run() {
            try {
                ServerSocket server = new ServerSocket(2024);
                Socket socket = server.accept();

                OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());
                out.write("Some text");

                out.close();
                socket.close();
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Client client;

    @Before
    public void setUp() throws Exception {
        String host = "localhost";

        client = new Client(2024, host, display);
    }

    @Test
    public void happyPath() throws Exception {
        FakeDateServer server = new FakeDateServer();
        server.start();

        context.checking(new Expectations() {{
            oneOf(display).show("Some text");
        }});

        client.connect();
    }
}
