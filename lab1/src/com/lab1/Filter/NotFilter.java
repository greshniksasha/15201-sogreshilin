package com.lab1.Filter;

import com.lab1.FilterNullPointerException;

import java.io.File;

public class NotFilter implements Filter {

    private static final char prefix = '!';
    private Filter filter;

    public NotFilter(Filter filter) throws FilterNullPointerException {
        if (filter == null) {
            throw new FilterNullPointerException("Cannot make NotFilter from null");
        }
        this.filter = filter;
    }

    public Filter getFilter() {
        return filter;
    }

    @Override
    public int hashCode() {
        return prefix + 37 * filter.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof NotFilter && this.hashCode() == obj.hashCode());
    }

    @Override
    public String toString() {
        return prefix + "(" + filter.toString() + ")";
    }

    @Override
    public boolean check(File file) {
        return !filter.check(file);
    }

}
