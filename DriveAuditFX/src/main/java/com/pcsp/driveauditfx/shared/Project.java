package com.pcsp.driveauditfx.shared;

import com.pcsp.driveauditfx.shared.device.Drive;
import com.pcsp.driveauditfx.shared.device.DriveServer;
import com.pcsp.driveauditfx.shared.device.ServerModel;

import java.util.ArrayList;
import java.util.List;

public class Project {
    public static List<DriveServer> servers = new ArrayList<>();
    public static List<ServerModel> serverModels = new ArrayList<>();
    public static List<Drive> Drives = new ArrayList<>();
    public static final int PORT = 8093;

    public Project(){
        for(int i = 0; i < 30; i++){
            servers.add(new DriveServer("Server" + i,"Disconnected"));
            serverModels.add(new ServerModel("Server" + i, "Disconnected", 0, 0, 0,0));
        }
    }

    public static void addDrive(Drive drive){
        Drives.add(drive);
    }

    public static Drive getDrive(String driveName){
        return Drives.stream().filter(drive -> drive.getName().equals(driveName)).findFirst().orElse(null);
    }

    public static ServerModel getServerModel(String serverName){
        return serverModels.stream().filter(serverModel -> serverModel.getServerName().equals(serverName)).findFirst().orElse(null);
    }


}
