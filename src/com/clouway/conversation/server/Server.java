package com.clouway.conversation.server;

import com.clouway.conversation.time.Clock;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class Server {

    private Clock clock;

    public Server(Clock clock) {
        this.clock = clock;
    }

    public void run(int portNumber) {
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            Socket clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            out.println("Hello! " + format.format(clock.getTime()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
