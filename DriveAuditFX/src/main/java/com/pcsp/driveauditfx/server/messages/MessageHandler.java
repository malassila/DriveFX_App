package com.pcsp.driveauditfx.server.messages;

import com.pcsp.driveauditfx.shared.Command;
import com.pcsp.driveauditfx.shared.Project;
import com.pcsp.driveauditfx.shared.device.Drive;
import com.pcsp.driveauditfx.shared.device.DriveServer;

import java.util.List;

import static com.pcsp.driveauditfx.shared.Project.servers;

public class MessageHandler implements Message {
    private String[] messageArray;
    private DriveServer server;

    /**
     * messageArray[0] = Message Type
     * messageArray[1] = Server Name
     * messageArray[2] = command
     * messageArray[3] = slot
     * messageArray[4] = serial number
     * messageArray[5] = message
     */
    @Override
    public String getType() {
        return messageArray[0];
    }

    @Override
    public DriveServer getServer() {
        server = servers.stream().filter(s -> s.getServerName()
                        .equals(messageArray[1]))
                .findFirst().orElse(null);
        return server;
    }
    @Override
    public String getCommand() {
        return messageArray[2];
    }
    @Override
    public String getSlot() {
        return messageArray[3];
    }
    @Override
    public String getSerialNumber() {
        return messageArray[4];
    }
    @Override
    public String getMessage() {
        return messageArray[5];
    }


    @Override
    public String[] splitMessage(String message) {
        String[] messageArray = message.split(" ");
        return messageArray;
    }


    @Override
    public void processRawMessage(String message) {
        System.out.println("Processing message");
        messageArray = splitMessage(message);
        server = getServer();
        switch (getType()) {
            case "SERVER":
                processServerMessage();
                break;
            case "DRIVE":
                processDriveMessage();
                break;
        }
    }



    void processServerMessage() {
        System.out.println("Processing server command");
        switch (getCommand()) {
            case "ADD":
                server.setStatus("Connected");
                break;
            case "REMOVE":
                server.setStatus("Disconnected");
                break;
            case "UPDATE":
                break;
        }
    }

    void processDriveMessage() {
        System.out.println("Processing drive command");
        switch (getCommand()) {
            case "ADD":
                server.addHardDrive(getSlot(), new Drive(getSerialNumber()));
                server.getHardDrive(getSlot()).setStatus("Connected");
                server.getHardDrive(getSlot()).setConnected(true);
                break;
            case "REMOVE":
                server.removeHardDrive(getSlot());
                server.getHardDrive(getSlot()).setConnected(false);
                break;
            case "WIPING":
                server.getHardDrive(getSlot()).setStatus("Wiping");
                break;
            case "COMPLETE":
                server.getHardDrive(getSlot()).setStatus("Complete");
                break;
            case "ERROR":
                server.getHardDrive(getSlot()).setStatus("Error Occurred");
                break;
        }
    }
}
