package com.clouway.download.agent;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class DownloadAgent {

    public void downloadFile(String url, ProgressBar pb) {
        try {
            URL url1 = new URL(url);
            URLConnection connection = url1.openConnection();
            connection.connect();
            pb.fullSize(connection.getContentLength());

            InputStream in = new BufferedInputStream(connection.getInputStream());
            FileOutputStream fileOut = new FileOutputStream(fileName(url1.getFile()));
            byte[] buf = new byte[2048];
            int n;
            while (-1 != (n = in.read(buf))) {
                pb.updateProgress(n);
                fileOut.write(buf);
            }


            fileOut.close();
            in.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String fileName(String file) {
        String[] files = file.split("/");

        return "Download"+files[files.length - 1];
    }
}
