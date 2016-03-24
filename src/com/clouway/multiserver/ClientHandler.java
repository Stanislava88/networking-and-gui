package com.clouway.multiserver;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class ClientHandler extends Thread {
    private Socket socket;
    private Dispatcher dispatcher;

    public ClientHandler(Socket socket, Dispatcher dispatcher) {
        this.socket = socket;
        this.dispatcher = dispatcher;
    }

    @Override
    public void run() {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());

            String message = "You are client #: " + dispatcher.getNumber();
            writer.write(message);

            writer.flush();

            writer.close();
            socket.close();
        } catch (IOException ex) {
            return;
        }
    }
}
