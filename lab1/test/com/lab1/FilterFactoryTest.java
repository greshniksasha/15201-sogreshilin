package com.lab1;

import com.lab1.Filter.Filter;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class FilterFactoryTest {
    @Test
    public void create() throws Exception {
        FilterFactory factory = new FilterFactory();
        Filter f1 = factory.create(".hpp");
        Filter f2 = factory.create("<(02.02.1997 00:00:00)");
        Filter f3 = factory.create("~.hpp");
        Filter f4 = factory.create("&(~.hpp >(02.02.1997 00:00:00))");
        Filter f5 = factory.create("|(&(.cpp .hpp) ~.h ~.doc)");
        Filter f6 = factory.create("|(|(|(.h .hpp) .cc) .cpp)");

        Assert.assertEquals(".hpp", f1.toString());
        Assert.assertEquals("<(02.02.1997 00:00:00)", f2.toString());
        Assert.assertEquals("~(.hpp)", f3.toString());
        Assert.assertEquals("&(~(.hpp) >(02.02.1997 00:00:00))", f4.toString());
        Assert.assertEquals("|(&(.cpp .hpp) ~(.h) ~(.doc))", f5.toString());
        Assert.assertEquals("|(|(|(.h .hpp) .cc) .cpp)", f6.toString());
        System.out.println("TEST FilterFactory.create() PASSED");
    }

}