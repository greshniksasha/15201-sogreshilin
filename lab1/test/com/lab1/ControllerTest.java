package com.lab1;

import com.lab1.Filter.Filter;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Alexander on 26/02/2017.
 */
public class ControllerTest {
    String configFile = "test/com/lab1/ControllerTest.txt";
    String testDir = "test/com/lab1/testDir";

    @Test
    public void collectStatisticsOf() throws Exception {
        Controller controller = new Controller(configFile);
        controller.collectStatisticsOf(new File(testDir));
        Map<String, Integer> expected = new HashMap<String, Integer>();
        expected.put(".txt", 15);
        expected.put(".h", 20);
        expected.put(".c", 30);
        expected.put("&(|(.txt .h .c) ~(.txt))", 50);
        expected.put("&(|(.txt .h .c) ~(.h))", 45);
        expected.put("&(|(.txt .h .c) ~(|(.txt .c)))", 20);
        expected.put("&(|(.txt .h .c) <(26.02.2017 18:30:00))", 30);
        expected.put("&(|(.txt .h .c) >(26.02.2017 18:30:00))", 35);
        expected.put("&(|(.txt .h .c) <(26.02.2017 18:30:00))", 30);
        expected.put("&(|(.txt .h .c) ~(&(|(.txt .h .c) <(26.02.2017 18:30:00))))", 35);

        Statistics stat = controller.getStatistics();
        Filter[] filters = controller.getFilters();
        for (Filter filter : filters) {
            Assert.assertEquals((long)expected.get(filter.toString()), stat.getLineCount(filter));
        }

        Assert.assertEquals(65, stat.getLineCount());
        Assert.assertEquals(3, stat.getFileCount());
        System.out.println("TEST ControllerTest.collectStatisticsOf() PASSED");
    }

}