package com.pcsp.driveauditfx.server.FX.controller;

import com.pcsp.driveauditfx.server.database.DriveServerDAO;
import com.pcsp.driveauditfx.server.database.MessageDAO;
import com.pcsp.driveauditfx.server.database.ServerDAO;
import com.pcsp.driveauditfx.server.messages.MessageHandler;
import com.pcsp.driveauditfx.shared.device.DriveModel;
import com.pcsp.driveauditfx.shared.device.ServerModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

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
    @FXML private TableColumn<DriveModel, String> colFailed;

    @FXML private ListView<String> listMessages;
    @FXML private AnchorPane paneMain;
    @FXML private Tab tabMain;
    @FXML private Tab tabSecond;
    @FXML private TableView<ServerModel> tableView;
    private DriveServerDAO driveServerDAO;
    private MessageDAO messageDAO;
    private MessageHandler messageHandler;

    // Other variables
    private ObservableList<ServerModel> serverModelList;
    private ObservableList<String> messageList;

        @FXML
        public void initialize() {
            messageHandler = new MessageHandler();
            messageHandler.setMainController(this);
            driveServerDAO = messageHandler.serverDAO();
            messageDAO = messageHandler.messageDAO();


            // Initialize the server model list and message list
            serverModelList = FXCollections.observableArrayList();
            messageList = FXCollections.observableArrayList();



            // Initialize the table columns
            initializeTableColumns();
            // Initialize the message list
            initializeMessageList();
        }

    private void initializeTableColumns() {
            System.out.println(Thread.currentThread().getName());
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
            System.out.println("Updating UI");
            System.out.println(Thread.currentThread().getName());
            // Get the index of the server in the table view
            System.out.println("Server model: " + serverModel.getServerName());
//            get the index based on the serverModel name
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
    // model, serial, type, sector_size, size, smart_result, hours, rsec, spindle_speed, status

}
