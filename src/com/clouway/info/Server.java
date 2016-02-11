package com.clouway.info;

import com.google.common.util.concurrent.AbstractExecutionThreadService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class Server extends AbstractExecutionThreadService {
    private int port;
    private ConsoleMessage console;
    private ServerSocket serverSocket = null;
    private List<Socket> clientList = new ArrayList<>();

    public Server(int port, ConsoleMessage console) {
        this.port = port;
        this.console = console;
    }


    @Override
    protected void run() throws Exception {
        while (isRunning()) {
            try {
                serverSocket.setSoTimeout(100);
                Socket clientSocket = serverSocket.accept();

                int clientNumber = clientList.size() + 1;
                console.printMessage("Client number: " + clientNumber + ", has just connected");
                sendMessageToClient(clientSocket, "You are client number: " + clientNumber);
                sendMessageToClients(clientList, clientNumber);
                clientList.add(clientSocket);

                readFromAllClients();
            } catch (SocketTimeoutException ex) {
                readFromAllClients();
            }

        }

    }

    private synchronized void readFromAllClients() {
        for (Socket client : clientList) {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                client.setSoTimeout(100);
                console.printMessage(in.readLine());
            } catch (SocketTimeoutException ignored) {
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void sendMessageToClients(List<Socket> clientList, int clientNumber) {
        for (Socket client : clientList) {
            String message = "Client number " + clientNumber + " just joined";
            sendMessageToClient(client, message);
        }
    }

    private void sendMessageToClient(Socket clientSocket, String message) {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void startUp() throws Exception {
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


}
