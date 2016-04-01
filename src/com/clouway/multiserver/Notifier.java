package com.clouway.multiserver;

/**
 * The implementation of this interface will be used for notification
 *
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public interface Notifier {
    /**
     *
     * @param message notification message
     */
    void notify(String message);
}
