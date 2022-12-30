package com.pcsp.driveauditfx.server.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketConnect {
    private ServerSocket serverSocket;

    public SocketConnect(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    public void startSocket() {
        new Thread(() -> {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("Client connected");
                    new Thread(new ServerSideSocket(socket)).start();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
//                    System.out.println("Client disconnected");
                }
            }
        }).start();

//        System.out.println("Server started");
//        try {
//            while (!serverSocket.isClosed()) {
//                Socket socket = serverSocket.accept();
//
//                ServerSideSocket serverSocket = new ServerSideSocket(socket);
//                Thread thread = new Thread(serverSocket);
//                thread.start();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
