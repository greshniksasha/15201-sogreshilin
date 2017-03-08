package com.lab1;

import java.io.*;
import java.util.Iterator;

public class ConfigIterator implements Iterator<String> {

    private BufferedReader configReader;
    private String lastReadLine;

    public ConfigIterator(String configFileName) {
        FileReader configFile = null;
        try {
            configFile = new FileReader(configFileName);
        } catch (FileNotFoundException e) {
            throw new NoSuchFile(configFileName);
        }
        configReader = new BufferedReader(configFile);
    }

    @Override
    public boolean hasNext() {
        try {
            lastReadLine = configReader.readLine();
            if (lastReadLine != null) {
                lastReadLine = lastReadLine.replaceAll("^\\s+", "").replaceAll("\\s+$", "").replaceAll("\\s+", " ");
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
