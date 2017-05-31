package com.lab1.Filter;

import com.lab1.FilterNullPointerException;

import java.io.File;

public class OrFilter implements Filter {

    private static final char prefix = '|';
    private final Filter[] filters;

    public OrFilter(Filter[] filters) throws FilterNullPointerException {
        if (filters == null) {
            throw new FilterNullPointerException("Cannot make OrFilter from null");
        }
        this.filters = filters;
    }

    public Filter[] getFilters() {
        return filters;
    }

    @Override
    public int hashCode() {
        int result = prefix;
        for (Filter filter : filters) {
            result = 37 * result + filter.hashCode();
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof OrFilter && this.hashCode() == obj.hashCode());
    }

    @Override
    public String toString() {
        String res = prefix + "(";
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
