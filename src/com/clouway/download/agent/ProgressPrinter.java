package com.clouway.download.agent;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class ProgressPrinter implements ProgressBar {


    public void updateProgress(int progressPercent) {
        System.out.println(progressPercent + "%");
    }


}
