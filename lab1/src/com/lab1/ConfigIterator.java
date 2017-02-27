package com.lab1;

import java.io.*;
import java.util.Iterator;

public class ConfigIterator implements Iterator<String> {

    private BufferedReader configReader;
    private String lastReadLine;

    public ConfigIterator(String configFileName) {
        try {
            configReader = new BufferedReader(new FileReader(configFileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasNext() {
        try {
            lastReadLine = configReader.readLine();
            if (lastReadLine != null) {
                lastReadLine = lastReadLine.trim();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lastReadLine != null;
    }

    @Override
    public String next() {
        return lastReadLine;
    }

}
