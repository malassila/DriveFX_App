package com.pcsp.driveauditfx.client.messages;

import com.pcsp.driveauditfx.shared.Command;
import com.pcsp.driveauditfx.shared.Type;

import java.util.HashMap;

public class DriveMessageService {
    /**
     * messageArray[0] = Message Type
     * messageArray[1] = Server Name
     * messageArray[2] = command
     * messageArray[3] = slot
     * messageArray[4] = serial number
     * messageArray[5] = model
     * messageArray[6] = size
     * messageArray[7] = hours
     * messageArray[8] = reallocated sectors
     * messageArray[9] = smart result
     * messageArray[10] = status
     * messageArray[11] =
     *     private String name;
     *     private String model;
     *     private String serial;
     *     private String type;
     *     private String sectorSize;
     *     private String size;
     *     private String smartResult;
     *     private String slot = "1";
     *     private String hours;
     *     private String rsec;
     *     private String status;
     */
    private Type type;
    private String serverName;
    private Command command;
    private String slot;
    private String serialNumber;
    private String message;
    private HashMap<String, Boolean> include = new HashMap<>();


    public DriveMessageService() {

    }


    public static String createMessage(Type type, String serverName, Command command, String slot, String serialNumber, String message) {
        return type + " " + serverName + " " + command + " " + slot + " " + serialNumber + " " + message;
    }

}
