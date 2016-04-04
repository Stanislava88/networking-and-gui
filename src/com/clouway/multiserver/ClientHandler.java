package com.clouway.multiserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class ClientHandler extends Thread {
    private Socket socket;
    private NotifyAgent notifyAgent;
    private Display display;

    public ClientHandler(Socket socket, Display display, NotifyAgent notifyAgent) {
        this.socket = socket;
        this.display = display;
        this.notifyAgent = notifyAgent;
    }

    @Override
    public void run() {
        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String msg = "You are client number: " + notifyAgent.getClientCount();

            writer.println(msg);
            display.show(msg);

            String input = in.readLine();
            display.show(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

