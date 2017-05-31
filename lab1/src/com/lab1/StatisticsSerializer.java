package com.lab1;

import com.lab1.Filter.Filter;

import javax.print.PrintException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;

/**
 * Created by Alexander on 08/03/2017.
 */
public class StatisticsSerializer {

    public static void printUsage() throws PrintException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/com/lab1/readme.txt"));
            String line = "";
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            throw new PrintException("Cannot print usage", e);
        }
    }

    public static void printStatistics(Statistics stat) throws FilterSerializeException, FilterNullPointerException {

        Factory factory = new Factory();
        Map<Filter, Statistics.Record> sortedStat = stat.sortByValue();

        System.out.println(
                "Total : " + stat.getLineCount() +
                        " lines in " + stat.getFileCount() +
                        " files");
        System.out.println("------------------------------------------------------------");

        for (Map.Entry<Filter, Statistics.Record> entry : sortedStat.entrySet()) {
            System.out.println(
                    factory.create(entry.getKey()).serialize(entry.getKey()) +
                            " : " + entry.getValue().getLineCount() +
                            " lines in " + entry.getValue().getFileCount() +
                            " files");
        }
    }

}
