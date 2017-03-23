package com.lab1;

public class FilterCreateException extends Exception {
    public FilterCreateException(String message) {
        super(message);
    }

    FilterCreateException(Throwable thrown) {
        super(thrown);
    }
}
