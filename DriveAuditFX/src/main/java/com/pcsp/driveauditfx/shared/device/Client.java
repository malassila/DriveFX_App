package com.pcsp.driveauditfx.shared.device;

import javafx.beans.property.SimpleIntegerProperty;

public class Client {
    private SimpleIntegerProperty connectedDrives = new SimpleIntegerProperty();
    private SimpleIntegerProperty wipingDrives = new SimpleIntegerProperty();
    private SimpleIntegerProperty doneWipingDrives = new SimpleIntegerProperty();
    private SimpleIntegerProperty failedDrives = new SimpleIntegerProperty();
//
//    public void updateConnectedDrives() {
//        int count = 0;
//        for (Map.Entry<String, HardDrive> entry : drives.entrySet()) {
//            HardDrive drive = entry.getValue();
//            if (drive.getStatus() == HardDrive.Status.IDLE) {
//                count++;
//            }
//        }
//        setConnectedDrives(count);
//    }
//
//    public void updateWipingDrives() {
//        int count = 0;
//        for (Map.Entry<String, HardDrive> entry : drives.entrySet()) {
//            HardDrive drive = entry.getValue();
//            if (drive.getStatus() == HardDrive.Status.WIPING) {
//                count++;
//            }
//        }
//        setWipingDrives(count);
//    }
//
//    public void updateDoneWipingDrives() {
//        int count = 0;
//        for (Map.Entry<String, HardDrive> entry : drives.entrySet()) {
//            HardDrive drive = entry.getValue();
//            if (drive.getStatus() == HardDrive.Status.DONE_WIPING) {
//                count++;
//            }
//        }
//        setDoneWipingDrives(count);
//    }
//
//    public void updateFailedDrives() {
//        int count = 0;
//        for (Map.Entry<String, HardDrive> entry : drives.entrySet()) {
//            HardDrive drive = entry.getValue();
//            if
//

        }
