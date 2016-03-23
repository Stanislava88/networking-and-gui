package com.clouway.clientserver;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.core.Is.is;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class ClientTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private Display display = context.mock(Display.class);

    private Client client;
    private int port = 2022;
    private String host = "localhost";

    public class FakeDateServer extends Thread {
        @Override
        public void run() {
            try {
                ServerSocket server = new ServerSocket(port);
                Socket socket = server.accept();
                OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());
                out.write("Hello");
                out.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Before
    public void setUp() throws Exception {
        client = new Client(port, host, display);
    }

    @Test
    public void happyPath() throws Exception {
        FakeDateServer server = new FakeDateServer();
        server.start();

        context.checking(new Expectations() {{
            allowing(display).show("Hello");
        }});

        client.connect();
    }
}
