package com.pcsp.driveauditfx.client.linux;

import com.pcsp.driveauditfx.shared.device.Drive;

import java.io.FileNotFoundException;

import static com.pcsp.driveauditfx.shared.utils.StringUtils.*;

public class DriveDataImpl implements DriveData {
    private Drive drive;
    private LinuxCommandImpl linuxCommand;
    private FileData smartData;
    private FileData driveInfoData;
//    private

    Drive getDrive() {
        return drive;
    }
    public DriveDataImpl(String driveName) throws FileNotFoundException {
        drive = new Drive(driveName);
        smartData = new FileData(driveName, "smart");
        driveInfoData = new FileData(driveName, "driveInfo");
    }


    @Override
    public Drive getDriveData() {
        linuxCommand = new LinuxCommandImpl(drive.getName());
        saveSMART();
        saveDriveInfo();
            drive.setSlot("Slot1");
            drive.setModel(getModel());
            drive.setSerial(getSerialNumber());
            drive.setType(getType());
            drive.setSectorSize(getSectorSize());
            drive.setSize(getSize());
            drive.setSmartResult(getSmartResult());
            drive.setHours(getPowersOnHours());
            drive.setSpindleSpeed(getRotationRate());
            drive.setRsec(getReallocatedSectors());
            drive.setStatus(getStatus());
        return drive;
    }

    /*
      * The slot command returns the physical pci port that the drive is connected to.
      * The output looks like this:
      * pci-0000:03:00.0-sas-exp0x5001438024db7940-phy11-lun-0
    */
    @Override
    public String getSlot() {
        String commandOutput = linuxCommand.getSlot();
        System.out.println("Slot: " + commandOutput);
        return extractSlot(commandOutput);
    }

    @Override
    public String getName() {
        return drive.getName();
    }

    @Override
    public String setName() {
        return null;
    }

    @Override
    public String getModel() {
        String model = smartData.getValueFromFile("model");
        model = extractValueFromDriveData(model);
//        System.out.println("Model: " + model);
        return model;
    }

    @Override
    public String getSerialNumber() {
        String serialNumber = driveInfoData.getValueFromFile("serial");
        serialNumber = extractValueFromDriveData(serialNumber);
//        System.out.println("Serial number: " + serialNumber);
        return serialNumber;
    }

    @Override
    public String getType() {
        String commandOutput = linuxCommand.getType();
        String driveType = determineDriveType(commandOutput);
//        System.out.println("Drive type: " + driveType);
        return driveType;
    }

    @Override
    public String getSectorSize() {
        String sectorSize = driveInfoData.getValueFromFile("phy-sec");
        sectorSize = extractValueFromDriveData(sectorSize);
//        System.out.println("Sector size: " + sectorSize);
        return sectorSize;
    }

    @Override
    public String getSize() {
        String size = smartData.getValueFromFile("capacity");
        size = extractSize(size);
//        System.out.println("Size: " + size);
        return size;
    }

    @Override
    public String getSmartResult() {
        String[] contains = {"smart overall-health self-assessment test result", "smart health status"};
        String smartResult = smartData.getValueFromFileMultiple(contains, false);
        smartResult = extractValueFromDriveData(smartResult);
        smartResult = smartResult.replace("OK", "PASSED");
//        System.out.println("SMART result: " + smartResult);
        return smartResult;
    }

    @Override
    public String getPowersOnHours() {
        String powerOnHours = smartData.getValueFromFileTwoValues("power", "hours");
        powerOnHours = extractValueFromSMARTData(powerOnHours);
//        System.out.println("Power on hours: " + powerOnHours);
        return powerOnHours;
    }

    @Override
    public String getRotationRate() {
        String rotationRate = smartData.getValueFromFileTwoValues("rotation", "rate");
        rotationRate = extractValueFromDriveData(rotationRate);
//        System.out.println("Rotation rate: " + rotationRate);
        return rotationRate;
    }

    @Override
    public String getReallocatedSectors() {
        String reallocatedSectors = smartData.getValueFromFile("reallocated");

        reallocatedSectors = extractValueFromSMARTData(reallocatedSectors);
//        System.out.println("Reallocated sectors: " + reallocatedSectors);
        return reallocatedSectors;
    }

    @Override
    public String getStatus() {
        return null;
    }

    @Override
    public void saveSMART() {
        linuxCommand.saveSMART();
    }

    @Override
    public void saveDriveInfo() {
        linuxCommand.saveDriveInfo();
    }
}
