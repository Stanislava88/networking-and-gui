package com.clouway.conversation.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class Client {
    private final StatusBoard statusBoard;
    private final MessagePrinter messagePrinter;

    public Client(StatusBoard statusBoard, MessagePrinter messagePrinter) {
        this.statusBoard = statusBoard;
        this.messagePrinter = messagePrinter;
    }


    public void run(InetAddress host, int port) throws IOException {
        Socket socket = new Socket(host, port);
        statusBoard.printStatus("Connected to host: " + host);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String fromServer;
        fromServer = bufferedReader.readLine();
        statusBoard.printStatus("Read Message");
        messagePrinter.printMessage(fromServer);
        statusBoard.printStatus("Printed Message");

        socket.close();
        statusBoard.printStatus("Closed the connection with the server");
    }
}
