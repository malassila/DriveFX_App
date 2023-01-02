package com.pcsp.driveauditfx.client;

import com.pcsp.driveauditfx.client.events.DriveEventHandlerImpl;
import com.pcsp.driveauditfx.client.socket.ClientSideSocket;

import java.io.IOException;
import java.net.Socket;

import static com.pcsp.driveauditfx.client.ClientProperties.SERVER_NAME;
import static com.pcsp.driveauditfx.shared.SocketProperties.SERVER_IP;
import static com.pcsp.driveauditfx.shared.SocketProperties.SERVER_PORT;

public class LaunchClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(SERVER_IP, SERVER_PORT);
        ClientSideSocket clientSideSocket = new ClientSideSocket(socket, SERVER_NAME);
        Thread thread = new Thread(clientSideSocket);
        thread.start();
        DriveEventHandlerImpl driveEventHandler = new DriveEventHandlerImpl();
        driveEventHandler.setClientSocket(clientSideSocket);
        driveEventHandler.handleDriveConnected("sdc");
    }
}


//public class LaunchClient {
//    public static void main(String[] args) {
//        ClientWebSocket clientWebSocket = new ClientWebSocket(SERVER_NAME);
//        clientWebSocket.connect(SERVER_IP, SERVER_PORT);
//        DriveEventHandlerImpl driveEventHandler = new DriveEventHandlerImpl();
//        driveEventHandler.setClientSocket(clientWebSocket);
//        driveEventHandler.handleDriveConnected("sdc");
//    }
//}
