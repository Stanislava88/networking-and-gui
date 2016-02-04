package com.clouway.conversation.server;

import com.clouway.conversation.time.Clock;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
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
import java.util.Date;

import static com.clouway.conversation.util.CalendarUtil.january;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class ServerTest {
    Server server = null;

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery() {{
        setThreadingPolicy(new Synchroniser());
    }};

    @Mock
    Clock clock;

    @Before
    public void setUp() {
        server = new Server(clock);
    }

    @Test
    public void sendMessageToClient() throws IOException {
        int port = 5050;
        Date currentTime = january(2016, 23);

        new Thread() {
            @Override
            public void run() {
                server.run(port);
            }
        }.start();

        context.checking(new Expectations() {{
            oneOf(clock).getTime();
            will(returnValue(currentTime));
        }});

        Socket socket = getSocket(port);
        String fromServer = readFromServer(socket);
        socket.close();

        String expected = "Hello! 23/01/2016";

        assertThat(fromServer, is(equalTo(expected)));

    }

    private Socket getSocket(int port) throws IOException {
        InetAddress host = InetAddress.getByName("localhost");
        return new Socket(host, port);
    }

    private String readFromServer(Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        return in.readLine();
    }

}
