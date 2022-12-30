package com.pcsp.driveauditfx;

import com.pcsp.driveauditfx.server.socket.SocketConnect;
import com.pcsp.driveauditfx.shared.Project;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;

public class LaunchServer extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LaunchServer.class.getResource("server-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1016, 646);
        stage.setTitle("DriveAuditFX");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException {
        Project project = new Project();
        ServerSocket serverSocket;
        serverSocket = new ServerSocket(project.PORT);

        SocketConnect socketConnect = new SocketConnect(serverSocket);
        socketConnect.startSocket();

        launch();
    }
}