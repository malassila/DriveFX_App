package com.pcsp.driveauditfx.server.database;

import com.pcsp.driveauditfx.shared.device.DriveServer;
import com.pcsp.driveauditfx.shared.device.Drive;
import com.pcsp.driveauditfx.shared.device.ServerModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServerDAO implements DriveServerDAO {
    private Connection connection;

    public ServerDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insertDriveServer(DriveServer server) throws SQLException {
        String sql = "INSERT INTO servers (name) VALUES (?)";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, server.getServerName());

        statement.executeUpdate();
    }

    @Override
    public void updateDriveServer(ServerModel server) {
        try {
            String sql = "UPDATE servers SET connected = ?, wiping = ?, failed = ?, complete = ? WHERE name = ?";

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
    public void driveServerConnected(DriveServer server) {
        try {
            String sql = "UPDATE servers SET connected = ? WHERE name = ?";
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
            String sql = "UPDATE servers SET connected = ? WHERE name = ?";
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
            String sql = "SELECT * FROM servers WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, serverName);

            ResultSet resultSet = statement.executeQuery();

            String name = resultSet.getString("name");
            String status = resultSet.getString("status");
            int connected = resultSet.getInt("connected_drives");
            int wiping = resultSet.getInt("wiping_drives");
            int complete = resultSet.getInt("complete_drives");
            int failed = resultSet.getInt("failed_drives");
            ServerModel server = new ServerModel(name, status , connected, wiping, complete, failed);

            return server;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ServerModel> getAllServers() {
        try {
            String sql = "SELECT * FROM servers";
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            List<ServerModel> servers = new ArrayList<>();

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String status = resultSet.getString("status");
                int connectedDrives = resultSet.getInt("connected_drives");
                int wipingDrives = resultSet.getInt("wiping_drives");
                int completeDrives = resultSet.getInt("complete_drives");
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

    @Override
    public void deleteDriveServer(int id) throws SQLException {

    }

    @Override
    public void insertPort(int DriveServerId, String port) throws SQLException {

    }

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
