package com.clouway.conversation.server;

import com.clouway.conversation.time.Clock;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class ServerTest {
    Server server = null;
    final int port = 1414;
    SimpleDateFormat format = null;

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery() {{
        setThreadingPolicy(new Synchroniser());
    }};

    @Mock
    Clock clock;

    @Before
    public void setUp() {
        server = new Server(clock);
        format = new SimpleDateFormat("dd/MM/yyyy");

    }

    @Test
    public void sendMessageToClient() throws IOException {
        new Thread() {
            @Override
            public void run() {
                server.run(port);
            }
        }.start();

        context.checking(new Expectations() {{
            oneOf(clock).getTime();
            will(returnValue(new Date()));
        }});
        String fromServer = readFromServer(port);

        String expected = "Hello! " + format.format(new Date());

        assertThat(fromServer, is(equalTo(expected)));

    }

    private String readFromServer(int port) throws IOException {
        InetAddress host = InetAddress.getByName("localhost");
        Socket socket = new Socket(host, port);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String fromServer = in.readLine();
        socket.close();
        return fromServer;
    }
}
