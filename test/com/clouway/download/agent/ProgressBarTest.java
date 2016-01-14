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
        ProgressBarImplementation progressBar = new ProgressBarImplementation();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PrintStream newOut = new PrintStream(baos);
        PrintStream oldOut = System.out;

        System.setOut(newOut);
        progressBar.updateProgress(100);
        String output = baos.toString();

        System.out.flush();
        System.setOut(oldOut);

        assertThat(output, is("100%\n"));
    }


//    example of callLog pattern
//    interface Job {
//        void apply();
//    }
//
//
//    @Test
//    public void ad() {
//        StringBuilder callLog = new StringBuilder();
//        Job fakeJob = () -> callLog.append("a");
//
//        List<Integer> progressUpdates = Arrays.asList(4, 8, 20, 70, 90, 100);
//
//        testMethod(fakeJob);
//
//        assertThat(callLog.toString(), is(equalTo("a")));
//    }
//
//
//    private void testMethod(Job job) {
//        job.apply();
//    }
}
