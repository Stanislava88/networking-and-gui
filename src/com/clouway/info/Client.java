package com.clouway.info;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class Client {

    private final StatusBoard board;

    public Client(StatusBoard board) {
        this.board = board;
    }

    public void run(Socket socket){
        try {
            BufferedReader in= new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String fromServer;
            while((fromServer=in.readLine())!=null){
                board.printStatus(fromServer);
            }
        }
        catch (IOException e) {
        }
    }

}
