package com.lab1.Filter;

import java.io.File;

public class AndFilter implements Filter {

    private Filter[] filters;

    public AndFilter(Filter[] filters) {
        this.filters = filters;
    }

    @Override
    public int hashCode() {
        int result = 0;
        for (Filter filter : filters) {
            result = 37 * result + filter.hashCode();
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof AndFilter && this.hashCode() == obj.hashCode());
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
