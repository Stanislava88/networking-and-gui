package com.clouway.multiserver;

/**
 * @author Stanislava Kaukova(sisiivanovva@gmail.com)
 */
public class ClosedSocketException extends RuntimeException{
    public ClosedSocketException(String msg) {
        super(msg);
    }
}
