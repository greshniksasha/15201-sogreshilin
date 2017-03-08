package com.lab1;

public class InvalidFilterException extends RuntimeException {
    private String invalidConfig;

    public InvalidFilterException(String config) {
        invalidConfig = config;
    }

    @Override
    public void printStackTrace() {
        System.out.println("Invalid symbol used in configuration file in line : " + invalidConfig);
    }
}
