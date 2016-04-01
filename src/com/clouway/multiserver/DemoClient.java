package com.clouway.multiserver;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class DemoClient {
    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost", 2020, new Display() {
            @Override
            public void show(String message) {
                System.out.println(message);
            }
        }, new Console() {
            @Override
            public String write() {
                Scanner scanner = new Scanner(System.in);
                String msg = scanner.nextLine();
                if (msg == null) {
                    return null;
                }
                return msg;
            }
        });
        client.start();
    }
}
