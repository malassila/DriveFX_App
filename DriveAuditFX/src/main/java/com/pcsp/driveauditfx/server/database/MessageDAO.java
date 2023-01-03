package com.pcsp.driveauditfx.server.database;

import com.pcsp.driveauditfx.shared.utils.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessageDAO {
    private Connection connection;

    public MessageDAO(Connection connection) {
        this.connection = connection;
    }

    public void insertMessage(Integer serverId, String message) {
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
            String sql = "SELECT message_string, timestamp FROM message ORDER BY id DESC LIMIT 100";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            List<String> messages = new ArrayList<>();
            while (resultSet.next()) {
                messages.add(resultSet.getString("message_string") + " " + resultSet.getString("timestamp"));
            }
            Collections.reverse(messages);
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<String> getSelectedServers(List<String> serverNames) {
        try {
            List<Integer> serverIds = new ArrayList<>();
            serverNames.forEach(serverName -> {
                serverIds.add(StringUtils.getInt(serverName));
            });

            StringBuilder searchArray = new StringBuilder();
            for (int i = 0; i < serverIds.size(); i++) {
                searchArray.append("?");
                if (i < serverIds.size() - 1) {
                    searchArray.append(",");
                }
            }
            String sql = "SELECT * FROM message WHERE server_id IN (" + searchArray.toString() + ")";
            System.out.println(sql);
            PreparedStatement statement = connection.prepareStatement(sql);
            for (int i = 0; i < serverIds.size(); i++) {
                System.out.println(serverIds.get(i));
                statement.setInt(i + 1, serverIds.get(i));
            }
            ResultSet resultSet = statement.executeQuery();
            List<String> messages = new ArrayList<>();
            while (resultSet.next()) {
                messages.add(resultSet.getString("message_string") + " " + resultSet.getString("timestamp"));
            }
            Collections.reverse(messages);
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


}
