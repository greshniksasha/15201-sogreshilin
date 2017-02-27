package com.lab1.Filter;

import com.lab1.FilterFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class AgregateFilter implements Filter {

    private String[] splitConfigs(String configsString) {
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

    protected Filter[] parse(String filterConfigs) {
        FilterFactory factory = new FilterFactory();
        List<Filter> filtersList = new ArrayList<Filter>();
        String[] configs = splitConfigs(filterConfigs.substring(1, filterConfigs.length() - 1));
        for (String config : configs) {
            filtersList.add(factory.create(config));
        }
        return filtersList.toArray(new Filter[filtersList.size()]);
    }

}
