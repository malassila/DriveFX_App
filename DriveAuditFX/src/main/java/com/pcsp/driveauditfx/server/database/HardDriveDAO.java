package com.pcsp.driveauditfx.server.database;

import com.pcsp.driveauditfx.shared.device.Drive;

import java.sql.SQLException;
import java.util.List;

public interface HardDriveDAO {
    void insertHardDrive(Drive hardDrive);
    void updateStatus(String serial, String status) throws SQLException;
    void updateSlot(String serial, String slot) throws SQLException;
    void updateStatus(String serial, boolean connected) throws SQLException;
    Drive getHardDrive(String serial) throws SQLException;
    List<Drive> getAllHardDrives() throws SQLException;
    void deleteHardDrive(String serial) throws SQLException;
}
