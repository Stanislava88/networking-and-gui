package com.clouway.download.agent;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.net.URL;

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
        da.downloadFile("https://upload.wikimedia.org/wikipedia/commons/thumb/0/0b/Cat_poster_1.jpg/1920px-Cat_poster_1.jpg");


        BufferedImage expImage = ImageIO.read(new URL("https://upload.wikimedia.org/wikipedia/commons/thumb/0/0b/Cat_poster_1.jpg/1920px-Cat_poster_1.jpg").openStream());
        byte[] expectedImage = ((DataBufferByte) expImage.getData().getDataBuffer()).getData();

        BufferedImage downImage = ImageIO.read(new File("1920px-Cat_poster_1.jpg"));
        byte[] downloadedImage = ((DataBufferByte) downImage.getData().getDataBuffer()).getData();


        assertThat(downloadedImage, is(equalTo(expectedImage)));
    }


}
