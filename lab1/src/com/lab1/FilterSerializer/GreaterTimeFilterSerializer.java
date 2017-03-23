package com.lab1.FilterSerializer;

import com.lab1.Filter.Filter;
import com.lab1.Filter.GreaterTimeFilter;

/**
 * Created by Alexander on 08/03/2017.
 */
public class GreaterTimeFilterSerializer implements FilterSerializer {

    public Filter readFilter(String s) {
        return new GreaterTimeFilter(Long.parseLong(s));
    }

}
