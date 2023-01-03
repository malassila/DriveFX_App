package com.pcsp.driveauditfx.server.FX.controller;

import com.pcsp.driveauditfx.LaunchServer;
import com.pcsp.driveauditfx.server.database.DriveServerDAO;
import com.pcsp.driveauditfx.server.database.HardDriveDAO;
import com.pcsp.driveauditfx.server.database.MessageDAO;
import com.pcsp.driveauditfx.server.messages.MessageHandler;
import com.pcsp.driveauditfx.shared.device.DriveModel;
import com.pcsp.driveauditfx.shared.device.ServerModel;
import com.pcsp.driveauditfx.shared.utils.StringUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.pcsp.driveauditfx.shared.utils.StringUtils.getInt;

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
    @FXML private ButtonBar btnBarTop;
    @FXML private ButtonBar btnBarBottom;
    @FXML private Button btnSelectAll;
    @FXML private TabPane tabPaneMain;
    @FXML private HBox hboxMain;
    private DriveServerDAO driveServerDAO;
    private MessageDAO messageDAO;
    private HardDriveDAO driveDAO;
    private MessageHandler messageHandler;
    private String defaultButtonStyle;
    private double xOffset = 0;
    private double yOffset = 0;

    // Other variables
    private ObservableList<ServerModel> serverModelList;
    private ObservableList<String> messageList;
    private ObservableList<DriveModel> driveModelList;
    public Scene scene;


        @FXML
        public void initialize() {
            // Initialize the MessageHandler and give it the instance of the JavaFX Application Thread
            // this allows it to call the updateUI() method to update the UI from a non-JavaFX thread
            messageHandler = new MessageHandler();
            messageHandler.setMainController(this);

            // Initialize the DAO objects to be connected to the methods in the message handler
            initializeDAO();

            // Initialize the button bar to be left aligned
//            initializeButtonBar();

            // Initialize the server model list and message list
            serverModelList = FXCollections.observableArrayList();
            messageList = FXCollections.observableArrayList();

            // Initialize the table columns
            initializeTableColumns();
            initializeTableViewDrive();

            // Initialize window dragging
            initializeWindowDragging();

            // Initialize the message list
            initializeMessageList();

            // Add an ENTER KEY event listener to the text field to search for a serial number
            txtSerialSearch.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    handleSearchAction(new ActionEvent());
                }
            });

//            get the window


//            tableView.prefWidthProperty().bind(paneMain.widthProperty());
//            listMessages.prefHeightProperty().bind(paneMain.heightProperty());
//            hboxMain.prefHeightProperty().bind(paneMain.heightProperty());
//            tabPaneMain.prefHeightProperty().bind(paneMain.heightProperty());
//            paneMain.prefHeightProperty().bind(paneMain.heightProperty());
//            btnBarTop.prefHeightProperty().bind(paneMain.heightProperty());
//            btnBarBottom.prefHeightProperty().bind(paneMain.heightProperty());

        }

        public void initializeWindowDragging() {
            Scene scene = tabPaneMain.getScene();

            // Set the mouse pressed event handler
            tabPaneMain.setOnMousePressed(event -> {
                tabPaneMain.setCursor(Cursor.CLOSED_HAND);
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });

            // Set the mouse dragged event handler
            tabPaneMain.setOnMouseDragged(event -> {
                tabPaneMain.getScene().getWindow().setX(event.getScreenX() - xOffset);
                tabPaneMain.getScene().getWindow().setY(event.getScreenY() - yOffset);
            });

            // Set the mouse released event handler
            tabPaneMain.setOnMouseReleased(event -> {
                tabPaneMain.setCursor(Cursor.OPEN_HAND);
            });
        }

    public void initializeDAO() {
        driveServerDAO = messageHandler.serverDAO();
        messageDAO = messageHandler.messageDAO();
        driveDAO = messageHandler.driveDAO();
    }
//    public void initializeButtonBar() {
////        btnBarTop.setButtonOrder(ButtonBar.BUTTON_ORDER_LINUX);
////        left align the buttons in the button bar
//        btnBarTop.setButtonData(btnBarTop.getButtons().get(0), ButtonBar.ButtonData.LEFT);
//        btnBarTop.setButtonData(btnBarTop.getButtons().get(1), ButtonBar.ButtonData.LEFT);
//
//
//    }

    private void initializeTableColumns() {
        colServerName.setCellValueFactory(new PropertyValueFactory<ServerModel, String>("serverName"));
        colServerStatus.setCellValueFactory(new PropertyValueFactory<ServerModel, String>("status"));
        colDrivesConnected.setCellValueFactory(new PropertyValueFactory<ServerModel, Integer>("numOfConnected"));
        colDrivesWiping.setCellValueFactory(new PropertyValueFactory<ServerModel, Integer>("numOfWiping"));
        colDrivesCompleted.setCellValueFactory(new PropertyValueFactory<ServerModel, Integer>("numOfCompleted"));
        colDrivesFailed.setCellValueFactory(new PropertyValueFactory<ServerModel, Integer>("numOfFailed"));

        colServerName.setComparator((server1, server2) -> {
            // Get the integer values of the server names
            int server1Int = StringUtils.getInt(server1);
            int server2Int = StringUtils.getInt(server2);
            // Compare the integer values
            return Integer.compare(server1Int, server2Int);
        });

        // Set all the servers to Disconnected in the db when the program starts
        driveServerDAO.resetConnectedServers();

        // Populate the table with data from the database
        ObservableList<ServerModel> serverData = FXCollections.observableArrayList(driveServerDAO.getAllServers());

        // Set the items for the table
        tableView.setItems(serverData);

        // Allow for multiple selections
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Create a row click listener for the table
        tableView.setRowFactory(tableView -> {
//                    btnSelectAll.setText("Select All");
//                    btnSelectAll.setStyle("-fx-background-color: lightgrey");
            TableRow<ServerModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    ServerModel rowData = row.getItem();
                    String serverName = rowData.getServerName();
                    System.out.println("Server Name>>: " + serverName);

                }
            });
            return row;
        });

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnSelectAll.setText("Select All");
            btnSelectAll.setStyle("-fx-background-color: lightgrey");
        });

        // Create a hover effect for the rows of the table
