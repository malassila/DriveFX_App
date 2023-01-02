package com.pcsp.driveauditfx.shared.device;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DriveModel {
    // model, serial, size, smart_result, status, failed
    private StringProperty model;
    private StringProperty serial;
    private StringProperty size;
    private StringProperty smart;
    private StringProperty status;
//    private StringProperty failed;

    public DriveModel() {

    }

    public DriveModel(String model, String serial, String size, String smart, String status) {
        this.model = new SimpleStringProperty(model);
        this.serial = new SimpleStringProperty(serial);
        this.size = new SimpleStringProperty(size);
        this.smart = new SimpleStringProperty(smart);
        this.status = new SimpleStringProperty(status);
//        this.failed = new SimpleStringProperty(failed);
    }

    public String getModel() {
        return model.get();
    }

    public StringProperty modelProperty() {
        return model;
    }

    public void setModel(String model) {
        this.model.set(model);
    }

    public String getSerial() {
        return serial.get();
    }

    public StringProperty serialProperty() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial.set(serial);
    }

    public String getSize() {
        return size.get();
    }

    public StringProperty sizeProperty() {
        return size;
    }

    public void setSize(String size) {
        this.size.set(size);
    }

    public String getSmart() {
        return smart.get();
    }

    public StringProperty smartProperty() {
        return smart;
    }

    public void setSmart(String smart) {
        this.smart.set(smart);
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

//    public String getFailed() {
//        return failed.get();
//    }
//
//    public StringProperty failedProperty() {
//        return failed;
//    }
//
//    public void setFailed(String failed) {
//        this.failed.set(failed);
//    }
}
