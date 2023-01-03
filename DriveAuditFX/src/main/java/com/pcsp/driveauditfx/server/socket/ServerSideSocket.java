package com.pcsp.driveauditfx.server.socket;

import com.jcraft.jsch.Session;
import com.pcsp.driveauditfx.server.messages.MessageHandler;
import com.pcsp.driveauditfx.shared.SharedMethods;
import com.pcsp.driveauditfx.shared.SocketProperties;
import com.pcsp.driveauditfx.shared.device.DriveServer;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pcsp.driveauditfx.shared.SharedMethods.closeEverything;
import static com.pcsp.driveauditfx.shared.SocketProperties.SERVER_PORT;


public class ServerSideSocket implements Runnable {
    public static List<DriveServer> driveServers;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private MessageHandler messageHandler;
    private DriveServer driveServer;
    private String serverName;
    private Session session;
    // Map to store the Sessions for each connected client
    private Map<String, Session> clients;
    public ServerSideSocket(Socket socket, MessageHandler messageHandler) {
        try {
            this.socket = socket;
            this.messageHandler = messageHandler;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.driveServers = new ArrayList<>();
            this.session = session;
            clients = new HashMap<>();
            System.out.println("Server has been connected successfully!");
        } catch (IOException e) {
            System.out.println("Server has not been connected successfully!");
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
                    if (serverResponse.contains("SERVER") && serverResponse.contains("ADD")) {
                        String[] messageArray = serverResponse.split(" ");
                        serverName = messageArray[1];
                    }
                    messageHandler.processRawMessage(serverResponse, this);
                } else {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                }
            }
        } catch (IOException e) {
            System.out.println("Disconnected from server");
            messageHandler.processRawMessage("SERVER "+serverName+" REMOVE", this);
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage(String message) {
        try {
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
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

    // Method to send a message to a particular client
    public void sendMessageToClient(String clientId, String message) throws IOException {
        bufferedWriter.write(message);
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

