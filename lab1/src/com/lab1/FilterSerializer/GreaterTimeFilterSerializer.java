package com.lab1.FilterSerializer;

import com.lab1.Filter.Filter;
import com.lab1.Filter.GreaterTimeFilter;
import com.lab1.FilterCreateException;
import com.lab1.FilterSerializeException;

/**
 * Created by Alexander on 08/03/2017.
 */
public class GreaterTimeFilterSerializer implements FilterSerializer {

    public static final char prefix = '>';

    public static char getPrefix() {
        return prefix;
    }

    public Filter readFilter(String s) throws FilterCreateException {
        try {
            return new GreaterTimeFilter(Long.parseLong(s));
        } catch (NumberFormatException e) {
            throw new FilterCreateException("Invalid format for filter used : >" + s);
        }
    }

    @Override
    public String serialize(Filter f) throws FilterSerializeException {
        if (!(f instanceof GreaterTimeFilter)) {
            /* programmers mistake */
            throw new FilterSerializeException("Using incorrect serializer for filter : " + f.toString());
        }
        return prefix + ((GreaterTimeFilter)f).getLastModified().toString();
    }

}
