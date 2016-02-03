package com.clouway.conversation.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class Server {

    private Calendar calendar;

    public Server(Calendar calendar) {
        this.calendar=calendar;
    }

    public void run(int portNumber) {
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            Socket clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            out.println("Hello! " + format.format(calendar.getTime()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void date(Date date) {
        calendar.setTime(date);
    }
}
