package com.pcsp.driveauditfx.server.FX.controller;

import com.pcsp.driveauditfx.server.database.DriveServerDAO;
import com.pcsp.driveauditfx.server.database.HardDriveDAO;
import com.pcsp.driveauditfx.server.database.MessageDAO;
import com.pcsp.driveauditfx.server.messages.MessageHandler;
import com.pcsp.driveauditfx.shared.device.DriveModel;
import com.pcsp.driveauditfx.shared.device.ServerModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.util.List;

public class MainController {
    @FXML private TableColumn<ServerModel, Integer> colDrivesCompleted;
    @FXML private TableColumn<ServerModel, Integer> colDrivesConnected;
    @FXML private TableColumn<ServerModel, Integer> colDrivesFailed;
    @FXML private TableColumn<ServerModel, Integer> colDrivesWiping;
    @FXML private TableColumn<ServerModel, String> colServerName;
    @FXML private TableColumn<ServerModel, String> colServerStatus;
    // model, serial, size, smart_result, status, failed
    @FXML private TableColumn<DriveModel, String> colModel;
    @FXML private TableColumn<DriveModel, String> colSerial;
    @FXML private TableColumn<DriveModel, String> colSize;
    @FXML private TableColumn<DriveModel, String> colSmart;
    @FXML private TableColumn<DriveModel, String> colStatus;
//    @FXML private TableColumn<DriveModel, String> colFailed;

    @FXML private ListView<String> listMessages;
    @FXML private AnchorPane paneMain;
    @FXML private Tab tabMain;
    @FXML private Tab tabSecond;
    @FXML private TableView<ServerModel> tableView;
    @FXML private TableView<DriveModel> tableViewDrive;
    @FXML private TextField txtSerialSearch;
    private DriveServerDAO driveServerDAO;
    private MessageDAO messageDAO;
    private HardDriveDAO driveDAO;
    private MessageHandler messageHandler;

    // Other variables
    private ObservableList<ServerModel> serverModelList;
    private ObservableList<String> messageList;
    private ObservableList<DriveModel> driveModelList;

        @FXML
        public void initialize() {
            // Initialize the MessageHandler and give it the instance of the JavaFX Application Thread
            // this allows it to call the updateUI() method to update the UI from a non-JavaFX thread
            messageHandler = new MessageHandler();
            messageHandler.setMainController(this);

            // Initialize the DAO objects to be connected to the methods in the message handler
            initializeDAO();

            // Initialize the server model list and message list
            serverModelList = FXCollections.observableArrayList();
            messageList = FXCollections.observableArrayList();

            // Initialize the table columns
            initializeTableColumns();
            initializeTableViewDrive();
            // Initialize the message list
            initializeMessageList();
        }

    public void initializeDAO() {
        driveServerDAO = messageHandler.serverDAO();
        messageDAO = messageHandler.messageDAO();
        driveDAO = messageHandler.driveDAO();
    }

    private void initializeTableColumns() {
        colServerName.setCellValueFactory(new PropertyValueFactory<ServerModel, String>("serverName"));
        colServerStatus.setCellValueFactory(new PropertyValueFactory<ServerModel, String>("status"));
        colDrivesConnected.setCellValueFactory(new PropertyValueFactory<ServerModel, Integer>("numOfConnected"));
        colDrivesWiping.setCellValueFactory(new PropertyValueFactory<ServerModel, Integer>("numOfWiping"));
        colDrivesCompleted.setCellValueFactory(new PropertyValueFactory<ServerModel, Integer>("numOfCompleted"));
        colDrivesFailed.setCellValueFactory(new PropertyValueFactory<ServerModel, Integer>("numOfFailed"));


        // Populate the table with data from the database
        ObservableList<ServerModel> serverData = FXCollections.observableArrayList(driveServerDAO.getAllServers());

        // Set the items for the table
        tableView.setItems(serverData);

        // Create a row click listener for the table
        tableView.setRowFactory(tableView -> {
            TableRow<ServerModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    ServerModel rowData = row.getItem();
                    String serverName = rowData.getServerName();

                    System.out.println("Server Name: " + serverName);

                }
            });
            return row;
        });

        // Create a hover effect for the rows of the table
        tableView.setRowFactory(tv -> {
            TableRow<ServerModel> row = new TableRow<>();
            row.setOnMouseEntered(event -> {
                row.setStyle("-fx-background-color: lightgray;");
            });
            row.setOnMouseExited(event -> {
                row.setStyle("");
            });
            return row;
        });
    }

    private void initializeTableViewDrive() {
        // Set the cell value factories for each column in the table view
        colModel.setCellValueFactory(new PropertyValueFactory<DriveModel, String>("model"));
        colSerial.setCellValueFactory(new PropertyValueFactory<DriveModel, String>("serial"));
        colSize.setCellValueFactory(new PropertyValueFactory<DriveModel, String>("size"));
        colSmart.setCellValueFactory(new PropertyValueFactory<DriveModel, String>("smart"));
        colStatus.setCellValueFactory(new PropertyValueFactory<DriveModel, String>("status"));

        // Set the items for the table view
        tableViewDrive.setItems(driveModelList);
    }

    public void initializeMessageList() {

            messageList = null;
        // Query the database to get all the messages
        // Add the messages to the ObservableList
        messageList = FXCollections.observableArrayList(messageDAO.getAllMessages());
        listMessages.setItems(messageList);
        // Scroll to the bottom of the list
        int index = messageList.size() - 1;
        listMessages.scrollTo(index);


    }

    public void updateUI(ServerModel serverModel) {
        Platform.runLater(() -> {
            // Get the index of the server in the table view
            String name = serverModel.getServerName();
            // update the messages
            initializeMessageList();

            // loop through the table view items and search for the name
            for (int i = 0; i < tableView.getItems().size(); i++) {
                System.out.println("Server name: " + tableView.getItems().get(i).getServerName());
                if (tableView.getItems().get(i).getServerName().equals(name)) {
                    // Update the server's data in the table view
                    System.out.println("Updating server: " + name);
                    System.out.println("ServerModel: " + serverModel);
                    tableView.getItems().set(i, serverModel);
                    break;
                }
            }

        });
    }

    @FXML
    private void handleSearchAction(ActionEvent event) {
        try {
            String serial = txtSerialSearch.getText();
            // Query the database for the drive with the specified serial
            driveModelList = FXCollections.observableArrayList(driveDAO.getDrivesBySerial(serial));

            // Clear the table view
            if (tableViewDrive.getItems() != null) {
                tableViewDrive.getItems().clear();
            }
            // Add the search result to the table view
            tableViewDrive.setItems(driveModelList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
