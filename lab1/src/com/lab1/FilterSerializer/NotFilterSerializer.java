package com.lab1.FilterSerializer;

import com.lab1.Filter.Filter;
import com.lab1.Filter.NotFilter;
import com.lab1.FilterFactory;

/**
 * Created by Alexander on 08/03/2017.
 */
public class NotFilterSerializer {

    public static Filter parseFilter(String s) {
        Filter filter = null;
        if (s.charAt(0) == '(') {
            filter = FilterFactory.create(s.substring(1, s.length() - 1));
        } else {
            filter = FilterFactory.create(s);
        }
        return new NotFilter(filter);
    }

}
