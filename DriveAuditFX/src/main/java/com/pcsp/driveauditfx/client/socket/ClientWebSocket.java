package com.pcsp.driveauditfx.client.socket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class ClientWebSocket {// extends WebSocketClient implements ClientSocket {
//    private final String serverName;
//
//    public ClientWebSocket(String serverName) {
//        super(URI.create("ws://localhost:8887"));
//        this.serverName = serverName;
//    }
//
//    public void connect(String ip, int port) {
//        try {
//            URI uri = new URI("ws://" + ip + ":" + port);
//            this.connectBlocking(uri);
//        } catch (URISyntaxException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onOpen(ServerHandshake handshakedata) {
//        System.out.println("Connected to server");
//    }
//
//    @Override
//    public void onMessage(String message) {
//        System.out.println("Received message: " + message);
//    }
//
//    @Override
//    public void onClose(int code, String reason, boolean remote) {
//        System.out.println("Disconnected from server");
//    }
//
//    @Override
//    public void onError(Exception ex) {
//        ex.printStackTrace();
//    }
//
//    @Override
//    public void sendMessage(String message) {
//        this.send(message);
//    }
//
//    @Override
//    public String getServerName() {
//        return serverName;
//    }
}
