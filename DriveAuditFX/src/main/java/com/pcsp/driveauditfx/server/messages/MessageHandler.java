package com.pcsp.driveauditfx.server.messages;

import com.pcsp.driveauditfx.server.FX.controller.MainController;
import com.pcsp.driveauditfx.server.database.*;
import com.pcsp.driveauditfx.server.socket.ServerSideSocket;
import com.pcsp.driveauditfx.shared.Project;
import com.pcsp.driveauditfx.shared.device.ServerModel;
import com.pcsp.driveauditfx.shared.messages.DriveMessageService;
import com.pcsp.driveauditfx.shared.messages.ServerMessageService;
import com.pcsp.driveauditfx.shared.device.Drive;
import com.pcsp.driveauditfx.shared.device.DriveServer;

import java.sql.SQLException;

import static com.pcsp.driveauditfx.shared.Project.servers;

public class MessageHandler implements Message {
    private String[] messageArray;
    private DriveServer server;
    private ServerModel serverModel;
    private DriveMessageService driveMessageService;
    private ServerMessageService serverMessageService;
    private ServerSideSocket serverSideSocket;
    private DriveServerDAO driveServerDAO;
    private HardDriveDAO hardDriveDAO;
    private MainController mainController;

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

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
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
                serverDAO().driveServerConnected(server);
                break;
            case "UPDATE":
                break;
            default:
                System.out.println("REMOVED SUCCESSFULLY");
                this.server.setStatus("Disconnected");
                serverDAO().driveServerDisconnected(server);
                break;
        }
    }

    void processDriveMessage(String message) {
        switch (getCommand()) {
            case "ADD":
                    handleDriveConnected(getSerialNumber());
                break;
            case "REMOVE":
                    handleDriveRemoved(getSerialNumber());
                break;
            case "WIPING":
                    handleDriveWipeStart(getSerialNumber());
                break;
            case "COMPLETE":
                    handleDriveWipeComplete(getSerialNumber());
                break;
            case "ERROR":
                 handleDriveError(getSerialNumber());
                break;
        }
    }


    public DriveServerDAO serverDAO(){
            driveServerDAO = new ServerDAO(DatabaseConnection.getConnection());
            return driveServerDAO;
    }

    public  HardDriveDAO driveDAO(){
            hardDriveDAO = new DriveDAO(DatabaseConnection.getConnection());
            return hardDriveDAO;
    }

    public void handleDriveConnected(String message){
        driveMessageService = new DriveMessageService(message);
        Drive drive = driveMessageService.saveDriveData();
        drive.setStatus("IDLE");
        Project.addDrive(drive);
        this.server.incrementNumOfDrives();
        // Add the drive to the slotMap in the DriveServer class
        server.addHardDrive(drive.getSlot(), drive);

        serverModel = Project.getServerModel(server.getServerName());

        serverModel.setNumOfConnected(serverModel.getNumOfConnected() + 1);
        serverDAO().updateDriveServer(serverModel);
//        serverModel.setNumOfWiping(serverModel.getNumOfWiping() + 1);

        // Insert the new drive into the database
        driveDAO().insertHardDrive(drive);
        // Update the ServerModel in the database
        serverDAO().updateDriveServer(serverModel);

        // Update the UI
        mainController.updateUI(serverModel);
    }

    public void handleDriveWipeStart(String message){
            Drive drive = server.getHardDrive(getSlot());
            drive.setStatus("Wiping");

            serverModel = Project.getServerModel(server.getServerName());

            serverModel.setNumOfWiping(serverModel.getNumOfWiping() + 1);

        try {
            // Update the drive into the database
            driveDAO().updateStatus(getSerialNumber(), "Wiping");
            // Update the ServerModel in the database
            serverDAO().updateDriveServer(serverModel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Update the UI
        mainController.updateUI(serverModel);
    }
    public void handleDriveWipeComplete(String message){
            Drive drive = server.getHardDrive(getSlot());
            drive.setStatus("Complete");

            serverModel = Project.getServerModel(server.getServerName());

            serverModel.setNumOfWiping(serverModel.getNumOfWiping() - 1);
            serverModel.setNumOfCompleted(serverModel.getNumOfCompleted() + 1);

        try {
            // Update the drive into the database
            driveDAO().updateStatus(getSerialNumber(), "Complete");
            // Update the ServerModel in the database
            serverDAO().updateDriveServer(serverModel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Update the UI
        mainController.updateUI(serverModel);
    }

    public void handleDriveRemoved(String message){
            Drive drive = server.getHardDrive(getSlot());
            server.decrementNumOfDrives();
            // Add the drive to the slotMap in the DriveServer class
            server.removeHardDrive(drive.getSlot());

            serverModel = Project.getServerModel(server.getServerName());
            if (drive.getStatus().equals("Wiping")){
                serverModel.setNumOfWiping(serverModel.getNumOfWiping() - 1);
            } else if (drive.getStatus().equals("Complete")){
                serverModel.setNumOfCompleted(serverModel.getNumOfCompleted() - 1);
            } else if (drive.getStatus().equals("Error Occurred")) {
                serverModel.setNumOfFailed(serverModel.getNumOfFailed() - 1);
            }



        serverModel.setNumOfConnected(serverModel.getNumOfConnected() - 1);

        try {
            // Update the status of the drive in the database only if it is not already complete, or failed
            driveDAO().updateStatus(getSerialNumber(), "Disconnected");
            // Update the ServerModel in the database
            serverDAO().updateDriveServer(serverModel);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Update the UI
        mainController.updateUI(serverModel);
    }

    private void handleDriveError(String serialNumber) {
        Drive drive = server.getHardDrive(getSlot());
        drive.setStatus("Error Occurred");

        serverModel = Project.getServerModel(server.getServerName());

        if (drive.getStatus().equals("Wiping")){
            serverModel.setNumOfWiping(serverModel.getNumOfWiping() - 1);
        }
        serverModel.setNumOfFailed(serverModel.getNumOfFailed() + 1);

        try {
            // Update the drive into the database
            driveDAO().updateStatus(getSerialNumber(), "Error Occurred");
            // Update the ServerModel in the database
            serverDAO().updateDriveServer(serverModel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Update the UI
        mainController.updateUI(serverModel);
    }



}
