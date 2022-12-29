package com.pcsp.driveauditfx.client.socket;

import com.pcsp.driveauditfx.client.messages.DriveMessageService;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import static com.pcsp.driveauditfx.shared.Command.ADD;
import static com.pcsp.driveauditfx.shared.Command.REMOVE;
import static com.pcsp.driveauditfx.shared.SharedMethods.closeEverything;
import static com.pcsp.driveauditfx.shared.Type.DRIVE;
import static com.pcsp.driveauditfx.shared.Type.SERVER;


public class ClientSideSocket implements Runnable{
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String serverName;

    public ClientSideSocket(Socket socket, String serverName) {
        try {
            this.socket = socket;
            this.serverName = serverName;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    void sendMessageToServer(String message) {
        try {
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public String listenForServerResponse() {

        String serverResponse = null;
        try {
            serverResponse = bufferedReader.readLine();
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
        return serverResponse;
    }

    public void sendAddDriveMessage(String slot, String serialNumber) throws UnknownHostException {
        String message = DriveMessageService.createMessage(DRIVE, serverName, ADD, slot, serialNumber, "ADD");
        sendMessageToServer(message);
    }
    public void sendRemoveDriveMessage(String slot, String serialNumber) throws UnknownHostException {
        String message = DriveMessageService.createMessage(DRIVE, serverName, REMOVE, slot, serialNumber, "ADD");
        sendMessageToServer(message);
    }



    @Override
    public void run() {
        String message = DriveMessageService.createMessage(SERVER, serverName, ADD, null, null, null);
        sendMessageToServer(message);
        listenForServerResponse();
    }
}
