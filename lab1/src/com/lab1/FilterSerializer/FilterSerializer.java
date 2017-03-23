package com.lab1.FilterSerializer;

import com.lab1.Filter.Filter;
import com.lab1.FilterCreateException;

public interface FilterSerializer {
    public Filter readFilter(String s) throws FilterCreateException;
}
