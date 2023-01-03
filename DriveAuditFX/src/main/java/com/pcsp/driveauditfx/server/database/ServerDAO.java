package com.pcsp.driveauditfx.server.database;

import com.pcsp.driveauditfx.shared.device.DriveModel;
import com.pcsp.driveauditfx.shared.device.DriveServer;
import com.pcsp.driveauditfx.shared.device.Drive;
import com.pcsp.driveauditfx.shared.device.ServerModel;
import com.pcsp.driveauditfx.shared.utils.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.pcsp.driveauditfx.shared.utils.StringUtils.getInt;

public class ServerDAO implements DriveServerDAO {
    private Connection connection;
    public static int count = 0;

    public ServerDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insertDriveServer(DriveServer server) throws SQLException {
        String sql = "INSERT INTO server (name) VALUES (?)";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, server.getServerName());

        statement.executeUpdate();
    }

    @Override
    public void updateDriveServer(ServerModel server) {
        try {
            String sql = "UPDATE server SET connected_drives = ?, wiping_drives = ?, failed_drives = ?, completed_drives = ? WHERE name = ?";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, server.getNumOfConnected());
            statement.setInt(2, server.getNumOfWiping());
            statement.setInt(3, server.getNumOfCompleted());
            statement.setInt(4, server.getNumOfFailed());
            statement.setString(5, server.getServerName());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void updateDriveServerStatus(ServerModel server) {
        try {
            String sql = "UPDATE server SET status = ? WHERE name = ?";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, server.getStatus());
            statement.setString(2, server.getServerName());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void driveServerConnected(DriveServer server) {
        try {
            String sql = "UPDATE server SET status = ? WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, "Connected");
            statement.setString(2, server.getServerName());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void driveServerDisconnected(DriveServer server) {
        try {
            String sql = "UPDATE server SET status = ? WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, "Disconnected");
            statement.setString(2, server.getServerName());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public ServerModel getDriveServer(String serverName) {
        try {
            String sql = "SELECT * FROM server WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, serverName);

            ResultSet resultSet = statement.executeQuery();

            String name = resultSet.getString("name");
            String status = resultSet.getString("status");
            int connected = resultSet.getInt("connected_drives");
            int wiping = resultSet.getInt("wiping_drives");
            int complete = resultSet.getInt("completed_drives");
            int failed = resultSet.getInt("failed_drives");
            ServerModel server = new ServerModel(name, status , connected, wiping, complete, failed);

            return server;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void resetConnectedServers(){
        try {
            String sql = "UPDATE server SET connected_drives = ?, wiping_drives = ?, failed_drives = ?, completed_drives = ?, status = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, 0);
            statement.setInt(2, 0);
            statement.setInt(3, 0);
            statement.setInt(4, 0);
            statement.setString(5, "Disconnected");

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ServerModel> getAllServers() {
        try {

            String sql = "SELECT * FROM server";
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            List<ServerModel> servers = new ArrayList<>();

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String status = resultSet.getString("status");
                int connectedDrives = resultSet.getInt("connected_drives");
                int wipingDrives = resultSet.getInt("wiping_drives");
                int completeDrives = resultSet.getInt("completed_drives");
                int failedDrives = resultSet.getInt("failed_drives");

                ServerModel server = new ServerModel(name, status, connectedDrives, wipingDrives, completeDrives, failedDrives);
                servers.add(server);
            }
            return servers;
        } catch (SQLException e) {
            e.printStackTrace();
        }
            return null;
    }

    public List<DriveModel> getDrivesByServer(String serverName) {
        try {
            int serverId = getInt(serverName);
            String sql = "SELECT * FROM hard_drive WHERE server_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, serverId);

            ResultSet resultSet = statement.executeQuery();

            List<DriveModel> drives = new ArrayList<>();

            while (resultSet.next()) {
                // model, serial, size, smart_result, status
                String model = resultSet.getString("model");
                String serial = resultSet.getString("serial");
                String size = resultSet.getString("size");
                String smartResult = resultSet.getString("smart_result");
                String status = resultSet.getString("status");

                DriveModel drive = new DriveModel(model, serial, size, smartResult, status);
                drives.add(drive);
            }
         return drives;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public void deleteDriveServer(int id) throws SQLException {

    }

//    @Override
//    public void insertPort() {
//if (count == 0) {
//    count++;
//    try {
//        for (int i = 1; i <= 30; i++) {
////            String serverName = "Server" + i;i
//            int serverId = i; // You'll need to write this method to retrieve the ID of the server with the specified name
//            for (int j = 1; j <= 36; j++) {
//                String slotName = "Slot" + j;
//                String insertSql = "INSERT INTO slot_drive_mapping (slot_name, server_id, drive_id) VALUES ('" + slotName + "', " + serverId + ", NULL)";
//                // Execute the INSERT statement using a prepared statement or similar
//                PreparedStatement statement = connection.prepareStatement(insertSql);
//                statement.executeUpdate();
//            }
//        }
//    } catch (SQLException e) {
//        e.printStackTrace();
//    }
//
//}
//fnff
//    }



    @Override
    public void updatePort(String port) throws SQLException {

    }

    @Override
    public String getPort(int id) throws SQLException {
        return null;
    }

    @Override
    public List<String> getPortsByDriveServerId(int DriveServerId) throws SQLException {
        return null;
    }

    @Override
    public void deletePort(int id) throws SQLException {

    }
}
