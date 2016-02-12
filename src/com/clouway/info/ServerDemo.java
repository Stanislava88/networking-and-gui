package com.clouway.info;

import java.util.Scanner;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class ServerDemo {
    public static void main(String[] args) {
        ConsoleMessage console= new ConsoleMessage() {
            @Override
            public void printMessage(String message) {
                System.out.println(message);
            }

            @Override
            public String readMessage() {
                return  null;
            }
        };
        Server server= new Server(5050, console);
        server.startAsync();
        server.awaitRunning();
    }
}
