package com.lab1;

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
        try {
            if (args.length < 2) {
                printUsage();
                return;
            }
            Controller controller = new Controller(args[0]);
            controller.collectStatisticsOf(new File(args[1]));
            controller.printStatistics();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return;
    }

}
