package com.clouway.multiserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class MultiServer extends Thread {
    private final int port;

    private Display display;
    private Notifier notifier;

    private ServerSocket serverSocket;

    public MultiServer(int port, Display display, Notifier notifier) {
        this.port = port;
        this.display = display;
        this.notifier = notifier;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            Dispatcher dispatcher = new Dispatcher(notifier);
            Socket socket;

            while (true) {
                socket = serverSocket.accept();

                dispatcher.notifyClients();

                ClientHandler client = new ClientHandler(socket, display, dispatcher);

                dispatcher.add(socket);

                client.start();
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void shutDown() throws IOException {
        serverSocket.close();
    }
}
