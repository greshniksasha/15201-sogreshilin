package com.lab1;

import com.lab1.Filter.Filter;
import com.lab1.FilterSerializer.*;


import javax.sound.midi.Soundbank;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class FilterFactory {
    private static final Map<Character, String> factoryMap;
    static {
        factoryMap = new HashMap<Character, String>();
        factoryMap.put('.', ExtensionFilterSerializer.class.getName());
        factoryMap.put('<', LessTimeFilterSerializer.class.getName());
        factoryMap.put('>', GreaterTimeFilterSerializer.class.getName());
        factoryMap.put('&', AndFilterSerializer.class.getName());
        factoryMap.put('|', OrFilterSerializer.class.getName());
        factoryMap.put('!', NotFilterSerializer.class.getName());
    }

    public static Filter create (String config) {
        char filterType = config.charAt(0);
        String filterParameter = config.substring(1);
        if (!factoryMap.containsKey(filterType)) {
            System.err.println("Invalid symbol used in configuration file : " + config);
            System.exit(1);
        }
        try {
            Class c = Class.forName(factoryMap.get(filterType));
            Method m = c.getMethod("parseFilter", String.class);
            return (Filter)m.invoke(null, filterParameter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
