package com.pcsp.driveauditfx.server.socket;

import com.pcsp.driveauditfx.server.messages.MessageHandler;
import com.pcsp.driveauditfx.shared.SharedMethods;
import com.pcsp.driveauditfx.shared.SocketProperties;
import com.pcsp.driveauditfx.shared.device.DriveServer;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static com.pcsp.driveauditfx.shared.SharedMethods.closeEverything;
import static com.pcsp.driveauditfx.shared.SocketProperties.SERVER_PORT;


public class ServerSideSocket implements Runnable {
    public static List<DriveServer> driveServers;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private MessageHandler messageHandler;
    private DriveServer driveServer;
    public ServerSideSocket(Socket socket, MessageHandler messageHandler) {
        try {
            this.socket = socket;
            this.messageHandler = messageHandler;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.driveServers = new ArrayList<>();
            System.out.println("Server has been connected successfully!");
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        String serverResponse;
        try {
            while (socket.isConnected() || bufferedReader.ready()) {
                serverResponse = bufferedReader.readLine();
                if (serverResponse != null) {
//                    System.out.println("Server Response: " + serverResponse);
                    messageHandler.processRawMessage(serverResponse, this);
                } else {
                    messageHandler.processServerMessage();
                    closeEverything(socket, bufferedReader, bufferedWriter);
                }
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public DriveServer getDriveServer() {
        return driveServer;
    }

    public void setDriveServer(DriveServer driveServer) {
        this.driveServer = driveServer;
    }

    public static void addDriveServer(DriveServer driveServer) {
        driveServers.add(driveServer);
    }

    public void removeDriveServer(DriveServer driveServer) {
        driveServers.remove(driveServer);
    }

    //    public void sendMessageToUI(String message, String ip) {
//        try {
//            Socket singleSocket = new Socket(ip, SERVER_PORT);
//            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(singleSocket.getOutputStream()));
//            bufferedWriter.write(message);
//            bufferedWriter.flush();
//            bufferedWriter.close();
//            singleSocket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}

