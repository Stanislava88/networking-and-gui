package com.clouway.info;

import com.google.common.util.concurrent.AbstractExecutionThreadService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class FakeServer extends AbstractExecutionThreadService {

    private PrintWriter out;
    private BufferedReader in;
    private int port;

    public FakeServer(int port) {
        this.port = port;
    }

    @Override
    public synchronized void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            Socket clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendMessageToClient(String message) {
        out.println(message);
    }

    public synchronized String receiveFromClient() throws IOException {
        return in.readLine();
    }

}