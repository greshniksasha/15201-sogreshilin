package com.lab1;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;


public class ConfigIteratorTest {
    String fileName = "test/com/lab1/ConfigIteratorTest.txt";

    @Test
    public void hasNext() throws Exception {
        ConfigIterator it = new ConfigIterator(fileName);
        Assert.assertTrue(it.hasNext());
        Assert.assertTrue(it.hasNext());
        Assert.assertTrue(it.hasNext());
        Assert.assertFalse(it.hasNext());
        System.out.println("TEST ConfigIterator.hasNext() PASSED");
    }

    @Test
    public void next() throws Exception {
        ConfigIterator it = new ConfigIterator(fileName);
        it.hasNext();
        Assert.assertEquals(".hpp", it.next());
        it.hasNext();
        Assert.assertEquals(".cpp", it.next());
        it.hasNext();
        Assert.assertEquals("<(20.01.1997 00:00:00)", it.next());
        System.out.println("TEST ConfigIterator.next() PASSED");
    }

}
