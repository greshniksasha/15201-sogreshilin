package com.lab1.FilterSerializer;

import com.lab1.Filter.Filter;
import com.lab1.Filter.NotFilter;
import com.lab1.FilterCreateException;
import com.lab1.FilterFactory;

/**
 * Created by Alexander on 08/03/2017.
 */
public class NotFilterSerializer implements FilterSerializer {

    public Filter readFilter(String s) throws FilterCreateException {
        Filter filter = null;
        if (s.charAt(0) == '(') {
            if (s.charAt(s.length() - 1) == ')') {
                filter = FilterFactory.create(s.substring(1, s.length() - 1));
            } else {
                throw new FilterCreateException("Could not parse the substring : !" + s);
            }
        } else {
            filter = FilterFactory.create(s);
        }
        return new NotFilter(filter);
    }

}
