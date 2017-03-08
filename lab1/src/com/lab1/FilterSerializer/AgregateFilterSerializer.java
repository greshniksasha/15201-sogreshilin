package com.lab1.FilterSerializer;

import com.lab1.Filter.Filter;
import com.lab1.FilterFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 08/03/2017.
 */
public class AgregateFilterSerializer {

    private static String[] splitConfigs(String configsString) {
        List<String> configs = new ArrayList<String>();
        configsString += ' ';
        String config = "";
        int inEscSeq = 0;
        for (int i = 0; i < configsString.length(); ++i) {
            char c = configsString.charAt(i);
            if (c == '(') {
                ++inEscSeq;
            } else if (c == ')') {
                --inEscSeq;
            } else if (c == ' ' && inEscSeq == 0) {
                configs.add(config);
                config = "";
                continue;
            }
            config += c;
        }
        return configs.toArray(new String[configs.size()]);
    }

    protected static Filter[] parse(String s) {
        List<Filter> filtersList = new ArrayList<Filter>();
        String[] configs = splitConfigs(s.substring(1, s.length() - 1));
        for (String config : configs) {
            filtersList.add(FilterFactory.create(config));
        }
        return filtersList.toArray(new Filter[filtersList.size()]);
    }

}
