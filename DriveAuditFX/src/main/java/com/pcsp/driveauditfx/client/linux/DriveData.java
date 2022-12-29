package com.pcsp.driveauditfx.client.linux;

import com.pcsp.driveauditfx.shared.device.Drive;

public interface DriveData {
    public Drive getDriveData();
    public String getSlot();
    public String getName();
    public String setName();
    public String getModel();
    public String getSerialNumber();
    public String getType();
    public String getSectorSize();
    public String getSize();
    public String getSmartResult();
    public String getPowersOnHours();
    public String getRotationRate();
    public String getReallocatedSectors();
    public String getStatus();
    public void saveSMART();
    public void saveDriveInfo();

}
