package com.clouway.clientserver;

import java.util.Date;

/**
 * The implementation of this interface will be used to get time
 *
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public interface Clock {
    /**
     * Will return Date
     *
     * @return current time
     */
    Date getTime();
}
