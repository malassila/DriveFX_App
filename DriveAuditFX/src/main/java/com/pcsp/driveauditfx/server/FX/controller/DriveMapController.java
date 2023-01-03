package com.pcsp.driveauditfx.server.FX.controller;

import com.pcsp.driveauditfx.shared.device.DriveModel;
import com.pcsp.driveauditfx.shared.device.ServerModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.List;

public class DriveMapController {

    @FXML private Button btnPrint;

    @FXML private Label lblCompleted;

    @FXML private Label lblConnected;

    @FXML private Label lblDriveStatus;

    @FXML private Label lblFailed;

    @FXML private Label lblModel;

    @FXML private Label lblSerial;

    @FXML private Label lblServerName;

    @FXML private Label lblServerStatus;

    @FXML private Label lblSize;

    @FXML private Label lblSmart;

    @FXML private Label lblWiping;

    @FXML private Button slot1;

    @FXML private Button slot10;

    @FXML private Button slot11;

    @FXML private Button slot12;

    @FXML private Button slot13;

    @FXML private Button slot14;

    @FXML private Button slot15;

    @FXML private Button slot16;

    @FXML private Button slot17;

    @FXML private Button slot18;

    @FXML private Button slot19;

    @FXML private Button slot2;

    @FXML private Button slot20;

    @FXML private Button slot21;

    @FXML private Button slot22;

    @FXML private Button slot23;

    @FXML private Button slot24;

    @FXML private Button slot3;

    @FXML private Button slot4;

    @FXML private Button slot5;

    @FXML private Button slot6;

    @FXML private Button slot7;

    @FXML private Button slot8;

    @FXML private Button slot9;
    private ServerModel serverModel;
    private List<DriveModel> driveModels;
    private DriveModel selectedDriveModel;


    @FXML
    public void initialize() {
        try {
            serverModel = new ServerModel();
            System.out.println("ServerModel: " + serverModel.getServerName());
            System.out.println("DriveMapController initialize");
            initializeServerModel();
            btnPrint.setOnAction(event -> {
                System.out.println("Print");
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void initializeServerModel() {
        lblServerName.setText(serverModel.getServerName());
        lblServerStatus.setText(serverModel.getStatus());
        lblConnected.setText(String.valueOf(serverModel.getNumOfConnected()));
        lblWiping.setText(String.valueOf(serverModel.getNumOfWiping()));
        lblFailed.setText(String.valueOf(serverModel.getNumOfFailed()));
        lblCompleted.setText(String.valueOf(serverModel.getNumOfCompleted()));
    }

    public ServerModel getServerModel() {
        return serverModel;
    }

    public void setServerModel(ServerModel serverModel) {
        this.serverModel = serverModel;
    }

    public List<DriveModel> getDriveModels() {
        return driveModels;
    }

    public void setDriveModels(List<DriveModel> driveModels) {
        this.driveModels = driveModels;
    }

    public DriveModel getSelectedDriveModel() {
        return selectedDriveModel;
    }

    public void setSelectedDriveModel(DriveModel selectedDriveModel) {
        this.selectedDriveModel = selectedDriveModel;
    }


    public void setServer(ServerModel selectedServer) {
        this.serverModel = selectedServer;
    }
}
