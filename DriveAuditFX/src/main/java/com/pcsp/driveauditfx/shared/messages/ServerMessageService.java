package com.pcsp.driveauditfx.shared.messages;

import com.pcsp.driveauditfx.shared.device.DriveServer;

import static com.pcsp.driveauditfx.shared.Project.servers;

public class ServerMessageService {
    private String[] messageArray;
    private DriveServer server;
    private DriveMessageService driveMessageService;
    private ServerMessageService serverMessageService;

    /**
     * DRIVE, serverName, ADD
     * messageArray[0] = Message Type
     * messageArray[1] = Server Name
     * messageArray[2] = command
     */
    public String getType() {
        return messageArray[0];
    }

    public DriveServer getServer() {
        server = servers.stream().filter(s -> s.getServerName()
                        .equals(messageArray[1]))
                .findFirst().orElse(null);
        return server;
    }
    public String getCommand() {
        return messageArray[2];
    }

    public ServerMessageService() {
    }

    public ServerMessageService(String message) {


    }
}
