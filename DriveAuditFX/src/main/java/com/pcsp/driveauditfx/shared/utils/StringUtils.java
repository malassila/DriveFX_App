package com.pcsp.driveauditfx.shared.utils;

import com.pcsp.driveauditfx.shared.device.Slot;

import java.util.StringTokenizer;

public class StringUtils {
    public static String extractSlot(String output) {
        Slot slot = new Slot();
        String slotValue = slot.getSlotWithValue(output);
        return slotValue;
    }

    public static String extractValueFromDriveData(String line) {
        System.out.println("line: " + line);
        StringTokenizer st = new StringTokenizer(line, ":");
        String token = "";
        while (st.hasMoreTokens()) {
            token = st.nextToken().replaceAll("[^a-zA-Z0-9.]", "");
        }
        return token;
    }
    public static String extractValueFromSMARTData(String line) {
        String[] split = line.split("[ \t]");
        String value = split[split.length - 1];
        return value;
    }

    public static String extractSize(String line) {
        String value = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
        value = value.replace(" ", "");
        return value;
    }

    public static String determineDriveType(String line) {
        if (line.toUpperCase().contains("ATA")) {
            return "SATA";
        } else if (line.toUpperCase().contains("NVME")) {
            return "NVMe";
        } else {
            return "SAS";
        }
    }

    public static int getInt(String line) {
        return Integer.parseInt(line.replaceAll("[^0-9]", ""));
    }


}
