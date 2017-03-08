package com.lab1.FilterSerializer;

import com.lab1.Filter.Filter;
import com.lab1.Filter.OrFilter;

/**
 * Created by Alexander on 08/03/2017.
 */
public class OrFilterSerializer extends AgregateFilterSerializer {

    public static Filter parseFilter(String s) {
        return new OrFilter(parse(s));
    }

}
