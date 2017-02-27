package com.lab1;

import com.lab1.Filter.*;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Alexander on 26/02/2017.
 */
public class FilterParserTest {
    String fileName = "test/com/lab1/FilterParserTest.txt";

    @Test
    public void parse() throws Exception {
        Filter[] expectedFilters = {
                new ExtensionFilter("cpp"),
                new NotFilter(".hpp"),
                new LessTimeFilter("(20.01.1997 00:00:00)"),
                new AndFilter("(~.hpp <(20.01.1997 00:00:00))"),
                new OrFilter("(.cpp .cc .hpp .h ~<(20.01.1997 00:00:00))")
        };

        Filter[] filters = FilterParser.parse(fileName);
        for (int i = 0; i < expectedFilters.length; ++i) {
            Assert.assertEquals(expectedFilters[i].toString(), filters[i].toString());
        }
        System.out.println("TEST FilterParser.parse() PASSED");
    }

}