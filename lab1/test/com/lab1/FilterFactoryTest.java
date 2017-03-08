package com.lab1;

import com.lab1.Filter.Filter;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class FilterFactoryTest {
    @Test
    public void create() throws Exception {
        Filter f1 = FilterFactory.create(".hpp");
        Filter f2 = FilterFactory.create("<1488962231");
        Filter f3 = FilterFactory.create("!.hpp");
        Filter f4 = FilterFactory.create("&(!.hpp >1488962231)");
        Filter f5 = FilterFactory.create("|(&(.cpp .hpp) !.h !.doc)");
        Filter f6 = FilterFactory.create("|(|(|(.h .hpp) .cc) .cpp)");

        Assert.assertEquals(".hpp", f1.toString());
        Assert.assertEquals("<1488962231", f2.toString());
        Assert.assertEquals("!(.hpp)", f3.toString());
        Assert.assertEquals("&(!(.hpp) >1488962231)", f4.toString());
        Assert.assertEquals("|(&(.cpp .hpp) !(.h) !(.doc))", f5.toString());
        Assert.assertEquals("|(|(|(.h .hpp) .cc) .cpp)", f6.toString());
        System.out.println("TEST FilterFactory.create() PASSED");
    }

}
