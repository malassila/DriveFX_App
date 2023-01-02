package com.pcsp.driveauditfx.server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    private Connection connection;

    public MessageDAO(Connection connection) {
        this.connection = connection;
    }

    public void insertMessage(int serverId, String message) {
        try {
            String sql = "INSERT INTO message (server_id, message_string) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, serverId);
            statement.setString(2, message);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getMessages(int serverId) {
        try {
            String sql = "SELECT message_string FROM message WHERE server_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, serverId);
            ResultSet resultSet = statement.executeQuery();
            List<String> messages = new ArrayList<>();
            while (resultSet.next()) {
                messages.add(resultSet.getString("message_string"));
            }
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    public List<String> getAllMessages() {
        try {
            String sql = "SELECT message_string, timestamp FROM message";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            List<String> messages = new ArrayList<>();
            while (resultSet.next()) {
                messages.add(resultSet.getString("message_string"));
            }
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

}
