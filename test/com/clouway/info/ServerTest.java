package com.clouway.info;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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


    @Before
    public void setUp() throws UnknownHostException {
        port = 5050;
        server = new Server(port);
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
    public void serverSendsNumberToClient() throws IOException {

        Socket socket = new Socket(host, port);
        String serverMessage = readFromServer(socket);
        String expectedMessage = "You are client number: 1";

        assertThat(serverMessage, is(equalTo(expectedMessage)));
    }

    @Test
    public void serverSendsTheFirstClientNumberOfTheSecond() throws IOException {
        Socket firstClient = new Socket(host, port);
        String firstMesssage = readFromServer(firstClient);

        Socket secondClient = new Socket(host, port);

        String secondMessage = readFromServer(firstClient);
        String expectedSecondMessage = "Client number 2 just joined";


        assertThat(secondMessage, is(equalTo(expectedSecondMessage)));
    }

    private String readFromServer(Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        socket.setSoTimeout(1000);
        return in.readLine();
    }
}
