package com.pcsp.driveauditfx.shared.device;

import javafx.beans.property.SimpleIntegerProperty;
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
        System.out.println("Searching for drive with name: " + driveName);
        System.out.println("Number of drives in slotMap: " + slotMap.size());
        Drive drive = slotMap.values().stream()
                .filter(d -> {
                    System.out.println("Checking drive with name: " + d.getName());
                    return d.getName().equals(driveName);
                })
                .findFirst().orElse(null);
        System.out.println("Found drive: " + drive);
        return drive;
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
