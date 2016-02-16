package com.clouway.info;

import org.jmock.Expectations;
import org.jmock.States;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.DeterministicExecutor;
import org.jmock.lib.concurrent.Synchroniser;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class ServerTest {
    Server server = null;
    InetAddress host = null;
    private int port;
    Synchroniser synchroniser = new Synchroniser();

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery() {{
        setThreadingPolicy(synchroniser);
    }};

    @Mock
    ConsoleMessage console;


    @Before
    public void setUp() throws UnknownHostException {
        port = 5060;
        server = new Server(port, console);
        server.startAsync();
        server.awaitRunning();
        host = InetAddress.getByName("localhost");
    }

    @After
    public void tearDown() {
        server.stopAsync();
        server.awaitTerminated();
    }

    @Test
    public void sendsMessageWithTheNumberOfTheClient() throws IOException {

        context.checking(new Expectations() {{
            oneOf(console).printMessage("Client number: 1, has just connected");
        }});

        Socket socket = new Socket(host, port);
        String serverMessage = readFromServer(socket);
        String expectedMessage = "You are client number: 1";

        assertThat(serverMessage, is(equalTo(expectedMessage)));
    }


    @Test
    public void sendsMessageToNotifyFirstClientThatSecondClientHasBeenConnected() throws IOException, InterruptedException {
        final States working = context.states("working");
        //noinspection Duplicates
        context.checking(new Expectations() {{
            oneOf(console).printMessage("Client number: 1, has just connected");
            when(working.isNot("finished"));
            oneOf(console).printMessage("Client number: 2, has just connected");
            then(working.is("finished"));
        }});
        Socket firstClient = new Socket(host, port);
        String firstMesssage = readFromServer(firstClient);

        Socket secondClient = new Socket(host, port);

        String secondMessage = readFromServer(firstClient);
        String expectedSecondMessage = "Client number 2 just joined";

        synchroniser.waitUntil(working.is("finished"));
        assertThat(firstMesssage, is(equalTo("You are client number: 1")));
        assertThat(secondMessage, is(equalTo(expectedSecondMessage)));
    }

    @Test
    public void serverReceiveMessageFromClient() throws IOException, InterruptedException {
        String messageFromClient = "i";
        final States working = context.states("working");

//        noinspection Duplicates
        context.checking(new Expectations() {{
            oneOf(console).printMessage("Client number: 1, has just connected");
            when(working.isNot("finished"));
            oneOf(console).printMessage(messageFromClient);
            then(working.is("finished"));

        }});

        Socket client = new Socket(host, port);
        String serverMessage = readFromServer(client);

        writeToServer(client, messageFromClient);
        String expectedMessage = "You are client number: 1";

        synchroniser.waitUntil(working.is("finished"));
        assertThat(serverMessage, is(equalTo(expectedMessage)));
    }

    private void writeToServer(Socket socket, String messageForServer) throws IOException {
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println(messageForServer);
    }

    private String readFromServer(Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        return in.readLine();

    }
}
