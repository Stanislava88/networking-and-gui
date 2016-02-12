package com.clouway.info;

import java.io.IOException;
import java.net.Socket;
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
                Scanner scanner= new Scanner(System.in);
                return scanner.nextLine();
            }
        };
        try {
            Socket socket= new Socket("localhost", 5050);
            Client client= new Client(board, console);
            client.run(socket);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSocketException e) {
            e.printStackTrace();
        }

    }
}
