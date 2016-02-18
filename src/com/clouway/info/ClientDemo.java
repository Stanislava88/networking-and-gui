package com.clouway.info;

import java.util.Scanner;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class ClientDemo {
    public static void main(String[] args) {
        StatusBoard board = new StatusBoard() {
            @Override
            public void printStatus(String status) {
                System.out.println(status);
            }
        };
        ConsoleMessage console = new ConsoleMessage() {
            @Override
            public void printMessage(String message) {
                System.out.println(message);
            }

            @Override
            public String readMessage() {
                Scanner scanner = new Scanner(System.in);
                return scanner.nextLine();
            }
        };

            Client client = new Client(board, console, 5050);
            client.start();


    }
}
