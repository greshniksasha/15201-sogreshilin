package com.lab1;

import com.lab1.Filter.*;
import com.lab1.FilterSerializer.*;


import javax.sound.midi.Soundbank;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class FilterFactory {
    private static final Map<Character, Class> factoryMap;
    static {
        factoryMap = new HashMap<Character, Class>();
        factoryMap.put(ExtensionFilter.prefix, ExtensionFilterSerializer.class);
        factoryMap.put(LessTimeFilter.prefix, LessTimeFilterSerializer.class);
        factoryMap.put(GreaterTimeFilter.prefix, GreaterTimeFilterSerializer.class);
        factoryMap.put(AndFilter.prefix, AndFilterSerializer.class);
        factoryMap.put(OrFilter.prefix, OrFilterSerializer.class);
        factoryMap.put(NotFilter.prefix, NotFilterSerializer.class);
    }

    public static Filter create (String config) throws FilterCreateException {
        char prefix = config.charAt(0);
        String filterString = config.substring(1);
        if (!factoryMap.containsKey(prefix)) {
            throw new FilterCreateException("Invalid prefix : " + prefix);
        }
        try {
            FilterSerializer serializer = (FilterSerializer)factoryMap.get(prefix).newInstance();
            return serializer.readFilter(filterString);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new FilterCreateException(e);
        }
    }
}
