package com.clouway.multiserver;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class NotifyAgent {
    private List<Socket> sockets = new ArrayList<>();
    private Display display;

    public NotifyAgent(Display display) {
        this.display = display;
    }

    public synchronized void add(Socket socket) {
        if (!sockets.contains(socket)) {
            sockets.add(socket);
        }
    }

    public synchronized int getNumber() {
        return sockets.size();
    }

    public synchronized void notifyClients() throws IOException, InterruptedException {
        if (sockets.isEmpty()) {
            return;
        }

        for (Socket socket : sockets) {
            String msg = "Client " + (sockets.size() + 1) + "is connected";
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            out.println(msg);
            display.show(msg);
        }
    }
}


