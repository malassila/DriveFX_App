package com.pcsp.driveauditfx.server.messages;

import com.pcsp.driveauditfx.server.socket.ServerSideSocket;
import com.pcsp.driveauditfx.shared.Command;
import com.pcsp.driveauditfx.shared.device.DriveServer;

import java.util.List;

public interface Message {
    String getType();
    String getMessage();
    DriveServer getServer();
    String[] splitMessage(String message);
    String getSlot();
    String getCommand();
    String getSerialNumber();


    void processRawMessage(String message, ServerSideSocket serverSideSocket);

}
