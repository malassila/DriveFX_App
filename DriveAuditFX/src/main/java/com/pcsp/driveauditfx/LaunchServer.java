package com.pcsp.driveauditfx;

import com.pcsp.driveauditfx.server.FX.controller.MainController;
import com.pcsp.driveauditfx.server.messages.MessageHandler;
import com.pcsp.driveauditfx.server.socket.SocketConnect;
import com.pcsp.driveauditfx.shared.Project;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class LaunchServer extends Application {
    private static MessageHandler messageHandler;
    private static MainController controller;
    public static boolean relaunch = false;
    private static String[] relaunchCommnds = {"relaunch", "restart", "reboot", "start", "run", "launch", "open", "execute", "fx"};

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LaunchServer.class.getResource("server-view.fxml"));
        Parent root = fxmlLoader.load();

        controller = fxmlLoader.getController();
        messageHandler.setMainController(controller);

        Scene scene = new Scene(root, 1016, 851);
        controller.scene = scene;
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("DriveAuditFX");
        stage.setScene(scene);
        stage.show();
    }
    //how do i do this but check if the UI is currently open already
    public static void listenForUIRelaunch() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    String input = scanner.nextLine();
                    // if it equals any value in a List of strings
                    if (input != null && input.length() > 0) {
                        for (String command : relaunchCommnds) {
                            if (input.toLowerCase().contains(command)) {
                                System.out.println("Relaunching UI...");
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        launch();
                                    }
                                });
                            }
                        }
                    }
                }
            }
        }).start();
    }
    public static void main(String[] args) throws IOException {
        if (!relaunch) {
            Project project = new Project();
            ServerSocket serverSocket = new ServerSocket(project.PORT);
            messageHandler = new MessageHandler();
            SocketConnect socketConnect = new SocketConnect(serverSocket, messageHandler);
            socketConnect.startSocket();
            listenForUIRelaunch();
        }

        // this boolean checks if the application is already running and if it is, it will only launch the UI
        relaunch = true;
        // Launch the JavaFX application
        launch();
    }


}




