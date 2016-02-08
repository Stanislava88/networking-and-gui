package com.clouway.conversation.server;

import com.clouway.conversation.time.Clock;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.clouway.conversation.util.CalendarUtil.february;
import static com.clouway.conversation.util.CalendarUtil.january;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
@RunWith(Parameterized.class)
public class ServerTest {
    Server server=null;
    private Date date = null;
    private int port;
    InetAddress host = null;

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery() {{
        setThreadingPolicy(new Synchroniser());
    }};

    @Mock
    Clock clock;

    @Parameterized.Parameters(name = "{index}: {1}-->{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {1414, january(2016, 23)}, {5050, january(2016, 24)}, {3090, january(2013, 13)}, {1478, february(1994, 4)}
        });
    }


    public ServerTest(int port, Date date) {
        this.port = port;
        this.date = date;
    }

    @Before
    public void setUp() throws Exception {
        host = InetAddress.getByName("localhost");
    }

    @After
    public void tearDown(){
        server.stopAsync();
//        try {
//            server.awaitTerminated(1, TimeUnit.SECONDS);
//        } catch (TimeoutException e) {
//        }
        server=null;
    }

    @Test
    public void sendMessageToClient() throws Exception {
        server = new Server(clock, port);

        server.startAsync();
        server.awaitRunning();

        context.checking(new Expectations() {{
            oneOf(clock).getTime();
            will(returnValue(date));
        }});

        Socket socket = new Socket(host, port);
        String fromServer = readFromServer(socket);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String expected = "Hello! " + format.format(date);

        server.stopAsync();


        assertThat(fromServer, is(equalTo(expected)));

    }

    @Test
    public void sendMessageToSecondClient() throws Exception {
        port += 2;
        server = new Server(clock, port);

        server.startAsync();
        server.awaitRunning();

        context.checking(new Expectations() {{
            atLeast(1).of(clock).getTime();
            will(returnValue(date));
        }});

        Socket socket = new Socket(host, port);
        String messageOne = readFromServer(socket);


        Socket socketTwo = new Socket(host, port);
        String fromServer = readFromServer(socketTwo);

        server.stopAsync();

        assertThat(fromServer, is(equalTo(messageOne)));
    }


    private String readFromServer(Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        socket.setSoTimeout(1000);
        return in.readLine();
    }

}
