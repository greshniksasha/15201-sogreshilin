package com.lab1;

import com.lab1.Filter.Filter;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class InvalidFilterExceptionTest {

    @Test (expected = InvalidFilterException.class)
    public void printStackTrace() throws Exception {
        FilterFactory factory = new FilterFactory();
        System.out.println("TEST InvalidFilter.printStackTrace() PASSED");
        Filter filter = factory.create("*(.cpp .hpp)");
    }

}
