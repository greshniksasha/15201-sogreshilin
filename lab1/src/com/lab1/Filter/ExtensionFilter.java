package com.lab1.Filter;

import java.io.File;

public class ExtensionFilter implements Filter {
    private String filterExtension;

    public ExtensionFilter(String extension) {
        filterExtension = extension;
    }

    @Override
    public String toString() {
        return "." + filterExtension;
    }

    @Override
    public boolean check(File file) {
        return getExtension(file).equals(filterExtension);
    }

    private String getExtension(File file) {
        String extention = "";
        String fileName = file.getAbsolutePath();
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            extention = fileName.substring(lastDotIndex + 1);
        }
        return extention;
    }
}
