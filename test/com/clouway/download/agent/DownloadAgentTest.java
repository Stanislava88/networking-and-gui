package com.clouway.download.agent;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class DownloadAgentTest {

    @Test
    public void successfulDownload() throws IOException {
        DownloadAgent da = new DownloadAgent();
        byte[] expectedImage = da.downloadFile("file:///home/clouway/workspaces/idea/networking-and-gui/resources/1920px-Cat_poster_1.jpg", new ProgressBarImplementation());

        BufferedImage downImage = ImageIO.read(new File("1920px-Cat_poster_1.jpg"));
        byte[] downloadedImage = ((DataBufferByte) downImage.getData().getDataBuffer()).getData();

        assertThat(downloadedImage, is(equalTo(expectedImage)));
    }

    @Test
    public void progressUpdating() {
        DownloadAgent da = new DownloadAgent();
        List<Long> expected = Arrays.asList(2662l, 2048l, 614l);
        List<Long> actual = new ArrayList<>();
        ProgressBar pb = new ProgressBar() {
            @Override
            public void addProgress(long progressSize) {
                actual.add(progressSize);
            }

            @Override
            public void fullSize(long contentLength) {
                actual.add(contentLength);
            }
        };
        da.downloadFile("file:///home/clouway/workspaces/idea/networking-and-gui/resources/lorem_Ipsum", pb);
        assertThat(actual, is(equalTo(expected)));

    }


}
