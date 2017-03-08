package com.lab1;

/**
 * Created by Alexander on 08/03/2017.
 */
public class NoSuchDirectory extends RuntimeException {
    private String dirPath;

    public NoSuchDirectory(String dirPath) {
        this.dirPath = dirPath;
    }

    @Override
    public void printStackTrace() {
        System.out.println("No such directory : " + dirPath);
    }
}
