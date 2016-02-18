package com.clouway.info;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class Client extends Thread {

    private final StatusBoard board;
    private ConsoleMessage console;
    private int port;

    public Client(StatusBoard board, ConsoleMessage console, int port) {
        this.board = board;
        this.console = console;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket("localhost", port);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            String fromServer;
            String toServer = console.readMessage();
            out.println(toServer);
            while ((fromServer = in.readLine()) != null) {
                board.printStatus(fromServer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
