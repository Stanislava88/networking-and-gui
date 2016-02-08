package com.clouway.info;

import com.google.common.util.concurrent.AbstractExecutionThreadService;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class Server extends AbstractExecutionThreadService {
    private int port;
    ServerSocket serverSocket = null;
    private List<Socket> clientList = new ArrayList<>();

    public Server(int port) {
        this.port = port;
    }


    @Override
    protected void run() throws Exception {
        try {
            while (serverSocket.isBound()) {
                Socket clientSocket = serverSocket.accept();

                int clientNumber = clientList.size() + 1;

                sendMessageToClients(clientList, clientNumber);
                sendMessageToClient(clientSocket, "You are client number: " + clientNumber);
                clientList.add(clientSocket);
            }
        } catch (SocketException e) {
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
