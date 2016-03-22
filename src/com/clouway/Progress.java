package com.clouway;

/**
 * The implementation of this interface will be used to update progress by download
 *
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public interface Progress {
    /**
     * @param progress inspected progress
     */
    void update(int progress);
}
