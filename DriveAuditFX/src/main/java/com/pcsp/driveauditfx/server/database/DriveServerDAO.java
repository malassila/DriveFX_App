package com.pcsp.driveauditfx.server.database;

import com.pcsp.driveauditfx.shared.device.DriveModel;
import com.pcsp.driveauditfx.shared.device.DriveServer;
import com.pcsp.driveauditfx.shared.device.ServerModel;

import java.sql.SQLException;
import java.util.List;

public interface DriveServerDAO {
        void insertDriveServer(DriveServer server) throws SQLException;
        void updateDriveServer(ServerModel server);
        void updateDriveServerStatus(ServerModel server);
        void driveServerConnected(DriveServer server);
        void driveServerDisconnected(DriveServer server);
        ServerModel getDriveServer(String serverName) throws SQLException;
        public List<DriveModel> getDrivesByServer(String serverName);
        void resetConnectedServers();
        List<ServerModel> getAllServers();
        void deleteDriveServer(int id) throws SQLException;
//        void insertPort();
        void updatePort(String port) throws SQLException;
        String getPort(int id) throws SQLException;
        List<String> getPortsByDriveServerId(int DriveServerId) throws SQLException;
        void deletePort(int id) throws SQLException;

}
