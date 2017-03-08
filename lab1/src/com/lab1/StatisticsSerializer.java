package com.lab1;

import com.lab1.Filter.Filter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;

/**
 * Created by Alexander on 08/03/2017.
 */
public class StatisticsSerializer {

    public static void printUsage() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/com/lab1/readme.txt"));
            String line = "";
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printStatistics(Statistics stat) {
        System.out.println("Total : " + stat.getLineCount() + " lines in " + stat.getFileCount() + " files");
        System.out.println("------------------------------------------------------------");
        Map<Filter, Statistics.Record> sortedStat = stat.sortByValue();
        for (Map.Entry<Filter, Statistics.Record> entry : sortedStat.entrySet()) {
            System.out.println(entry.getKey().toString() + " : " + entry.getValue().getLineCount() +
                    " lines in " + entry.getValue().getFileCount() + " files");
        }
    }

}
