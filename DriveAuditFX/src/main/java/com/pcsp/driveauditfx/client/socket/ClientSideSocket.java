package com.pcsp.driveauditfx.client.socket;

import com.pcsp.driveauditfx.shared.messages.DriveMessageService;
import com.pcsp.driveauditfx.shared.device.Drive;

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

/*        Type messageType,
        String serverName,
        String command,
        String name,
        String model,
        String serial,
        String type,
        String sectorSize,
        String size,
        String smartResult,
        String slot,
        String hours,
        String rsec,
        String spindleSpeed ,
        String status
*/
    public void sendAddDriveMessage(Drive drive) throws UnknownHostException {
        String message = String.join(" ",
                        "DRIVE",
                        serverName,
                        ADD.toString(),
                        drive.getSlot(),
                        drive.getName(),
                        drive.getModel(),
                        drive.getSerial(),
                        drive.getType(),
                        drive.getSectorSize(),
                        drive.getSize(),
                        drive.getSmartResult(),
                        drive.getHours(),
                        drive.getRsec(),
                        drive.getSpindleSpeed(),
                        drive.getStatus());

        sendMessageToServer(message);
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendMessageToServer("DRIVE "+serverName+" WIPING "+drive.getSlot() + " a a a a a");
        try {
            Thread.sleep(9000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendMessageToServer("DRIVE "+serverName+" COMPLETE "+drive.getSlot() + " a a a a a");
    }
    public void sendRemoveDriveMessage(String slot, String serialNumber) throws UnknownHostException {
        String message = String.join(" ", "DRIVE", serverName, "REMOVE", slot, serialNumber);
        sendMessageToServer(message);
    }



    @Override
    public void run() {
        String message = DriveMessageService.createMessage(SERVER, serverName, ADD, null, null, null);
        sendMessageToServer(message);
        listenForServerResponse();
    }
}
