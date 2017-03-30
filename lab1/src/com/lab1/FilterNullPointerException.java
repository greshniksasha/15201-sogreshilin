package com.lab1;

/**
 * Created by Alexander on 31/03/2017.
 */
public class FilterNullPointerException extends Exception {
    public FilterNullPointerException(String message) {
        super(message);
    }

    FilterNullPointerException(Throwable thrown) {
        super(thrown);
    }
}
