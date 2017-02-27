package com.lab1.Filter;

import java.io.File;
import java.text.SimpleDateFormat;

public abstract class TimeFilter implements Filter {
    public static final SimpleDateFormat dateFormatter;
    static {
        dateFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    }
}
