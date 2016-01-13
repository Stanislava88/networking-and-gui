package com.clouway.download.agent;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class ProgressBar {


    private boolean undeterminedSize = false;
    private int size;
    private int progress;

    public ProgressBar(int size) {
        this.size = size;
        if (size <= 0) {
            System.out.println("Undetermined size");
            undeterminedSize = true;
        }
    }

    public void addProgress(int progressSize) {
        progress += progressSize;
        if (!undeterminedSize) {
            printProgress();
        }

    }

    private void printProgress() {
        int onePercent = size / 100;
        int currentPercent = progress / onePercent;
        System.out.println(currentPercent + "%");
    }
}
