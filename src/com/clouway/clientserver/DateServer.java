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
    private Time time;
    private ServerSocket serverSocket;

    public DateServer(int port, Time time) {
        this.port = port;
        this.time = time;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            Socket socket = serverSocket.accept();

            OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());
            Date date = time.getTime();
            out.write("Hello!" + date );

            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        serverSocket.close();
        System.out.println("The server is close!");
    }
}
