package com.clouway.download.agent;

import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertArrayEquals;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class DownloadAgentTest {

    @Test
    public void successfulDownload() throws IOException {
        DownloadAgent da = new DownloadAgent();
        da.downloadFile(this.getClass().getClassLoader().getResource("1920px-Cat_poster_1.jpg").toString(), new ProgressBarImplementation());
        byte[] expectedImage = imageToByteArray(this.getClass().getClassLoader().getResource("1920px-Cat_poster_1.jpg").toString());

        byte[] downloadedImage = imageToByteArray(this.getClass().getClassLoader().getResource("Download1920px-Cat_poster_1.jpg").toString());

        assertArrayEquals(expectedImage, downloadedImage);
    }


    @Test
    public void progressUpdating() throws IOException {
        DownloadAgent da = new DownloadAgent();
        List<Long> expected = Arrays.asList(2662l, 2048l, 614l);
        List<Long> actual = new ArrayList<>();
        ProgressBar pb = new ProgressBar() {
            @Override
            public void updateProgress(long progressSize) {
                actual.add(progressSize);
            }

            @Override
            public void fullSize(long contentLength) {
                actual.add(contentLength);
            }
        };
        da.downloadFile(this.getClass().getClassLoader().getResource("lorem_Ipsum").toString(), pb);
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    private byte[] imageToByteArray(String s) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new URL(s).openStream());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(bis.read());
        return baos.toByteArray();
    }


}
