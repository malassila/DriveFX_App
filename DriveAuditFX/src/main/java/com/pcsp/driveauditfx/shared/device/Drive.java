package com.pcsp.driveauditfx.shared.device;

public class Drive {
    private String slot;
    private String name;
    private String model;
    private String serial;
    private String type;
    private String sectorSize;
    private String size;
    private String smartResult;
    private String hours;
    private String rsec;
    private String spindleSpeed;
    private String status;
    private boolean connected;

    public Drive() {
    }

    public Drive(String name) {
        this.name = name;
    }

    public Drive(String name, String model, String serial, String type, String sectorSize, String size, String smartResult, String slot, String hours, String rsec, String spindleSpeed , String status) {
        this.name = name;
        this.model = model;
        this.serial = serial;
        this.type = type;
        this.sectorSize = sectorSize;
        this.size = size;
        this.smartResult = smartResult;
        this.slot = slot;
        this.hours = hours;
        this.rsec = rsec;
        this.spindleSpeed = spindleSpeed;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSectorSize() {
        return sectorSize;
    }

    public void setSectorSize(String sectorSize) {
        this.sectorSize = sectorSize;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSmartResult() {
        return smartResult;
    }

    public void setSmartResult(String smartResult) {
        this.smartResult = smartResult;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getRsec() {
        return rsec;
    }

    public void setRsec(String rsec) {
        this.rsec = rsec;
    }

    public String getSpindleSpeed() {
        return spindleSpeed;
    }

    public void setSpindleSpeed(String spindleSpeed) {
        this.spindleSpeed = spindleSpeed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public boolean isConnected() {
        return connected;
    }

    @Override
    public String toString() {
        return "Drive " + name + ": model=" + model + ", serial=" + serial + ", type=" + type + ", sectorSize=" + sectorSize + ", size=" + size
                + ", smartResult=" + smartResult + ", hours=" + hours + ", rsec=" + rsec + ", spindleSpeed=" + spindleSpeed + ", status=" + status + "}\n";
    }

}
