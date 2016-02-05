package com.clouway.conversation.server;

import com.clouway.conversation.time.Clock;
import com.google.common.util.concurrent.AbstractExecutionThreadService;

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


    public void setUp(Clock clock, int portNumber) {
        this.clock = clock;
        this.portNumber = portNumber;
    }

    @Override
    protected void run() throws Exception {
        ServerSocket serverSocket = new ServerSocket(portNumber);
        while (isRunning()) {
            Socket clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            out.println("Hello! " + format.format(clock.getTime()));
            clientSocket.close();
        }
    }




//
//    private Clock clock;
//    Thread serverThread= null;
//
//    public Server(Clock clock) {
//        this.clock = clock;
//    }
//
//    public void run(int portNumber) {
//        serverThread=new Thread(){
//          @Override
//            public void run() {
//              try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
//                  while (true) {
//                      Socket clientSocket = serverSocket.accept();
//                      PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
//                      SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//                      out.println("Hello! "+format.format(clock.getTime()));
//                      clientSocket.close();
//                  }
//
//              } catch (IOException e) {
//                  e.printStackTrace();
//              }
//          }
//        };
//        serverThread.start();
//
//    }
//
//
//    public void stop() {
//        serverThread.interrupt();
//    }
}
