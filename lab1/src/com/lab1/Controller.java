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

    public Statistics getStatistics() {
        return stat;
    }

    public Filter[] getFilters() {
        return filters;
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

}
