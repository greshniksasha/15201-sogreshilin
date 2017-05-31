package com.lab1;


import com.lab1.Filter.Filter;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.NoSuchFileException;
import java.util.LinkedHashMap;
import java.util.Map;

public class Controller {

    private Filter[] filters;
    private Statistics stat;

    public Controller(String configName) throws FilterCreateException, FileNotFoundException, FilterNullPointerException {
        if (configName == null) {
            throw new FilterNullPointerException("Empty configuration file name");
        }
        filters = FilterParser.parse(configName);
        stat = new Statistics();
    }

    public Statistics getStatistics() {
        return stat;
    }

    public Filter[] getFilters() {
        return filters;
    }

    public void collectStatisticsOf(File dir) throws NoSuchFileException, CountLinesException {
        File[] files = dir.listFiles();
        if (files == null) {
            throw new NoSuchFileException("No such directory : " + dir.getAbsolutePath());
        }
        for (File file : files) {
            if (file.isDirectory()) {
                collectStatisticsOf(file);
            } else {
                for (Filter filter : filters) {
                    if (filter.check(file)) {
                        stat.add(filter, file);
                    }
                }
            }
        }
    }

}