//        tableView.setRowFactory(tv -> {
//            TableRow<ServerModel> row = new TableRow<>();
//            row.setOnMouseEntered(event -> {
//                row.setStyle("-fx-background-color: green;");
//            });
//            row.setOnMouseExited(event -> {
//                row.setStyle("");
//            });
//            return row;
//        });


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

    public ObservableList<ServerModel> getSelectedList() {
        // Get the list of selected items
        ObservableList<ServerModel> selectedItems = tableView.getSelectionModel().getSelectedItems();
        return selectedItems;
    }

    @FXML
    private void handleWipeButton(ActionEvent event) {
        ObservableList<ServerModel> selectedItems = getSelectedList();
        // Make sure a server was selected
        if (selectedItems.size() > 0) {
            // Iterate through the selected items and perform any necessary processing
            for (ServerModel item : selectedItems) {
                // Perform processing on the selected item
                if (item.getStatus().equals("Connected")) {
                    // Send a message to the server to wipe the drive
                    messageHandler.sendWipeMessage(item.getServerName());
                    System.out.println(item.getServerName() + " will begin wiping");
                    messageHandler.messageDAO().insertMessage(getInt(item.getServerName()), item.getServerName() + " will begin wiping");
                    initializeMessageList();
                } else if (item.getStatus().equals("Wiping")) {
                    System.out.println(item.getServerName() + " is already wiping");
                    messageHandler.messageDAO().insertMessage(getInt(item.getServerName()), item.getServerName() + " is already wiping");
                    initializeMessageList();
                } else if (item.getStatus().equals("Disconnected")) {
                    System.out.println(item.getServerName() + " is disconnected");
                    messageHandler.messageDAO().insertMessage(getInt(item.getServerName()), item.getServerName() + " is disconnected");
                    initializeMessageList();
                }
            }
        } else {
            messageHandler.messageDAO().insertMessage(31, "Error: No server selected");
            initializeMessageList();
            System.out.println("No servers selected");
        }
    }
    @FXML
    private void handleSeeSelectedMessages(ActionEvent event) {
        List<ServerModel> selectedItems = getSelectedList();
        if (selectedItems.size() > 0) {
            List<String> serverNames = new ArrayList<>();
            // Iterate through the selected items and perform any necessary processing
            for (ServerModel item : selectedItems) {
                // Perform processing on the selected item
                System.out.println("Selected item: " + item.getServerName());
                serverNames.add(item.getServerName());
            }
            // reset the list view data
            messageList = null;

            // Query the database to get all the messages
            // Add the messages to the ObservableList
            messageList = FXCollections.observableArrayList(messageDAO.getSelectedServers(serverNames));

            listMessages.setItems(messageList);
            // Scroll to the bottom of the list
            int index = messageList.size() - 1;
            listMessages.scrollTo(index);
        } else {
            messageHandler.messageDAO().insertMessage(31, "Error: No server selected");
            initializeMessageList();
            System.out.println("No servers selected");
        }

    }

    @FXML
    private void handleSeeAllMessages(ActionEvent event) {
        initializeMessageList();
    }

    @FXML
    private void handleDriveMapButtonClick(ActionEvent event) throws IOException {
        // Get the selected row from the table view
        ServerModel selectedServer = tableView.getSelectionModel().getSelectedItem();
//        try {
//            selectedServer = driveServerDAO.getDriveServer(selectedServer.getServerName());
            System.out.println("ServerModel: " + selectedServer);
//        } catch (Exception e) {
//            System.out.println("Server not found");
//            e.printStackTrace();
//        }
        System.out.println("Selected server: " + selectedServer.getServerName());

        // Create a new stage for the drive map UI
        Stage stage = new Stage();
        stage.setTitle("Drive Map");

        // Load the FXML file for the drive map UI
//        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pcsp/driveauditfx/drive_map.fxml"));
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("drive_map.fxml"));
        Parent root = loader.load();

        // Get the controller for the drive map UI
        DriveMapController controller = loader.getController();

        // Pass the selected server and list of drives to the controller
        controller.setServer(selectedServer);
        controller.setDriveModels(driveServerDAO.getDrivesByServer(selectedServer.getServerName()));

        // Create a new scene and set it as the scene for the stage
        Scene scene = new Scene(root);
        stage.setScene(scene);

        // Show the stage
        stage.show();
    }


    @FXML
    private void handleSelectAllButton(ActionEvent event) {
//      unselect all if all are selected
        // Set the max width of the button to Double.MAX_VALUE to allow it to grow
        // if the text does not fit
        btnSelectAll.setMaxWidth(Double.MAX_VALUE);

        if (tableView.getSelectionModel().getSelectedItems().size() == tableView.getItems().size()) {
            tableView.getSelectionModel().clearSelection();
            btnSelectAll.setText("Select All");
            btnSelectAll.setStyle("-fx-background-color: lightgrey");
        } else {
            defaultButtonStyle = btnSelectAll.getStyle();
            tableView.getSelectionModel().selectAll();
//            change the color of the selected rows
            btnSelectAll.setText("Unselect All");
            btnSelectAll.setStyle("-fx-background-color: rgb(192, 255, 0)");


        }
    }

}
