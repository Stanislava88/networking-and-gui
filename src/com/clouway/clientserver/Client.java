package com.clouway.clientserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class Client {
    private final int port;
    private final String host;
    private final Display display;

    public Client(int port, String host, Display display) {
        this.port = port;
        this.host = host;
        this.display = display;
    }

    public void connect() throws IOException {
        Socket socket = new Socket(host, port);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String readLine;

        while ((readLine = in.readLine()) != null) {
            display.show(readLine);
            System.out.println(readLine);
        }

        in.close();
        socket.close();
    }
}
