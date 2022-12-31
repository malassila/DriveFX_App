package com.pcsp.driveauditfx.server.FX.controller;

import com.pcsp.driveauditfx.server.database.DriveServerDAO;
import com.pcsp.driveauditfx.server.database.ServerDAO;
import com.pcsp.driveauditfx.server.messages.MessageHandler;
import com.pcsp.driveauditfx.shared.device.ServerModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class MainController {
    @FXML private TableColumn<ServerModel, Integer> colDrivesCompleted;
    @FXML private TableColumn<ServerModel, Integer> colDrivesConnected;
    @FXML private TableColumn<ServerModel, Integer> colDrivesFailed;
    @FXML private TableColumn<ServerModel, Integer> colDrivesWiping;
    @FXML private TableColumn<ServerModel, String> colServerName;
    @FXML private TableColumn<ServerModel, String> colServerStatus;
    @FXML private ListView<String> listMessages;
    @FXML private AnchorPane paneMain;
    @FXML private Tab tabMain;
    @FXML private Tab tabSecond;
    @FXML private TableView<ServerModel> tableView;
    private DriveServerDAO driveServerDAO;
    private MessageHandler messageHandler;

    // Other variables
    private ObservableList<ServerModel> serverModelList;
    private ObservableList<String> messageList;

        @FXML
        public void initialize() {
            messageHandler = new MessageHandler();
            messageHandler.setMainController(this);
            driveServerDAO = messageHandler.serverDAO();


            // Initialize the server model list and message list
            serverModelList = FXCollections.observableArrayList();
            messageList = FXCollections.observableArrayList();



            // Initialize the table columns
            initializeTableColumns();
            // Initialize the message list
            initializeMessageList();
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
    }
    public void initializeMessageList() {
        // Query the database to get all the messages
//        messageList = messageDAO().getAllMessages();

        // Add the messages to the ObservableList
//        messages.addAll(messageList);
//        listMessages.setItems(messageList);
        // Scroll to the bottom of the list
//        int index = messages.size() - 1;
//        listMessages.scrollTo(index);
    }
    public void updateUI(ServerModel serverModel) {
        // Create a new task to update the UI
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                // Get the index of the server in the table view
                int index = tableView.getItems().indexOf(serverModel);
                // Update the server's data in the table view
                tableView.getItems().set(index, serverModel);
                return null;
            }
        };
        // Start the task
        new Thread(task).start();
    }
}
