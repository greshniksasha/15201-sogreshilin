package com.lab1.Filter;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LessTimeFilter implements Filter {

    public static final char prefix = '<';
    private Long lastModified;

    public LessTimeFilter(long lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public int hashCode() {
        return prefix + 37 * lastModified.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof GreaterTimeFilter && this.hashCode() == obj.hashCode());
    }

    @Override
    public String toString() {
        return prefix + lastModified.toString();
    }

    @Override
    public boolean check(File file) {
        return file.lastModified() < lastModified;
    }
}
