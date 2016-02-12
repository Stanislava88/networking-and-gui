package com.clouway.info;

import com.google.common.util.concurrent.AbstractExecutionThreadService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class Server extends AbstractExecutionThreadService {
    private int port;
    private ConsoleMessage console;
    private ServerSocket serverSocket = null;
    private List<ClientConnection> clientList = new ArrayList<>();

    public Server(int port, ConsoleMessage console) {
        this.port = port;
        this.console = console;
    }


    @Override
    protected void run() throws Exception {
        while (isRunning()) {
            try {
                Socket clientSocket = serverSocket.accept();
                ClientConnection clientConnection = new ClientConnection(clientSocket, console);
                int clientNumber = clientList.size() + 1;
                console.printMessage("Client number: " + clientNumber + ", has just connected");

                new Thread() {
                    @Override
                    public void run() {
                        clientConnection.readFromClient();
                    }
                }.start();



                clientConnection.sendMessageToClient("You are client number: " + clientNumber);
                sendMessageToClients(clientList, clientNumber);
                clientList.add(clientConnection);
            }catch (IOException ignored){
            }

        }

    }



    private void sendMessageToClients(List<ClientConnection> clientList, int clientNumber) {
        for (ClientConnection client : clientList) {
            String message = "Client number " + clientNumber + " just joined";
            client.sendMessageToClient(message);
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
