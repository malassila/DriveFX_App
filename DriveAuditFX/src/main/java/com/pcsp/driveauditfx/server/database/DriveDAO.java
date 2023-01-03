package com.pcsp.driveauditfx.server.database;

import com.pcsp.driveauditfx.shared.device.Drive;
import com.pcsp.driveauditfx.shared.device.DriveModel;
import com.pcsp.driveauditfx.shared.utils.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.pcsp.driveauditfx.shared.utils.StringUtils.getInt;


public class DriveDAO implements HardDriveDAO {
        private Connection connection;

        public DriveDAO(Connection connection) {
            this.connection = connection;
        }

        @Override
        public void insertHardDrive(Drive hardDrive, String serverName) {
            try {
                String sql = "INSERT INTO hard_drive (slot, name, model, serial, type, sector_size, size, smart_result, hours, rsec, spindle_speed, status, server_id) " +
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
                statement.setInt(13, getInt(serverName));

                statement.executeUpdate();
            } catch (SQLIntegrityConstraintViolationException e) {
                System.out.println("Drive already exists");
                updateStatus(hardDrive.getSerial(), "CONNECTED");
                updateSlot(hardDrive.getSerial(), hardDrive.getSlot());
                updateDriveName(hardDrive.getSerial(), hardDrive.getName());
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void updateStatus(String serial, String status) {
            try {
//                System.out.println("Updating status of drive " + serial + " to " + status);
                String sql = "UPDATE hard_drive SET status = ? WHERE serial = ? AND status NOT IN ('Complete', 'Error Occurred')";

                PreparedStatement statement = connection.prepareStatement(sql);

                statement.setString(1, status);
                statement.setString(2, serial);

                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        public void updateDriveName(String serial, String name) {
            try {
//                System.out.println("Updating drive name");
                String sql = "UPDATE hard_drive SET name = ? WHERE serial = ? AND status NOT IN ('Complete', 'Error Occurred')";

                PreparedStatement statement = connection.prepareStatement(sql);

                statement.setString(1, name);
                statement.setString(2, serial);

                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void updateSlot(String serial, String slot) {
            try {
                System.out.println("Updating slot");
                String sql = "UPDATE hard_drive SET slot = ? WHERE serial = ?";

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
                System.out.println("Updating status");
                String sql = "UPDATE hard_drive SET connected = ? WHERE serial = ?";

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
            String sql = "SELECT * FROM hard_drive";

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
                drives.add(drive);
            }

            return drives;
        }

        @Override
        public List<DriveModel> getDrivesByStatus(String status) throws SQLException {

            return null;
        }

        @Override
        public List<DriveModel> getDrivesBySerial(String serial) throws SQLException {
            try {
                String sql = "SELECT model, serial, size, smart_result, status FROM hard_drive WHERE serial like concat('%', ?, '%')";
                PreparedStatement statement = connection.prepareStatement(sql);

                statement.setString(1, serial);
                ResultSet resultSet = statement.executeQuery();

                List<DriveModel> drives = new ArrayList<>();

                while (resultSet.next()) {
                    System.out.println("Found drive");

                    String model = resultSet.getString("model");
                    String serialNumber = resultSet.getString("serial");
                    String size = resultSet.getString("size");
                    String smartResult = resultSet.getString("smart_result");
                    String status = resultSet.getString("status");
                    System.out.println(model + " " + serialNumber + " " + size + " " + smartResult + " " + status);
                    DriveModel drive = new DriveModel(model, serialNumber, size, smartResult, status.toUpperCase());
                    drives.add(drive);
                }

                return drives;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return new ArrayList<>();
        }

        @Override
        public List<DriveModel> getDrivesLike(String keyword) throws SQLException {
            return null;
        }

        @Override
        public void deleteHardDrive(String serial) throws SQLException {

        }



    }
