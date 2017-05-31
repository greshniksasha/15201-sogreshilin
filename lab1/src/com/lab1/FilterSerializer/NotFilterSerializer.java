package com.lab1.FilterSerializer;

import com.lab1.Factory;
import com.lab1.Filter.Filter;
import com.lab1.Filter.NotFilter;
import com.lab1.FilterCreateException;
import com.lab1.FilterNullPointerException;
import com.lab1.FilterSerializeException;

/**
 * Created by Alexander on 08/03/2017.
 */
public class NotFilterSerializer implements FilterSerializer {

    public static final char prefix = '!';

    public static char getPrefix() {
        return prefix;
    }

    public Filter readFilter(String s) throws FilterCreateException, FilterNullPointerException {
        Filter filter = null;
        if (s.charAt(0) == '(') {
            if (s.charAt(s.length() - 1) == ')') {
                filter = Factory.create(s.substring(1, s.length() - 1));
            } else {
                throw new FilterCreateException("Could not parse the substring : !" + s);
            }
        } else {
            filter = Factory.create(s);
        }
        return new NotFilter(filter);
    }

    @Override
    public String serialize(Filter f) throws FilterSerializeException, FilterNullPointerException {
        if (!(f instanceof NotFilter)) {
            /* programmers mistake */
            throw new FilterSerializeException("Using incorrect serializer for filter : " + f.toString());
        }
        Filter innerFilter = ((NotFilter)f).getFilter();
        return prefix + "(" + Factory.create(innerFilter).serialize(innerFilter) + ")";
    }

}
