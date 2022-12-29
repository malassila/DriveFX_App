package com.pcsp.driveauditfx.shared.device;

import javafx.scene.layout.HBox;

import java.util.HashMap;

public class DriveServer {
    private String serverName;
    private String status;
    private HashMap<String, Drive> slotMap;


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

    public void addHardDrive(String slot, Drive hardDrive) {
        slotMap.put(slot, hardDrive);
    }

    public Drive getHardDrive(String slot) {
        return slotMap.get(slot);
    }

    public void removeHardDrive(String slot) {
        slotMap.remove(slot);
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
}
