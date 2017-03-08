package com.lab1.FilterSerializer;

import com.lab1.Filter.ExtensionFilter;
import com.lab1.Filter.Filter;

/**
 * Created by Alexander on 08/03/2017.
 */
public class ExtensionFilterSerializer {

    public static Filter parseFilter(String s) {
        return new ExtensionFilter(s);
    }

}
