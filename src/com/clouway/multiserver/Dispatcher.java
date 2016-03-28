package com.clouway.multiserver;

import java.io.IOException;

import java.util.Vector;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class Dispatcher {
    private Vector<ClientHandler> listOfClients = new Vector<>();

    public synchronized void add(ClientHandler socket) {
        listOfClients.add(socket);
    }

    public synchronized int getNumber() {
        return listOfClients.size();
    }

    public synchronized void sendMessageToAll() throws IOException {
        for (ClientHandler each : listOfClients) {
            String message = "Client #" + (listOfClients.size() + 1) + "is connected";
            System.out.println(message);
            each.send(message);
        }
    }
}

