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
import com.pcsp.driveauditfx.shared.utils.StringUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pcsp.driveauditfx.shared.Project.servers;

public class MessageHandler implements Message {
    private String[] messageArray;
    private DriveServer server;
    private ServerModel serverModel;
    private DriveMessageService driveMessageService;
    private ServerMessageService serverMessageService;
    private static Map<String, ServerSideSocket> serverSideSockets = new HashMap();
    private DriveServerDAO driveServerDAO;
    private HardDriveDAO hardDriveDAO;
    private MessageDAO messageDAO;
    private MainController mainController;
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

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }


    @Override
    public void processRawMessage(String message, ServerSideSocket serverSideSocket) {
        System.out.println("Processing message: " + message);
        messageArray = splitMessage(message);
        this.server = getServer();
        serverSideSockets.put(server.getServerName(), serverSideSocket);
        insertMessageIntoDatabase(formattedMessage());
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

    public String formattedMessage() {
        String formattedMessage = "";
        switch (getType()){
            case "SERVER":
                if (getCommand().equals("ADD")) {
                    formattedMessage = getServer().getServerName() + " Connected!";
                } else {
                    formattedMessage = getServer().getServerName() + " Disconnected!";
                }
                break;
            case "DRIVE":
                if (getCommand().equals("ADD")) {
                    formattedMessage = getServer().getServerName() + "-->>" + getSlot()  + " Drive Added!";
                } else if (getCommand().equals("REMOVE")) {
                    formattedMessage = getServer().getServerName() + "-->>" + getSlot()  + " Drive Removed!";
                } else {
                    formattedMessage = getServer().getServerName() + "-->>" + getSlot()  + " Drive Update: " + getCommand();
                }
                break;
        }

    return formattedMessage;
    }

    public void processServerMessage() {
        System.out.println("Processing server command");
        switch (getCommand()) {
            case "ADD":
                    handleServerConnected(getMessage());
                break;
            case "REMOVE":
                    handleServerDisconnected(server.getServerName());
                break;
            default:
                System.out.println("Invalid server command");

                break;
        }
    }

    void processDriveMessage(String message) {
        switch (getCommand()) {
            case "ADD":
                    handleDriveConnected(message);
                break;
            case "REMOVE":
                    handleDriveRemoved(message);
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

    public MessageDAO messageDAO(){
            messageDAO = new MessageDAO(DatabaseConnection.getConnection());
            return messageDAO;
    }

    public void insertMessageIntoDatabase(String message){
        messageDAO().insertMessage(StringUtils.getInt(getServer().getServerName()), message);
    }


    public void handleDriveConnected(String message){
        System.out.println("Drive connected" + message + " " + getSlot());
        driveMessageService = new DriveMessageService(message);
        Drive drive = driveMessageService.saveDriveData();
        drive.setStatus("Connected");
        Project.addDrive(drive);
        this.server.incrementNumOfDrives();
        // Add the drive to the slotMap in the DriveServer class
        server.getNumOfDrives();
        // TODO: FIX THIS TEMP
        server.addHardDrive(getSlot(), drive);
        System.out.println("Drive added to slotMap at slot: " + getSlot());

        serverModel = Project.getServerModel(server.getServerName());

        serverModel.setNumOfConnected(serverModel.getNumOfConnected() + 1);
        serverDAO().updateDriveServer(serverModel);
//        serverModel.setNumOfWiping(serverModel.getNumOfWiping() + 1);

        // Insert the new drive into the database
        driveDAO().insertHardDrive(drive, serverModel.getServerName());
        // Update the ServerModel in the database
        serverDAO().updateDriveServer(serverModel);

        // Update the UI
        mainController.updateUI(serverModel);
    }


    public void handleServerDisconnected(String message){
        System.out.println("REMOVED SUCCESSFULLY");
        this.server.setStatus("Disconnected");

        serverModel = Project.getServerModel(server.getServerName());

        // reset all the values in the serverModel
        serverModel.setNumOfConnected(0);
        serverModel.setNumOfWiping(0);
        serverModel.setNumOfCompleted(0);
        serverModel.setNumOfFailed(0);
        serverModel.setStatus("Disconnected");

        // Update the ServerModel in the database
        serverDAO().updateDriveServer(serverModel);
        serverDAO().updateDriveServerStatus(serverModel);
System.out.println("Server status: " + serverModel.getStatus());
        // Update the UI
        mainController.updateUI(serverModel);
    }

    public void handleServerConnected(String message){
//        this.serverSideSocket.setDriveServer(server);
        ServerSideSocket.addDriveServer(server);
//        this.server.setStatus("Connected");

        serverModel = Project.getServerModel(server.getServerName());

        serverModel.setStatus("Connected");

        // Update the ServerModel in the database
        serverDAO().updateDriveServer(serverModel);
        serverDAO().updateDriveServerStatus(serverModel);

        // Update the UI
        mainController.updateUI(serverModel);


    }

    public Drive getDriveInSlot(String slot){
        return server.getHardDrive(slot);
    }

    public void handleDriveWipeStart(String message){

            Drive drive = server.getHardDrive(getSlot());
            drive.setStatus("Wiping");

            serverModel = Project.getServerModel(server.getServerName());

            serverModel.setNumOfWiping(serverModel.getNumOfWiping() + 1);

        try {
            // Update the drive into the database
            driveDAO().updateStatus(drive.getSerial(), "Wiping");
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
            driveDAO().updateStatus(drive.getSerial(), "Complete");
            // Update the ServerModel in the database
            serverDAO().updateDriveServer(serverModel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Update the UI
        mainController.updateUI(serverModel);
    }

    public void handleDriveRemoved(String message){
        System.out.println(message);
        System.out.println("name: " + getServer().getServerName());
        System.out.println("slot: " + messageArray[3]);
        System.out.println("name: " + server.getDriveByName(messageArray[3]));
        System.out.println("name: " + server.getServerName());
            Drive drive = server.getDriveByName(messageArray[3]);
            System.out.println("Drive removed: " + drive);
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
            driveDAO().updateStatus(drive.getSerial(), "Disconnected");
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
            driveDAO().updateStatus(drive.getSerial(), "Error Occurred");
            // Update the ServerModel in the database
            serverDAO().updateDriveServer(serverModel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Update the UI
        mainController.updateUI(serverModel);
    }

    public void sendWipeMessage(String serverName){
        ServerSideSocket serverSideSocket = serverSideSockets.get(serverName);
        serverModel = Project.getServerModel(serverName);
        serverModel.setStatus("Wiping");
        // Update the ServerModel in the database
        serverDAO().updateDriveServer(serverModel);
            serverSideSocket.sendMessage("WIPE");
    }

    public ServerSideSocket getServerSideSocket(String serverName) {
        return serverSideSockets.get(serverName);
    }



}
