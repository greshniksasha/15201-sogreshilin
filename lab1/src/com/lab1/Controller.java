package com.lab1;


import com.lab1.Filter.Filter;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class Controller {

    private Filter[] filters;
    private Statistics stat;

    public Controller(String configName) {
        filters = FilterParser.parse(configName);
        stat = new Statistics();
    }

    public void collectStatisticsOf(File dir) {
        File[] files = dir.listFiles();
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

    public void printStatistics() {
        System.out.println("Total : " + stat.getLineCount() + " lines in " + stat.getFileCount() + " files");
        System.out.println("------------------------------------------------------------");
        Map<Filter, Statistics.Record> sortedStat = stat.sortByValue();
        for (Map.Entry<Filter, Statistics.Record> entry : sortedStat.entrySet()) {
            System.out.println(entry.getKey().toString() + " : " + entry.getValue().getLineCount() +
                    " lines in " + entry.getValue().getFileCount() + " files");
        }
    }

    public Statistics getStatistics() {
        return stat;
    }

    public Filter[] getFilters() {
        return filters;
    }

}
