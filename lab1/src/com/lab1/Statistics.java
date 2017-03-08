package com.lab1;

import com.lab1.Filter.Filter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Statistics {

    public class Record {
        private int fileCount;
        private int lineCount;
        public Record() {
            fileCount = 0;
            lineCount = 0;
        }
        public void incFileCount() { ++fileCount; }
        public void incLineCountBy(int k) { lineCount += k; }
        public int getLineCount() { return lineCount; }
        public int getFileCount() { return fileCount; }
    }

    private HashMap<Filter, Record> stat;
    private List<File> checkedFiles;
    private int filesInGeneral;
    private int linesInGeneral;

    public Statistics() {
        stat = new HashMap<Filter, Record>();
        checkedFiles = new ArrayList<File>();
        filesInGeneral = 0;
        linesInGeneral = 0;
    }

    public void add(Filter filter, File file) {
        if (!checkedFiles.contains(file)) {
            checkedFiles.add(file);
            ++filesInGeneral;
            linesInGeneral += countLinesIn(file);
        }
        if (!stat.containsKey(filter)) {
            stat.put(filter, new Record());
        }
        stat.get(filter).incFileCount();
        stat.get(filter).incLineCountBy(countLinesIn(file));
    }

    public int getLineCount(Filter filter) {
        return (stat.containsKey(filter)) ? stat.get(filter).getLineCount() : 0;
    }

    public int getFileCount(Filter filter) {
        return (stat.containsKey(filter)) ? stat.get(filter).getFileCount() : 0;
    }

    public int getLineCount() { return linesInGeneral; }

    public int getFileCount() { return filesInGeneral; }

    public int countLinesIn(File file) {
        int linesCount = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (reader.readLine() != null) {
                linesCount++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return linesCount;
    }

    public TreeMap<Filter, Record> sortByValue() {
        Comparator<Filter> comparator = new ValueComparator(stat);
        TreeMap<Filter, Record> result = new TreeMap<Filter, Record>(comparator);

        result.putAll(stat);
        return result;
    }

}
