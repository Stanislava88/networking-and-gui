package com.clouway.info;

import org.jmock.Expectations;
import org.jmock.States;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.net.UnknownHostException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class ClientTest {
    private Client client;
    private FakeServer fakeServer;
    private Synchroniser synchroniser = new Synchroniser();


    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery() {{
        setThreadingPolicy(synchroniser);
    }};


    @Mock
    StatusBoard board;

    @Mock
    ConsoleMessage console;


    @Before
    public void setUp() throws UnknownHostException {
        int port = 6023;
        client = new Client(board, console, port);
        fakeServer = new FakeServer(port);
        fakeServer.startAsync().awaitRunning();
    }

    @After
    public void tearDown() {
        fakeServer.stopAsync().awaitTerminated();
    }

    @Test
    public void receivesMessageFromServer() throws IOException, NoSocketException, InterruptedException {
        String message = "You are number: 1";
        context.checking(new Expectations() {{
            oneOf(console).readMessage();
            will(returnValue("test"));

            oneOf(board).printStatus(message);
        }});
        client.start();
        fakeServer.sendMessageToClient(message);
    }

    @Test
    public void receivesSecondMessageFromServer() throws IOException, NoSocketException, InterruptedException {
        final States working = context.states("working");

        String messageOne = "one";
        String messageTwo = "two";
        context.checking(new Expectations() {{
            oneOf(console).readMessage();
            when(working.isNot("finished"));
            oneOf(board).printStatus(messageOne);
            when(working.isNot("finished"));
            oneOf(board).printStatus(messageTwo);
            then(working.is("finished"));
        }});
        client.start();
        fakeServer.sendMessageToClient(messageOne);
        fakeServer.sendMessageToClient(messageTwo);
        synchroniser.waitUntil(working.is("finished"));

    }


    @Test
    public void sendMessageToServer() throws IOException, NoSocketException, InterruptedException {
        String message = "You are number: 1";
        String messageToServer = "a message";


        context.checking(new Expectations() {{
            oneOf(console).readMessage();
            will(returnValue(messageToServer));

            oneOf(board).printStatus(message);
        }});
        client.start();
        fakeServer.sendMessageToClient(message);


        String readFromServer = fakeServer.receiveFromClient();
        assertThat(messageToServer, is(equalTo(readFromServer)));
    }
}
