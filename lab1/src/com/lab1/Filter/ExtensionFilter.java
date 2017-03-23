package com.lab1.Filter;

import java.io.File;

public class ExtensionFilter implements Filter {

    public static final char prefix = '.';
    private String extension;

    public ExtensionFilter(String extension) {
        this.extension = extension;
    }

    @Override
    public int hashCode() {
        return prefix + 37 * extension.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ExtensionFilter && this.hashCode() == obj.hashCode());
    }

    @Override
    public String toString() {
        return prefix + extension;
    }

    @Override
    public boolean check(File file) {
        return getExtension(file).equals(extension);
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
