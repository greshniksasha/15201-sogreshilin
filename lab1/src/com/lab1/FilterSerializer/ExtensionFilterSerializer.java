package com.lab1.FilterSerializer;

import com.lab1.Filter.ExtensionFilter;
import com.lab1.Filter.Filter;
import com.lab1.FilterCreateException;
import com.lab1.FilterSerializeException;

/**
 * Created by Alexander on 08/03/2017.
 */
public class ExtensionFilterSerializer implements FilterSerializer {

    public static final char prefix = '.';

    public static char getPrefix() {
        return prefix;
    }

    public Filter readFilter(String s) throws FilterCreateException {
        if (s.lastIndexOf(' ') != -1) {
            throw new FilterCreateException("Could not parse the substring : ." + s);
        }
        return new ExtensionFilter(s);
    }

    @Override
    public String serialize(Filter f) throws FilterSerializeException {
        if (!(f instanceof ExtensionFilter)) {
            /* programmers mistake */
            throw new FilterSerializeException("Using incorrect serializer for filter : " + f.toString());
        }
        return prefix + ((ExtensionFilter)f).getExtension();
    }

}
