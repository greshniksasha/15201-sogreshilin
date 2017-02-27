package com.lab1.Filter;

import com.lab1.FilterFactory;

import java.io.File;

public class NotFilter implements Filter {

    private Filter filter;

    public NotFilter(String filterConfig) {
        FilterFactory factory = new FilterFactory();
        if (filterConfig.charAt(0) == '(') {
            filter = factory.create(filterConfig.substring(1, filterConfig.length() - 1));
        } else {
            filter = factory.create(filterConfig);
        }
    }

    @Override
    public String toString() {
        return "~(" + filter.toString() + ")";
    }

    @Override
    public boolean check(File file) {
        return !filter.check(file);
    }

}
