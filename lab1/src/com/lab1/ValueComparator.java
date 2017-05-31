package com.lab1;

import com.lab1.Filter.Filter;

import java.util.Comparator;
import java.util.HashMap;

public class ValueComparator implements Comparator<Filter> {

    private HashMap<Filter, Statistics.Record> map;

    public ValueComparator(HashMap<Filter, Statistics.Record> m) {
        map = new HashMap<Filter, Statistics.Record>();
        map.putAll(m);
    }

    @Override
    public int compare(Filter o1, Filter o2) {
        if (map.get(o1).getLineCount() >= map.get(o2).getLineCount()) {
            return -1;
        } else {
            return 1;
        }
    }
}
