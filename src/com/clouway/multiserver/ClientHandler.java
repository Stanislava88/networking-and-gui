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
            String message = "You are client #: " + dispatcher.getNumber();

            send(message);

            dispatcher.sendMessageToAll();

        } catch (IOException ex) {
            return;
        }
    }

    public synchronized void send(String msg) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
        writer.write(msg);

        writer.flush();

        writer.close();
        socket.close();
    }
}
