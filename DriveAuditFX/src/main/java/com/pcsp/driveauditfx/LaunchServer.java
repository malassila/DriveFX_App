package com.pcsp.driveauditfx;

import com.pcsp.driveauditfx.server.FX.controller.MainController;
import com.pcsp.driveauditfx.server.messages.MessageHandler;
import com.pcsp.driveauditfx.server.socket.SocketConnect;
import com.pcsp.driveauditfx.shared.Project;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;

public class LaunchServer extends Application {
    private static MessageHandler messageHandler;
    private static MainController controller;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LaunchServer.class.getResource("server-view.fxml"));
        Parent root = fxmlLoader.load();

        controller = fxmlLoader.getController();
        messageHandler.setMainController(controller);

        Scene scene = new Scene(root, 1016, 646);
        stage.setTitle("DriveAuditFX");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException {
        Project project = new Project();
        ServerSocket serverSocket = new ServerSocket(project.PORT);
        messageHandler = new MessageHandler();
        SocketConnect socketConnect = new SocketConnect(serverSocket, messageHandler);
        socketConnect.startSocket();

        // Launch the JavaFX application
        launch();
    }
}




