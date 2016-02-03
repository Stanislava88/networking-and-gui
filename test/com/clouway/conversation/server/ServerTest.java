package com.clouway.conversation.server;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class ServerTest {
    Calendar calendar=null;
    Server server=null;
    final int port =  1414;
    SimpleDateFormat format=null;

    @Before
    public void setUp(){
        calendar=new GregorianCalendar();
        server= new Server(calendar);
        server.date(new Date());
        format = new SimpleDateFormat("dd/MM/yyyy");

    }

    @Test
    public void sendMessageToClient() throws IOException {
            new Thread(){
                @Override
                public void run(){
                    server.run(port);
                }
            }.start();


            String fromServer = readFromServer(port);
            String expected= "Hello! "+format.format(calendar.getTime());

            assertThat(fromServer, is(equalTo(expected)));

    }

    private String readFromServer(int port) throws IOException {
        InetAddress host = InetAddress.getByName("localhost");
        Socket socket= new Socket(host, port);
        BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String fromServer= in.readLine();
        socket.close();
        return fromServer;
    }
}
