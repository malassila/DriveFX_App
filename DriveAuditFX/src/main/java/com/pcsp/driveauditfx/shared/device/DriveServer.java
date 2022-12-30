package com.pcsp.driveauditfx.shared.device;

import javafx.scene.layout.HBox;

import java.util.HashMap;

public class DriveServer {
    private String serverName;
    private String status;
    private HashMap<String, Drive> slotMap;
    private int numberOfDrivesConnected;


    public DriveServer() {

    }

    public DriveServer(String serverName, String status) {
        this.serverName = serverName;
        this.status = status;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public HashMap<String, Drive> getSlotMap() {
        return slotMap;
    }

    public void setSlotMap(HashMap<String, Drive> slotMap) {
        this.slotMap = slotMap;
    }

    public int getNumOfDrives() {
        return numberOfDrivesConnected;
    }

    public void setNumOfDrives(int numberOfDrivesConnected) {
        this.numberOfDrivesConnected = numberOfDrivesConnected;
    }

    public void incrementNumOfDrives() {
        this.numberOfDrivesConnected++;
    }

    public void decrementNumOfDrives() {
        this.numberOfDrivesConnected--;
    }

    public void addHardDrive(String slot, Drive hardDrive) {
        try {
            slotMap.put(slot, hardDrive);
        } catch (NullPointerException e) {
            slotMap = new HashMap<>();
            slotMap.put(slot, hardDrive);
        }
    }

    public Drive getHardDrive(String slot) {
        return slotMap.get(slot);
    }

    public void removeHardDrive(String slot) {
        slotMap.remove(slot);
    }

    public Drive getDriveByName(String driveName) {
        return slotMap.values().stream().filter(drive -> drive.getName().equals(driveName)).findFirst().orElse(null);
    }

    public void clearHardDrives() {
        slotMap.clear();
    }

    public void updateHardDrive(String slot, Drive hardDrive) {
        slotMap.replace(slot, hardDrive);
    }

    public void updateHardDriveStatus(String slot, String status) {
        slotMap.get(slot).setStatus(status);
    }

    public void getHardDriveStatus(String slot) {
        slotMap.get(slot).getStatus();
    }

    @Override
    public String toString() {
        return "DriveServer " + serverName + "{ " +
                "number of drives=" + getNumOfDrives() +
                "\nstatus: " + status + "\n" +
                ", slotMap:\n" + slotMap +
                '}';
    }
}
