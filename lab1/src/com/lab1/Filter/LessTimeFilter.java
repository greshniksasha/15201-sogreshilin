package com.lab1.Filter;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LessTimeFilter extends TimeFilter {
    private Date lastModified;

    public LessTimeFilter(String timeString) {
        try {
            lastModified = dateFormatter.parse(timeString.substring(1, timeString.length() - 1));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "<(" + dateFormatter.format(lastModified) + ")";
    }

    @Override
    public boolean check(File file) {
        Date date = new Date(file.lastModified());
        return date.before(lastModified);
    }
}