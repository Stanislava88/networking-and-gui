package com.clouway.download.agent;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class ProgressBarImplementation implements ProgressBar {


    private boolean undeterminedSize = true;
    private long size;
    private long progress;


    public void updateProgress(long progressSize) {
        progress += progressSize;
        if (!undeterminedSize) {
            printProgress();
        }

    }


    public void fullSize(long contentLength) {
        if (contentLength > 0) {
            this.size = contentLength;
            undeterminedSize = false;
        } else {
            System.out.println("Undetermined size");
        }
    }

    private void printProgress() {
        System.out.println(progress() + "%");
    }

    private long progress() {
        long onePercent = size / 100;
        return progress / onePercent;
    }
}
