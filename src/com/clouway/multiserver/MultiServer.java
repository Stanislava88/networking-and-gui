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
    public void run() throws IOException {
        try {
            serverSocket = new ServerSocket(port);
            NotifyAgent notifyAgent = new NotifyAgent(display);
            Socket socket;

            while (true) {
                socket = serverSocket.accept();

                notifyAgent.notifyClients();

                ClientHandler client = new ClientHandler(socket, display, notifyAgent);

                notifyAgent.add(socket);

                client.start();
            }
        } catch (Exception e) {
            serverSocket.close();
        }
    }

    @Override
    public void shutDown() throws IOException {
        if (serverSocket != null) {
            serverSocket.close();
        }
    }
}
