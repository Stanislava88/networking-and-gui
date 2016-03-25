package com.clouway.multiserver;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class Dispatcher {
    List<Object> listOfClients = new ArrayList<>();

    public synchronized void add(Socket socket) {
        listOfClients.add(socket);
    }

    public synchronized int getNumber() {
        return listOfClients.size();
    }
}
