package com.clouway.multiserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class Client extends Thread {
    private final String host;
    private final int port;
    private Display display;
    private Console console;

    public Client(String host, int port, Display display, Console console) {
        this.host = host;
        this.port = port;
        this.display = display;
        this.console = console;
    }

    @Override
    public void run() {
        Socket socket = null;
        try {
            socket = new Socket(host, port);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String msg = console.write();
            out.println(msg);

            String readFromServer;
            while ((readFromServer = in.readLine()) != null) {
                display.show(readFromServer);
            }
        } catch (IOException | ClosedSocketException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


