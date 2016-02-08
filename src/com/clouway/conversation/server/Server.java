package com.clouway.conversation.server;

import com.clouway.conversation.time.Clock;
import com.google.common.util.concurrent.AbstractExecutionThreadService;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class Server extends AbstractExecutionThreadService {
    private Clock clock;
    private int portNumber;

    public Server(Clock clock, int port) {
        this.clock = clock;
        this.portNumber = port;
    }


    @Override
    protected void run() {
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (isRunning()) {
                Socket clientSocket = serverSocket.accept();
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                out.println("Hello! "+ format.format(clock.getTime()));
                out.close();
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
