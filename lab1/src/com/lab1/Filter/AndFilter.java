package com.lab1.Filter;

import java.io.File;

public class AndFilter extends AgregateFilter {

    private Filter[] filters;

    public AndFilter(String filterConfigs) {
        filters = parse(filterConfigs);
    }

    @Override
    public String toString() {
        String res = "&(";
        for (Filter filter : filters) {
            res += (filter.toString() + " ");
        }
        return res.substring(0, res.length() - 1) + ")";
    }

    @Override
    public boolean check(File file) {
        for (Filter filter : filters) {
            if (!filter.check(file)) {
                return false;
            }
        }
        return true;
    }
}
