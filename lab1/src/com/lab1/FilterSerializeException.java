package com.lab1;

/**
 * Created by Alexander on 31/03/2017.
 */
public class FilterSerializeException extends Exception {
    public FilterSerializeException(String message) {
        super(message);
    }

    FilterSerializeException(Throwable thrown) {
        super(thrown);
    }
}
