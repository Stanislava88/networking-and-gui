package com.clouway.clientserver;

import java.io.IOException;

import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class DateServer extends Thread {
    private final int port;
    private Clock clock;

    public DateServer(int port, Clock clock) {
        this.port = port;
        this.clock = clock;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port);
             Socket socket = serverSocket.accept()) {

            OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());
            Date date = clock.getDate();
            out.write("Hello!" + date);

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
