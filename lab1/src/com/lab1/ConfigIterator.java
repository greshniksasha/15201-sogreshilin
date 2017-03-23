package com.lab1;

import java.io.*;
import java.util.Iterator;

public class ConfigIterator implements Iterator<String> {

    private BufferedReader configReader;
    private String lastReadLine;

    public ConfigIterator(String configFileName) throws FileNotFoundException {
        FileReader configFile = null;
        configFile = new FileReader(configFileName);
        configReader = new BufferedReader(configFile);
    }

    @Override
    public boolean hasNext() {
        try {
            lastReadLine = configReader.readLine();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        if (lastReadLine != null) {
            lastReadLine = lastReadLine
                    .replaceAll("^\\s+|\\s+$", "")
                    .replaceAll("\\s+", " ")
                    .replaceAll(" {0,}\\( ", "(")
                    .replaceAll(" \\)", ")");
        }
        return lastReadLine != null;
    }

    @Override
    public String next() {
        return lastReadLine;
    }

}
