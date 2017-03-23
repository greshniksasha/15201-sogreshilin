package com.lab1.FilterSerializer;

import com.lab1.Filter.AndFilter;
import com.lab1.Filter.Filter;
import com.lab1.FilterCreateException;

/**
 * Created by Alexander on 08/03/2017.
 */
public class AndFilterSerializer extends AgregateFilterSerializer implements FilterSerializer {

    public Filter readFilter(String s) throws FilterCreateException {
        if (s.charAt(0) == '(' && s.charAt(s.length() - 1) == ')') {
            return new AndFilter(AgregateFilterSerializer.parse(s));
        } else {
            throw new FilterCreateException("Could not parse the substring : &" + s);
        }
    }

}
