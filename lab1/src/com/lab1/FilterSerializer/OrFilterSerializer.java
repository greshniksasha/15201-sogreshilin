package com.lab1.FilterSerializer;

import com.lab1.Filter.Filter;
import com.lab1.Filter.OrFilter;
import com.lab1.FilterCreateException;
import com.lab1.FilterNullPointerException;
import com.lab1.FilterSerializeException;

/**
 * Created by Alexander on 08/03/2017.
 */
public class OrFilterSerializer extends AgregateFilterSerializer implements FilterSerializer {

    public static final char prefix = '|';

    public static char getPrefix() {
        return prefix;
    }

    public Filter readFilter(String s) throws FilterCreateException, FilterNullPointerException {
        if (s.charAt(0) == '(' && s.charAt(s.length() - 1) == ')') {
            return new OrFilter(AgregateFilterSerializer.parse(s));
        } else {
            throw new FilterCreateException("Could not parse the substring : |" + s);
        }
    }

    @Override
    public String serialize(Filter f) throws FilterSerializeException {
        if (!(f instanceof OrFilter)) {
            /* programmers mistake */
            throw new FilterSerializeException("Using incorrect serializer for filter : " + f.toString());
        }
        String res = prefix + "(";
        for (Filter filter : ((OrFilter)f).getFilters()) {
            res += (filter.toString() + " ");
        }
        return res.substring(0, res.length() - 1) + ")";
    }

}
