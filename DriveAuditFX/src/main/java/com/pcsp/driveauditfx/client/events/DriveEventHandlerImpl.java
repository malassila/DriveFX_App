package com.pcsp.driveauditfx.client.events;

import com.pcsp.driveauditfx.client.linux.DriveData;
import com.pcsp.driveauditfx.client.linux.DriveDataImpl;
import com.pcsp.driveauditfx.client.socket.ClientSideSocket;
import com.pcsp.driveauditfx.shared.device.Drive;

import java.io.FileNotFoundException;
import java.net.UnknownHostException;

public class DriveEventHandlerImpl implements DriveEventHandler {
    private DriveController driveController;
    private ClientSideSocket clientSocket;

    @Override
    public void setClientSocket(ClientSideSocket clientSocket) {
        this.clientSocket = clientSocket;
    }
    @Override
    public void handleDriveConnected(String driveName) throws FileNotFoundException {
        System.out.println("Drive connected: " + driveName);
            DriveDataImpl driveData = new DriveDataImpl(driveName);
            Drive drive = driveData.getDriveData();
            System.out.println(drive);
        try {
            clientSocket.sendAddDriveMessage(drive);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleDriveDisconnected(String driveName) {
        System.out.println("Drive disconnected: " + driveName);
//        try {
//            clientSocket.sendRemoveDriveMessage(driveName);
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public void handleDriveCompleted(String driveName) {

    }

    @Override
    public void handleDriveError(String driveName) {

    }

    @Override
    public void handleDriveRegistered(String driveName) {

    }

    @Override
    public void handleDriveWiping(String driveName) {

    }

}
