package com.lab1;

import com.lab1.Filter.Filter;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class FactoryTest {
    @Test
    public void create() throws Exception {
        Filter f1 = Factory.create(".hpp");
        Filter f2 = Factory.create("<1488962231");
        Filter f3 = Factory.create("!.hpp");
        Filter f4 = Factory.create("&(!.hpp >1488962231)");
        Filter f5 = Factory.create("|(&(.cpp .hpp) !.h !.doc)");
        Filter f6 = Factory.create("|(|(|(.h .hpp) .cc) .cpp)");

        Assert.assertEquals(".hpp", Factory.create(f1).serialize(f1));
        Assert.assertEquals("<1488962231", Factory.create(f2).serialize(f2));
        Assert.assertEquals("!(.hpp)", Factory.create(f3).serialize(f3));
        Assert.assertEquals("&(!(.hpp) >1488962231)", Factory.create(f4).serialize(f4));
        Assert.assertEquals("|(&(.cpp .hpp) !(.h) !(.doc))", Factory.create(f5).serialize(f5));
        Assert.assertEquals("|(|(|(.h .hpp) .cc) .cpp)", Factory.create(f6).serialize(f6));
        System.out.println("TEST FilterFactory.create() PASSED");
    }

}
