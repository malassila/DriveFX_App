package com.pcsp.driveauditfx.client.linux;

import static com.pcsp.driveauditfx.client.ClientProperties.BOOT_DRIVE;

public class LinuxCommandImpl implements LinuxCommand {
    public String driveName;
    private final String SMARTCTL_COMMAND;
    private final String DRIVE_INFO_COMMAND;
    private final String SLOT_COMMAND;
    private final String TYPE_COMMAND;


    public LinuxCommandImpl(String driveName) {
        this.driveName = driveName;
        SLOT_COMMAND = "ls -l /dev/disk/by-path/ | grep " + this.driveName +  " | awk \'! /part*|" + BOOT_DRIVE + "/\' | awk \'{print $9}\'";
        SMARTCTL_COMMAND = "sudo smartctl -a /dev/" + this.driveName + " > /tmp/SMRT_" + this.driveName + ".txt";
        DRIVE_INFO_COMMAND = "sudo lsblk -dJo NAME,MODEL,SERIAL,SIZE,TYPE,PHY-SEC,RQ-SIZE | grep " + this.driveName + " > /tmp/INFO_" + this.driveName + ".txt";
        TYPE_COMMAND = "lsscsi | grep " + this.driveName + " | awk \'{print $3}\'";
    }



    public static String runCommand(String command) {
        String output = "";
        try {
            Process p = Runtime.getRuntime().exec(command);
            p.waitFor();
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(p.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                output += line + " ";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    @Override
    public String getSlot() {
        return runCommand(SLOT_COMMAND);
    }

    @Override
    public String getType() {
        return runCommand(TYPE_COMMAND);
    }

    @Override
    public void saveSMART() {
        runCommand(SMARTCTL_COMMAND);
    }

    @Override
    public void saveDriveInfo() {
        runCommand(DRIVE_INFO_COMMAND);
    }

}
