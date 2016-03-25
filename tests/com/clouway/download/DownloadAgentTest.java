package com.clouway.download;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class DownloadAgentTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private TemporaryFolder folder = new TemporaryFolder();

    private RandomAccessFile file;
    private File source;
    private String url;
    private String destination;

    private Progress progress = context.mock(Progress.class);
    private DownloadAgent agent;

    @Before
    public void setUp() throws IOException {
        folder.create();
    }

    @After
    public void tearDown() throws Exception {
        source.delete();
    }

    @Test
    public void happyPath() throws Exception {
        file = new RandomAccessFile("sourceFile.txt", "rw");
        source = new File("sourceFile.txt");
        destination = folder.newFile("destinationFile.txt").toString();

        url = source.toURI().toURL().toString();

        file.setLength(4096);
        agent = new DownloadAgent(url, destination, progress);

        context.checking(new Expectations() {{
            allowing(progress).update(25);
            allowing(progress).update(50);
            allowing(progress).update(75);
            allowing(progress).update(100);
        }});

        int expected = (int) source.length();
        int actual = agent.download();

        assertThat(actual, is(expected));
    }

    @Test(expected = FileNotFoundException.class)
    public void downloadUnknownFile() throws Exception {
        source = new File("text_file.txt");
        url = source.toURI().toURL().toString();

        agent = new DownloadAgent(url, destination, progress);

        agent.download();
    }

    @Test
    public void downloadUnevenSizeFile() throws Exception {
        file = new RandomAccessFile("sourceFile.txt", "rw");
        source = new File("sourceFile.txt");
        destination = folder.newFile("destinationFile.txt").toString();

        url = source.toURI().toURL().toString();

        file.setLength(7777);

        DownloadAgent agent = new DownloadAgent(url, destination, progress);

        context.checking(new Expectations() {{
            atLeast(5).of(progress).update(with(any(Integer.class)));
        }});

        int expected = (int) source.length();
        int actual = agent.download();

        assertThat(actual, is(expected));
    }
}
