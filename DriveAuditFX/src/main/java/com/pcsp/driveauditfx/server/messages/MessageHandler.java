package com.pcsp.driveauditfx.server.messages;

import com.pcsp.driveauditfx.server.socket.ServerSideSocket;
import com.pcsp.driveauditfx.shared.Project;
import com.pcsp.driveauditfx.shared.messages.DriveMessageService;
import com.pcsp.driveauditfx.shared.messages.ServerMessageService;
import com.pcsp.driveauditfx.shared.device.Drive;
import com.pcsp.driveauditfx.shared.device.DriveServer;

import static com.pcsp.driveauditfx.shared.Project.servers;

public class MessageHandler implements Message {
    private String[] messageArray;
    private DriveServer server;
    private DriveMessageService driveMessageService;
    private ServerMessageService serverMessageService;
    private ServerSideSocket serverSideSocket;

    /**
     * DRIVE, serverName, ADD, slot, serialNumber, "ADD"
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

    public void serverDisconnected() {

    }


    @Override
    public void processRawMessage(String message, ServerSideSocket serverSideSocket) {
//        System.out.println("Processing message");
        this.serverSideSocket = serverSideSocket;
        messageArray = splitMessage(message);
        this.server = getServer();
        String messageType = getType();


        switch (messageType) {
            case "SERVER":
                serverMessageService = new ServerMessageService(message);
                processServerMessage();
                break;
            case "DRIVE":
                processDriveMessage(message);
                break;
        }
    }



    public void processServerMessage() {
        System.out.println("Processing server command");
        switch (getCommand()) {
            case "ADD":
                this.serverSideSocket.setDriveServer(server);
                ServerSideSocket.addDriveServer(server);
                this.server.setStatus("Connected");
                break;
            case "UPDATE":
                break;
            default:
                System.out.println("REMOVED SUCCESSFULLY");
                this.server.setStatus("Disconnected");
                System.out.println(this.server);
                break;
        }
    }

    void processDriveMessage(String message) {
//        System.out.println("Processing drive command");
        switch (getCommand()) {
            case "ADD":
                driveMessageService = new DriveMessageService(message);

                Drive drive = driveMessageService.saveDriveData();
                drive.setStatus("IDLE");
                Project.addDrive(drive);
                System.out.println(this.server.getServerName() + ": New drive added to " + drive.getSlot() + "-->>\n-->>" + drive);
                this.server.incrementNumOfDrives();
                System.out.println(this.server);
                break;
            case "REMOVE":
                drive = Project.getDrive(messageArray[3]);
                System.out.println("DRIVE REMOVED SUCCESSFULLY");
                drive.setConnected(false);
                System.out.println(drive);
                System.out.println(drive.getSlot() + "<<<<<<<");
                server.removeHardDrive(drive.getSlot());
                this.server.decrementNumOfDrives();
                System.out.println(this.server);
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
