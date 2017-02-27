package com.lab1;

import com.lab1.Filter.*;

import java.util.HashMap;
import java.util.Map;

public class FilterFactory {
    private static final Map<Character, String> factoryMap;
    static {
        factoryMap = new HashMap<Character, String>();
        factoryMap.put('.', ExtensionFilter.class.getName());
        factoryMap.put('<', LessTimeFilter.class.getName());
        factoryMap.put('>', GreaterTimeFilter.class.getName());
        factoryMap.put('&', AndFilter.class.getName());
        factoryMap.put('|', OrFilter.class.getName());
        factoryMap.put('~', NotFilter.class.getName());
    }

    public static Filter create (String config) {
        Filter filter;
        char filterType = config.charAt(0);
        String filterParameter = config.substring(1);

        if (!factoryMap.containsKey(filterType)) {
            throw new InvalidFilterException(config);
        }

        try {
            filter = (Filter)Class.forName(factoryMap.get(filterType))
                    .getConstructor(String.class).newInstance(filterParameter);
            return filter;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
