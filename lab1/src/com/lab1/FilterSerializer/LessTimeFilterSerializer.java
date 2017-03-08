package com.lab1.FilterSerializer;

import com.lab1.Filter.Filter;
import com.lab1.Filter.LessTimeFilter;

/**
 * Created by Alexander on 08/03/2017.
 */
public class LessTimeFilterSerializer {

    public static Filter parseFilter(String s) {
        return new LessTimeFilter(Long.parseLong(s));
    }

}
