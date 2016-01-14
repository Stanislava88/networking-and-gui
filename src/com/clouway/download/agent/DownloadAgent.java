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
            float contentLength = connection.getContentLength();
            boolean undefinedLength = contentLength < 0;
            float onePercent = contentLength / 100;
            int progress = 0;

            InputStream in = new BufferedInputStream(connection.getInputStream());
            FileOutputStream fileOut = new FileOutputStream(fileName(url1.getFile()));
            byte[] buf = new byte[2048];
            int n;

            while (-1 != (n = in.read(buf))) {
                if (!undefinedLength) {
                    progress += n;
                    int percent = Math.round(progress / onePercent);
                    if (percent != 100) {
                        pb.updateProgress(percent);
                    }
                }


                fileOut.write(buf, 0, n);
            }
            if (progress == contentLength) {
                pb.updateProgress(100);
            }

            fileOut.close();
            in.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String fileName(String file) {
        String[] files = file.split("/");

        return "Download" + files[files.length - 1];
    }
}
