package com.clouway.download.agent;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public interface ProgressBar {

    void updateProgress(long progressSize);

    void fullSize(long contentLength);

}
