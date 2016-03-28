package com.clouway.multiserver;

import java.io.IOException;

import java.util.Iterator;
import java.util.Vector;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class Dispatcher extends Thread {
    private Vector<ClientHandler> listOfClients = new Vector<>();

    public synchronized void add(ClientHandler socket) {
        listOfClients.add(socket);
    }

    public synchronized int getNumber() {
        return listOfClients.size();
    }

    public synchronized void sendMessageToAll() throws IOException {
        for (int i = 0; i < listOfClients.size() - 1; i++) {
            ClientHandler connected = listOfClients.get(i);
            String message = "Client #" + (listOfClients.size()) + "is connected";
            System.out.println(message);
            connected.send(message);
        }
    }
}


