package com.clouway.download.agent;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class ProgressBarTest {

    @Test
    public void successfulPrinting() {
        ProgressBar progressBar = new ProgressBar(39990);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PrintStream newOut = new PrintStream(baos);
        PrintStream oldOut = System.out;

        System.setOut(newOut);
        progressBar.addProgress(39990);
        String output = baos.toString();

        System.out.flush();
        System.setOut(oldOut);

        assertThat(output, is("100%\n"));
    }

    @Test
    public void illegalSize() {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PrintStream newOut = new PrintStream(baos);
        PrintStream oldOut = System.out;

        System.setOut(newOut);
        ProgressBar progressBar = new ProgressBar(-1);
        progressBar.addProgress(39990);
        String output = baos.toString();

        System.out.flush();
        System.setOut(oldOut);


        assertThat(output, is("Undetermined size\n"));
    }
}
