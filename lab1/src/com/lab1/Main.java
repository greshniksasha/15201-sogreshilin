package com.lab1;

import com.lab1.Filter.Filter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main {

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

    public static void main(String[] args) {
        if (args.length < 2) {
            printUsage();
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
