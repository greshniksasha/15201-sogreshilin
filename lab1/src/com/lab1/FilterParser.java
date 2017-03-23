package com.lab1;

import com.lab1.Filter.Filter;

import java.io.FileNotFoundException;
import java.util.*;

public class FilterParser {

    public static Filter[] parse(String fileName) throws FilterCreateException, FileNotFoundException {
        List<Filter> filtersList = new ArrayList<Filter>();

        for (ConfigIterator it = new ConfigIterator(fileName); it.hasNext(); ) {
            Filter filter = FilterFactory.create(it.next());
            if (!filtersList.contains(filter)) {
                filtersList.add(filter);
            }
        }

        return filtersList.toArray(new Filter[filtersList.size()]);
    }

}
