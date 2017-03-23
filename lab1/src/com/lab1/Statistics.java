package com.lab1;

import com.lab1.Filter.Filter;

import java.io.*;
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

    public void add(Filter filter, File file) throws CountLinesException {
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

    public int countLinesIn(File file) throws CountLinesException {
        int linesCount = 0;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            while (reader.readLine() != null) {
              linesCount++;
            }
            reader.close();
        } catch (IOException e) {
            throw new CountLinesException("Could not open the file : " + file.getAbsolutePath());
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
