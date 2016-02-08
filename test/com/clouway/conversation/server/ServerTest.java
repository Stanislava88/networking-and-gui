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
    Server server = null;
    private Date date = null;
    private int port=5050;

    InetAddress host = null;


    @Parameterized.Parameters(name = "{index}:{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {january(2016, 23)}, {january(2016, 24)}, { january(2013, 13)}, { february(1994, 4)}
        });
    }


    public ServerTest(Date date) {
        this.date = date;
    }

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery() {{
        setThreadingPolicy(new Synchroniser());
    }};

    @Mock
    Clock clock;

    @Before
    public void setUp() throws Exception {
        server = new Server(clock, port);

        server.startAsync();
        server.awaitRunning();
        host = InetAddress.getByName("localhost");
    }

    @After
    public void tearDown() {
        server.stopAsync();
        try {
            server.awaitTerminated(1, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void sendMessageToClient() throws Exception {

        context.checking(new Expectations() {{
            oneOf(clock).getTime();
            will(returnValue(date));
        }});

        Socket socket = new Socket(host, port);
        String fromServer = readFromServer(socket);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String expected = "Hello! " + format.format(date);

        assertThat(fromServer, is(equalTo(expected)));

    }

    @Test
    public void sendMessageToSecondClient() throws Exception {

        context.checking(new Expectations() {{
            exactly(2).of(clock).getTime();
            will(returnValue(date));
        }});

        Socket clientOne = new Socket(host, port);
        String messageOne = readFromServer(clientOne);


        Socket clientTwo = new Socket(host, port);
        String messageTwo = readFromServer(clientTwo);

        assertThat(messageTwo, is(equalTo(messageOne)));
    }


    private String readFromServer(Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        socket.setSoTimeout(1000);
        return in.readLine();
    }

}
