package com.pcsp.driveauditfx.client.linux;

public interface LinuxCommand {
    public String getSlot();
    public String getType();
    public void saveSMART();
    public void saveDriveInfo();

}
