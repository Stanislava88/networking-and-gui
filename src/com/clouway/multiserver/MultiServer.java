package com.clouway.multiserver;

import com.google.common.util.concurrent.AbstractExecutionThreadService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class MultiServer extends AbstractExecutionThreadService {
    private final int port;

    private Display display;

    private ServerSocket serverSocket;

    public MultiServer(int port, Display display) {
        this.port = port;
        this.display = display;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            Dispatcher dispatcher = new Dispatcher(display);
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

    @Override
    public void shutDown() throws IOException {
        serverSocket.close();
    }
}
