package com.pcsp.driveauditfx.client.linux;

import java.io.*;

public class FileData {
    private File file;
    private final String fileName;
    private final String filePath = "/tmp/";
    private BufferedReader bufferedReader;


    public FileData(String driveName, String fileName) throws FileNotFoundException {
        this.fileName = fileName + "_" + driveName + ".txt";
        this.file = new File(this.filePath + this.fileName);
        this.bufferedReader = new BufferedReader(new FileReader(this.file));
    }

    public String getValueFromFile(String containsValue) {
        try {
            String value = bufferedReader.lines().filter(line -> line.toLowerCase().contains(containsValue)).findFirst().get();
            return value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getValueFromFileMultiple(String[] containsValues, boolean requiresAll) {
        try {
            while (bufferedReader.readLine() != null) {
                String value = bufferedReader.readLine();
                for (String containsValue : containsValues) {
                    if (value.toLowerCase().contains(containsValue)) {
                        return value;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getValueFromFileTwoValues(String containsValue, String containsValue2) {
        try {
            while (bufferedReader.readLine() != null) {
                String value = bufferedReader.readLine();
                if (value.toLowerCase().contains(containsValue) && value.toLowerCase().contains(containsValue2)) {
                    return value;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



}
