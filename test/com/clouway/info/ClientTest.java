package com.clouway.info;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class ClientTest {
    private int port;
    private Client client;
    private FakeServer fakeServer;
    Synchroniser synchroniser = new Synchroniser();

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
        port = 6023;
        client = new Client(board, console);
    }

    @After
    public void tearDown() {
        fakeServer.stopAsync();
        fakeServer.awaitTerminated();
    }

    @Test(expected = NoSocketException.class)
    public void receivesMessageFromServer() throws IOException, NoSocketException {
        fakeServer = new FakeServer(port);
        fakeServer.startAsync();
        fakeServer.awaitRunning();
        Socket socket = new Socket("localhost", port);

        String message = "You are number: 1";
        fakeServer.messageToClient(message, true);

        context.checking(new Expectations() {{
            oneOf(board).printStatus(message);
            oneOf(console).readMessage();
            will(returnValue("test"));
        }});
        client.run(socket);
    }

    @Test(expected = NoSocketException.class)
    public void receivesSecondMessageFromServer() throws IOException, NoSocketException {
        fakeServer = new FakeServer(port + 2);
        fakeServer.startAsync();
        fakeServer.awaitRunning();

        Socket socket = new Socket("localhost", port + 2);
        String messageOne = "one";
        fakeServer.messageToClient(messageOne, false);
        String messageTwo = "two";
        context.checking(new Expectations() {{
            oneOf(console).readMessage();
            oneOf(board).printStatus(messageOne);
            oneOf(board).printStatus(messageTwo);
        }});

        fakeServer.messageToClient(messageTwo, true);

        client.run(socket);
    }

    @Test(expected = NoSocketException.class)
    public void sendMessageToServer() throws IOException, NoSocketException {
        fakeServer = new FakeServer(port + 4);
        fakeServer.startAsync();
        fakeServer.awaitRunning();

        Socket socket = new Socket("localhost", port + 4);

        String message = "You are number: 1";
        fakeServer.messageToClient(message, true);

        String messageToServer = "a message";

        context.checking(new Expectations() {{
            oneOf(console).readMessage();
            will(returnValue(messageToServer));
            oneOf(board).printStatus(message);
        }});


        client.run(socket);

        String readFromServer = fakeServer.receiveFromClient();
        assertThat(messageToServer, is(equalTo(readFromServer)));
    }
}
