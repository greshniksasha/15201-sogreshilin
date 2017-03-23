package com.lab1.FilterSerializer;

import com.lab1.Filter.ExtensionFilter;
import com.lab1.Filter.Filter;
import com.lab1.FilterCreateException;

/**
 * Created by Alexander on 08/03/2017.
 */
public class ExtensionFilterSerializer implements FilterSerializer {

    public Filter readFilter(String s) throws FilterCreateException {
        if (s.lastIndexOf(' ') != -1) {
            throw new FilterCreateException("Could not parse the substring : ." + s);
        }
        return new ExtensionFilter(s);
    }

}
