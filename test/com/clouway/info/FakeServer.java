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
    private Socket clientSocket;
    private int port;
    private ServerSocket serverSocket;

    public FakeServer(int port) {
        this.port = port;
    }

    @Override
    public synchronized void run() {
        try {
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void startUp() throws IOException {
        serverSocket = new ServerSocket(port);
    }

    @Override
    protected void triggerShutdown() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void messageToClient(String message, boolean closeSocket) {
        out.println(message);
        if (closeSocket) {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized String receiveFromClient() throws IOException {
        return in.readLine();
    }

    public void closeSocket() throws IOException {
        serverSocket.close();
    }
}