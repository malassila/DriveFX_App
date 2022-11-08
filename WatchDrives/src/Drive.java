public class Drive {
    private String slot;
    private String deviceHandle;
    private String model;
    private String serial;
    private String type;
    private float powerOnHours;
    private int reallocatedSectors;
    private String smartResult;
    private String formFactor;
    private String sectorSize;
    private String status;

    public Drive() {
    }

    public Drive(String deviceHandle) {
        this.deviceHandle = deviceHandle;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getDeviceHandle() {
        return deviceHandle;
    }

    public void setDeviceHandle(String deviceHandle) {
        this.deviceHandle = deviceHandle;
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

    public float getPowerOnHours() {
        return powerOnHours;
    }

    public void setPowerOnHours(float powerOnHours) {
        this.powerOnHours = powerOnHours;
    }

    public int getReallocatedSectors() {
        return reallocatedSectors;
    }

    public void setReallocatedSectors(int reallocatedSectors) {
        this.reallocatedSectors = reallocatedSectors;
    }

    public String getSmartResult() {
        return smartResult;
    }

    public void setSmartResult(String smartResult) {
        this.smartResult = smartResult;
    }

    public String getFormFactor() {
        return formFactor;
    }

    public void setFormFactor(String formFactor) {
        this.formFactor = formFactor;
    }

    public String getSectorSize() {
        return sectorSize;
    }

    public void setSectorSize(String sectorSize) {
        this.sectorSize = sectorSize;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
