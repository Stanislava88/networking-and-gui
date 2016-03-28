package com.clouway.multiserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class MultiServer extends Thread {
    private int port;
    private ServerSocket serverSocket;
    private Dispatcher dispatcher;

    public MultiServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);

            dispatcher = new Dispatcher();

            while (!isInterrupted()) {
                Socket socket = serverSocket.accept();

                ClientHandler thread = new ClientHandler(socket, dispatcher);
                dispatcher.add(thread);

                thread.run();
                socket.close();
            }
        } catch (Exception e) {
            return;
        }
    }

    public void shutDown() throws IOException {
        serverSocket.close();
    }
}
