package com.lab1.FilterSerializer;

import com.lab1.Filter.Filter;
import com.lab1.FilterCreateException;
import com.lab1.FilterNullPointerException;
import com.lab1.FilterSerializeException;

public interface FilterSerializer {
    public Filter readFilter(String s) throws FilterCreateException, FilterNullPointerException;
    public String serialize(Filter f) throws FilterSerializeException, FilterNullPointerException;
}
