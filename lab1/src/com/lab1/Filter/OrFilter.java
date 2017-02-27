package com.lab1.Filter;

import com.lab1.FilterFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OrFilter extends AgregateFilter {

    private Filter[] filters;

    public OrFilter(String filterConfigs) {
        filters = parse(filterConfigs);
    }

    @Override
    public String toString() {
        String res = "|(";
        for (Filter filter : filters) {
            res += (filter.toString() + " ");
        }
        return res.substring(0, res.length() - 1) + ")";
    }

    @Override
    public boolean check(File file) {
        for (Filter filter : filters) {
            if (filter.check(file)) {
                return true;
            }
        }
        return false;
    }

}
