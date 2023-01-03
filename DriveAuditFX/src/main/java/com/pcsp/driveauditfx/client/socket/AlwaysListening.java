package com.pcsp.driveauditfx.client.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static com.pcsp.driveauditfx.client.ClientProperties.SERVER_NAME;
import static com.pcsp.driveauditfx.shared.utils.StringUtils.getInt;

public class AlwaysListening {

    public static void wakeMeUp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Create a ServerSocket to listen for incoming connections
                    int port = 8086 + getInt(SERVER_NAME);
                    ServerSocket serverSocket = new ServerSocket(port);

                    while (true) {
                        // Accept an incoming connection
                        Socket clientSocket = serverSocket.accept();

                        // Get a BufferedReader to read messages from the client
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                        // Get a PrintWriter to send messages to the client
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                        // Read a message from the client
                        String message = in.readLine();

                        // Echo the message back to the client
                        out.println(message);

                        // Close the socket
                        clientSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

}
