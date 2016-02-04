package com.clouway.download.agent;

import com.google.common.io.ByteStreams;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

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
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();


    @Test
    public void successfulDownload() throws IOException {
        DownloadAgent da = new DownloadAgent();
        ProgressBar pb = context.mock(ProgressBar.class);
        URL resource = this.getClass().getResource("/1920px-Cat_poster_1.jpg");
        context.checking(new Expectations() {{
            atLeast(1).of(pb).updateProgress(with(any(Integer.class)));
        }});

        da.downloadFile(resource, pb);
        byte[] expectedImage = imageToByteArray(resource.toString());
        byte[] downloadedImage = imageToByteArray(this.getClass().getResource("/Download1920px-Cat_poster_1.jpg").toString());

        assertArrayEquals(expectedImage, downloadedImage);
    }


    @Test
    public void progressUpdating() throws IOException {
        DownloadAgent da = new DownloadAgent();
        List<Integer> expected = Arrays.asList(77, 100);
        List<Integer> actual = new ArrayList<>();
        ProgressBar pb = actual::add;
        da.downloadFile(this.getClass().getResource("/lorem_Ipsum"), pb);
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    private byte[] imageToByteArray(String s) throws IOException {

        return ByteStreams.toByteArray(new URL(s).openStream());

    }


}
