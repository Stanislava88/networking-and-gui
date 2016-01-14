package com.clouway.download.agent;

import com.clouway.download.agent.DownloadAgent;
import com.clouway.download.agent.ProgressBar;
import com.clouway.download.agent.ProgressBarImplementation;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class DownloadAgentTest {

    @Test
    public void successfulDownload() throws IOException {
        DownloadAgent da = new DownloadAgent();
        URL resource = this.getClass().getResource("/1920px-Cat_poster_1.jpg");
        da.downloadFile(resource.toString(), new ProgressBarImplementation());
        byte[] expectedImage = imageToByteArray(resource.toString());

        byte[] downloadedImage = imageToByteArray(this.getClass().getResource("/Download1920px-Cat_poster_1.jpg").toString());

        assertArrayEquals(expectedImage, downloadedImage);
    }


    @Test
    public void progressUpdating() throws IOException {
        DownloadAgent da = new DownloadAgent();
        List<Integer> expected = Arrays.asList(77, 100);
        List<Integer> actual = new ArrayList<>();
        ProgressBar pb = new ProgressBar() {
            @Override
            public void updateProgress(int progressSize) {
                actual.add(progressSize);
            }
        };
        da.downloadFile(this.getClass().getResource("/lorem_Ipsum").toString(), pb);
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    private byte[] imageToByteArray(String s) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new URL(s).openStream());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[2048];
        int n;
        while (-1 != (n = bis.read(buf))) {
            baos.write(buf);
        }
        return baos.toByteArray();
    }


}
