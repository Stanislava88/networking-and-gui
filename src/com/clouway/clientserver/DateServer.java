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
    private int port;
    private Clock clock;
    private ServerSocket serverSocket;

    public DateServer(int port, Clock clock) {
        this.port = port;
        this.clock = clock;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            Socket socket = serverSocket.accept();

            OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());
            Date date = clock.getTime();
            out.write("Hello!" + date );

            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        serverSocket.close();
    }
}
