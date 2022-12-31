package com.pcsp.driveauditfx.server.database;

import com.pcsp.driveauditfx.shared.device.Drive;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

    public class DriveDAO implements HardDriveDAO {
        private Connection connection;

        public DriveDAO(Connection connection) {
            this.connection = connection;
        }

        @Override
        public void insertHardDrive(Drive hardDrive){
            try {
                String sql = "INSERT INTO hard_drives (slot, name, model, serial, type, sector_size, size, smart_result, hours, rsec, spindle_speed, status, connected) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement statement = connection.prepareStatement(sql);

                statement.setString(1, hardDrive.getSlot());
                statement.setString(2, hardDrive.getName());
                statement.setString(3, hardDrive.getModel());
                statement.setString(4, hardDrive.getSerial());
                statement.setString(5, hardDrive.getType());
                statement.setString(6, hardDrive.getSectorSize());
                statement.setString(7, hardDrive.getSize());
                statement.setString(8, hardDrive.getSmartResult());
                statement.setString(9, hardDrive.getHours());
                statement.setString(10, hardDrive.getRsec());
                statement.setString(11, hardDrive.getSpindleSpeed());
                statement.setString(12, hardDrive.getStatus());
                statement.setBoolean(13, hardDrive.isConnected());

                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void updateStatus(String serial, String status) {
            try {

                String sql = "UPDATE hard_drives SET status = ? WHERE serial = ? AND status NOT IN ('Complete', 'Error Occurred')";

                PreparedStatement statement = connection.prepareStatement(sql);

                statement.setString(1, status);
                statement.setString(2, serial);

                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void updateSlot(String serial, String slot) {
            try {
                String sql = "UPDATE hard_drives SET slot = ? WHERE serial = ?";

                PreparedStatement statement = connection.prepareStatement(sql);
                // allow slot to be null if drive is not connected
                if (slot == null) {
                    statement.setNull(1, java.sql.Types.VARCHAR);
                } else {
                    statement.setString(1, slot);
                }
                statement.setString(2, serial);

                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void updateStatus(String serial, boolean connected) {
            try {
                String sql = "UPDATE hard_drives SET connected = ? WHERE serial = ?";

                PreparedStatement statement = connection.prepareStatement(sql);

                statement.setBoolean(1, connected);
                statement.setString(2, serial);

                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public Drive getHardDrive(String serial) throws SQLException {


            return null;
        }

        @Override
        public List<Drive> getAllHardDrives() throws SQLException {
            String sql = "SELECT * FROM hard_drives";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            List<Drive> drives = new ArrayList<>();
            while (resultSet.next()) {
                Drive drive = new Drive();
                drive.setSlot(resultSet.getString("slot"));
                drive.setName(resultSet.getString("name"));
                drive.setModel(resultSet.getString("model"));
                drive.setSerial(resultSet.getString("serial"));
                drive.setType(resultSet.getString("type"));
                drive.setSectorSize(resultSet.getString("sector_size"));
                drive.setSize(resultSet.getString("size"));
                drive.setSmartResult(resultSet.getString("smart_result"));
                drive.setHours(resultSet.getString("hours"));
                drive.setRsec(resultSet.getString("rsec"));
                drive.setSpindleSpeed(resultSet.getString("spindle_speed"));
                drive.setStatus(resultSet.getString("status"));
                drive.setConnected(resultSet.getBoolean("connected"));
                drives.add(drive);
            }

            return drives;
        }

        @Override
        public void deleteHardDrive(String serial) throws SQLException {

        }



    }
