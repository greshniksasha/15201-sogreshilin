package com.lab1;

/**
 * Created by Alexander on 08/03/2017.
 */
public class NoSuchFile extends RuntimeException {
    private String filePath;

    public NoSuchFile(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void printStackTrace() {
        System.out.println("Invalid path to configuration file : " + filePath);
    }
}
