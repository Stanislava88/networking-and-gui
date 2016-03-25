package com.clouway.download;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class DownloadAgent {
    private final String source;
    private final String destination;
    private Progress progress;

    public DownloadAgent(String source, String destination, Progress progress) {
        this.source = source;
        this.destination = destination;
        this.progress = progress;
    }

    public int download() throws IOException {
        URL url = new URL(source);
        URLConnection connection = url.openConnection();
        InputStream input = connection.getInputStream();
        OutputStream output = new BufferedOutputStream(new FileOutputStream(destination));

        int inputSize = connection.getContentLength();
        int readBytes;
        int downloaded = 0;
        byte[] data = new byte[1024];

        while ((readBytes = input.read(data)) > 0) {

            downloaded += readBytes;

            int percent = downloaded * 100 / inputSize;
            progress.update(percent);

            output.write(data, 0, readBytes);
            output.flush();
        }

        input.close();
        output.close();

        return downloaded;
    }
}
