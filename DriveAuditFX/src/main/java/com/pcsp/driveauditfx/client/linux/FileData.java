package com.pcsp.driveauditfx.client.linux;

import java.io.*;
import java.util.Scanner;

public class FileData {
    private File file;
    private final String fileName;
    private final String filePath = "/smart/";
    private BufferedReader bufferedReader;
    private Scanner scanner;


    public FileData(String driveName, String fileName) throws FileNotFoundException {
        this.fileName = fileName + "_" + driveName + ".txt";
        this.file = new File(this.filePath + this.fileName);
        this.bufferedReader = new BufferedReader(new FileReader(this.file));
        this.scanner = new Scanner(this.file);
    }

    public String getValueFromFile(String containsValue) {
//        System.out.println("Reading file: " + this.fileName);
//        System.out.println("Searching for23: " + containsValue);
        try {
            String line;
            while ((line = this.bufferedReader.readLine()) != null) {
//                System.out.println("Line: " + line);
                if (line.toLowerCase().contains(containsValue)) {
//                    System.out.println("Found: " + line);
                    return line;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "0";
    }

    public String getValueFromFileMultiple(String[] containsValues, boolean requiresAll) {
//        System.out.println("Reading file: " + this.fileName);
        try {
            String line;
            while ((line = this.bufferedReader.readLine()) != null) {
                if (line.toLowerCase().contains(containsValues[0]) || line.toLowerCase().contains(containsValues[1])) {
//                    System.out.println("Found: " + line);
                    return line;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getValueFromFileTwoValues(String containsValue, String containsValue2) {
//        System.out.println("Reading file: " + this.fileName);
//        System.out.println("Searching for: " + containsValue + " and " + containsValue2);
        try {
            String line;
            while ((line = this.bufferedReader.readLine()) != null) {
//                System.out.println("Line: " + line);
                if (line.toLowerCase().contains(containsValue) && line.toLowerCase().contains(containsValue2)) {
//                    System.out.println("Found: " + line);
                    return line;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "N/A";
    }

    public void saveLineToFile(){

    }





}
