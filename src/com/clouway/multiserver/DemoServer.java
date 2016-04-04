package com.clouway.multiserver;

import java.io.IOException;


/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class DemoServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        MultiServer server = new MultiServer(2020, new Display() {
            @Override
            public void show(String message) {
                System.out.println(message);
            }
        });
        server.startAsync();
    }
}
