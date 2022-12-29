package com.pcsp.driveauditfx.shared;

import com.pcsp.driveauditfx.shared.device.DriveServer;

import java.util.ArrayList;
import java.util.List;

public class Project {
    public static List<DriveServer> servers = new ArrayList<>();
    public static final int PORT = 8093;

    public Project(){
        for(int i = 0; i < 30; i++){
            servers.add(new DriveServer("Server" + i,"Disconnected"));
        }
    }
}
