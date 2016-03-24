package com.clouway.clientserver;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class ServerTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery() {{
        setThreadingPolicy(new Synchroniser());
    }};

    class FakeClient {
        public String connect() throws IOException {
            String message = null;

            Socket socket = new Socket("localhost", port);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String readLine;
            while ((readLine = in.readLine()) != null) {
                message = readLine;
            }

            in.close();
            socket.close();

            return message;
        }
    }

    private Clock clock = context.mock(Clock.class);

    private final int port = 8080;
    private DateServer server = new DateServer(port, clock);

    @Before
    public void setUp() throws Exception {
        server.start();
    }

    @After
    public void tearDown() throws Exception {
        server.close();
    }

    @Test
    public void happyPath() throws Exception {
        FakeClient client = new FakeClient();
        final Date date = new Date();

        context.checking(new Expectations() {{
            oneOf(clock).getTime();
            will(returnValue(date));
        }});

        String actual = client.connect();
        String expected = "Hello!" + date;

        System.out.println(expected);

        assertThat(expected, is(actual));
    }
}

