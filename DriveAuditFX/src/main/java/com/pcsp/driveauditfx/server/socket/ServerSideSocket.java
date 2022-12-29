package com.pcsp.driveauditfx.server.socket;

import com.pcsp.driveauditfx.server.messages.MessageHandler;
import com.pcsp.driveauditfx.shared.SharedMethods;
import com.pcsp.driveauditfx.shared.SocketProperties;
import com.pcsp.driveauditfx.shared.device.DriveServer;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;
import java.util.List;

import static com.pcsp.driveauditfx.shared.SharedMethods.closeEverything;
import static com.pcsp.driveauditfx.shared.SocketProperties.SERVER_PORT;


public class ServerSideSocket implements Runnable {
    public static List<DriveServer> driveServers;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private MessageHandler messageHandler;
    public ServerSideSocket(Socket socket) {
        try {
            this.socket = socket;
            this.messageHandler = new MessageHandler();
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Server has been connected successfully!");
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        String serverResponse;
        try {
            while (socket.isConnected()) {
                serverResponse = bufferedReader.readLine();
                if (serverResponse != null) {
                    System.out.println("Server Response: " + serverResponse);
                    messageHandler.processRawMessage(serverResponse);
                }
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
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

