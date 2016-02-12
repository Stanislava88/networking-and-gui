package com.clouway.info;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class ClientConnection {
    private final Socket clientSocket;
    private final ConsoleMessage console;

    public ClientConnection(Socket clientSocket, ConsoleMessage console) {
        this.clientSocket = clientSocket;
        this.console = console;
    }

    public synchronized void readFromClient(){
        try {
            BufferedReader in= new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            console.printMessage(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageToClient(String message){
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
