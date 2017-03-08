package com.lab1.FilterSerializer;

import com.lab1.Filter.AndFilter;
import com.lab1.Filter.Filter;

/**
 * Created by Alexander on 08/03/2017.
 */
public class AndFilterSerializer extends AgregateFilterSerializer {

    public static Filter parseFilter(String s) {
        return new AndFilter(parse(s));
    }

}
