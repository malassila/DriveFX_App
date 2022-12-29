package com.pcsp.driveauditfx.client.events;

import com.pcsp.driveauditfx.client.socket.ClientSideSocket;

import java.io.FileNotFoundException;

public interface DriveEventHandler {
    void handleDriveConnected(String driveName) throws FileNotFoundException;
    void handleDriveDisconnected(String driveName);
    void handleDriveCompleted(String driveName);
    void handleDriveError(String driveName);
    void handleDriveRegistered(String driveName);
    void handleDriveWiping(String driveName);


    void setClientSocket(ClientSideSocket clientSocket);
}
