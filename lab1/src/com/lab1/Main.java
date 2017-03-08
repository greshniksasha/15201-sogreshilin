package com.lab1;

import com.lab1.Filter.Filter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main {

    public static void main(String[] args) {
        if (args.length < 2) {
            StatisticsSerializer.printUsage();
            return;
        }
        try {
            Controller controller = new Controller(args[0]);
            controller.collectStatisticsOf(new File(args[1]));
            StatisticsSerializer.printStatistics(controller.getStatistics());
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return;
    }

}
