package com.pcsp.driveauditfx.shared.messages;

import com.pcsp.driveauditfx.shared.Command;
import com.pcsp.driveauditfx.shared.Type;
import com.pcsp.driveauditfx.shared.device.Drive;
import com.pcsp.driveauditfx.shared.device.DriveServer;

import java.util.HashMap;
import java.util.StringTokenizer;

import static com.pcsp.driveauditfx.shared.Project.servers;

public class DriveMessageService {
    /**
     * messageArray[0] = Message Type
     * messageArray[1] = Server Name
     * messageArray[2] = command
     * messageArray[3] = slot
     * messageArray[4] = name
     * messageArray[5] = model
     * messageArray[6] = serial
     * messageArray[7] = type
     * messageArray[8] = sectorSize
     * messageArray[9] = size
     * messageArray[10] = smartResult
     * messageArray[11] = hours
     * messageArray[12] = rsec
     * messageArray[13] = spindleSpeed
     * messageArray[14] = status
     */
    private String[] messageArray;
    private DriveServer server;
    private String slot;
    private String message;
    private HashMap<String, Boolean> include = new HashMap<>();


    public DriveMessageService() {

    }
    public DriveMessageService(String message) {
        this.message = message;
        messageArray = splitMessage(message);
    }
    public static String createDriveDataMessage(Type messageType, String serverName, String command,String name, String model, String serial, String type, String sectorSize, String size, String smartResult, String slot, String hours, String rsec, String spindleSpeed , String status) {
        return messageType + " " + serverName + " " + command + " " + slot + " " + name + " " + model + " " + serial + " " + type + " " + sectorSize + " " + size + " " + smartResult + " " + hours + " " + rsec + " " + spindleSpeed + " " + status;
    }


    public static String createMessage(Type type, String serverName, Command command, String slot, String serialNumber, String message) {
        return type + " " + serverName + " " + command + " " + slot + " " + serialNumber + " " + message;
    }

    public String getIndex(int index) {
        StringTokenizer st = new StringTokenizer(message, " ");
        int i = 0;
        while (st.hasMoreTokens()) {
            if (i == index) {
                return st.nextToken();
            }
            st.nextToken();
            i++;
        }
        return "null";
    }

    public String[] splitMessage(String message) {
        String[] messageArray = message.split(" ");
        return messageArray;
    }

    public Type getMessageType() {
        Type type = Type.valueOf(messageArray[0]);
        return Type.valueOf(getIndex(0));
    }

    public DriveServer getServer() {
        server = servers.stream().filter(s -> s.getServerName()
                        .equals(getIndex(1)))
                .findFirst().orElse(null);
        return server;
    }

    public String getCommand() {
        String command = messageArray[2];
        return getIndex(2);
    }

    public String getSlot() {
        slot = messageArray[3];
        return getIndex(3);
    }

    public String getName() {
        System.out.println("Name: " + messageArray[0]);
        String name = messageArray[4];
        return getIndex(4);
    }

    public String getModel() {
        String model = messageArray[5];
        return getIndex(5);
    }

    public String getSerial() {
        String serial = messageArray[6];
        return getIndex(6);
    }

    public String getType() {
        String type = messageArray[7];
        return getIndex(7);
    }

    public String getSectorSize() {
        String sectorSize = messageArray[8];
        return getIndex(8);
    }

    public String getSize() {
        String size = messageArray[9];
        return getIndex(9);
    }

    public String getSmartResult() {
        String smartResult = messageArray[10];
        return getIndex(10);
    }

    public String getHours() {
        String hours = messageArray[11];
        return getIndex(11);
    }

    public String getRsec() {
        String rsec = messageArray[12];
        return getIndex(12);
    }

    public String getSpindleSpeed() {
        String spindleSpeed = messageArray[13];
        return getIndex(13);
    }

    public String getStatus() {
        String status = messageArray[14];
        return getIndex(14);
    }

    public Drive saveDriveData() {
        server = getServer();
        System.out.println("Server: " + server);
        Drive drive = new Drive("sdc");
            drive.setModel(getModel());
            drive.setSerial(getSerial());
            drive.setType(getType());
            drive.setSectorSize(getSectorSize());
            drive.setSize(getSize());
            drive.setSmartResult(getSmartResult());
            drive.setSlot(getSlot());
            drive.setHours(getHours());
            drive.setRsec(getRsec());
            drive.setSpindleSpeed(getSpindleSpeed());
            drive.setStatus(getStatus());
        server.addHardDrive(getSlot(), drive);
        return drive;
    }






}
