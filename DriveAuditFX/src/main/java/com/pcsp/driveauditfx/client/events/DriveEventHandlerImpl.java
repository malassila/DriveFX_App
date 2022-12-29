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
            DriveDataImpl driveData = new DriveDataImpl(driveName);
            Drive drive = driveData.getDriveData();
        try {
            clientSocket.sendAddDriveMessage(drive.getSlot(), drive.getSerial());
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        System.out.println(drive);
    }

    @Override
    public void handleDriveDisconnected(String driveName) {

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
