package com.pcsp.driveauditfx.server.socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;


import static com.pcsp.driveauditfx.shared.utils.StringUtils.getInt;

public class WakeUpClient {
    public static void wakeUpClient(String host, String message, String serverName) {
        try {
            int port = 8086 + getInt(serverName);
            // Connect to the server socket
            Socket socket = new Socket(host, port);

            // Get a PrintWriter to send messages to the server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Send a message to the server
            out.println(message);

            // Close the socket
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
