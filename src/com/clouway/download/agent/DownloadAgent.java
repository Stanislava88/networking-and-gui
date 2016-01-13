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

    public byte[] downloadFile(String url, ProgressBar pb) {
        try {
            URL url1 = new URL(url);
            URLConnection connection = url1.openConnection();
            connection.connect();
            pb.fullSize(connection.getContentLength());

            InputStream in = new BufferedInputStream(connection.getInputStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[2048];
            int n;
            while (-1 != (n = in.read(buf))) {
                pb.addProgress(n);
                out.write(buf, 0, n);
            }
            FileOutputStream fileOut = new FileOutputStream(fileName(url1.getFile()));
            byte[] result = out.toByteArray();
            fileOut.write(result);

            fileOut.close();
            in.close();
            out.close();
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String fileName(String file) {
        String[] files = file.split("/");

        return files[files.length - 1];
    }
}
