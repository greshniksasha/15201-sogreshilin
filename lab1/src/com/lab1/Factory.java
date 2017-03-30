package com.lab1;

import com.lab1.Filter.*;
import com.lab1.FilterSerializer.*;


import java.util.HashMap;
import java.util.Map;

public class Factory {

    private static final FilterSerializer[] serializers = {
            new ExtensionFilterSerializer(),
            new LessTimeFilterSerializer(),
            new GreaterTimeFilterSerializer(),
            new AndFilterSerializer(),
            new OrFilterSerializer(),
            new NotFilterSerializer()
    };

    private static final Map<Character, FilterSerializer> prefixToSerializerMap;
    static {
        prefixToSerializerMap = new HashMap<Character, FilterSerializer>();
        prefixToSerializerMap.put(ExtensionFilterSerializer.getPrefix(), serializers[0]);
        prefixToSerializerMap.put(LessTimeFilterSerializer.getPrefix(), serializers[1]);
        prefixToSerializerMap.put(GreaterTimeFilterSerializer.getPrefix(), serializers[2]);
        prefixToSerializerMap.put(AndFilterSerializer.getPrefix(), serializers[3]);
        prefixToSerializerMap.put(OrFilterSerializer.getPrefix(), serializers[4]);
        prefixToSerializerMap.put(NotFilterSerializer.getPrefix(), serializers[5]);
    }

    private static Map<Class, FilterSerializer> filterToSerializerMap;
    static {
        filterToSerializerMap = new HashMap<>();
        filterToSerializerMap.put(ExtensionFilter.class, serializers[0]);
        filterToSerializerMap.put(LessTimeFilter.class, serializers[1]);
        filterToSerializerMap.put(GreaterTimeFilter.class, serializers[2]);
        filterToSerializerMap.put(AndFilter.class, serializers[3]);
        filterToSerializerMap.put(OrFilter.class, serializers[4]);
        filterToSerializerMap.put(NotFilter.class, serializers[5]);
    }

    public static Filter create (String config) throws FilterCreateException, FilterNullPointerException {
        if (config.isEmpty()) {
            throw new FilterNullPointerException("Attemp to create filter from empty string");
        }
        char prefix = config.charAt(0);
        String filterString = config.substring(1);
        if (!prefixToSerializerMap.containsKey(prefix)) {
            throw new FilterCreateException("Invalid prefix : " + prefix);
        }
        FilterSerializer serializer = prefixToSerializerMap.get(prefix);
        return serializer.readFilter(filterString);
    }

    public static FilterSerializer create (Filter filter) throws FilterSerializeException, FilterNullPointerException {
        if (filter == null) {
            throw new FilterNullPointerException("Attempt to create filter serializer from null pointer");
        }

        if (filterToSerializerMap.containsKey(filter.getClass())) {
            return filterToSerializerMap.get(filter.getClass());
        } else {
            /* programmers mistake */
            throw new FilterSerializeException("Serializer for this filter does not exist : " + filter.toString());
        }
    }

}
