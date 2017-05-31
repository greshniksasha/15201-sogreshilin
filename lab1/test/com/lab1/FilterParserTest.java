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

        Filter[] andArray = {
                new NotFilter(new ExtensionFilter("hpp")),
                new LessTimeFilter(853718400)
        };
        Filter[] orArray = {
                new ExtensionFilter("cpp"),
                new ExtensionFilter("cc"),
                new ExtensionFilter("hpp"),
                new ExtensionFilter("h"),
                new NotFilter(new LessTimeFilter(853718400))
        };
        Filter[] expectedFilters = {
                new ExtensionFilter("cpp"),
                new NotFilter(new ExtensionFilter("hpp")),
                new LessTimeFilter(853718400),
                new AndFilter(andArray),
                new OrFilter(orArray)
        };

        Filter[] filters = FilterParser.parse(fileName);
        for (int i = 0; i < expectedFilters.length; ++i) {
            Assert.assertEquals(expectedFilters[i].toString(), filters[i].toString());
        }
        System.out.println("TEST FilterParser.parse() PASSED");
    }

}
