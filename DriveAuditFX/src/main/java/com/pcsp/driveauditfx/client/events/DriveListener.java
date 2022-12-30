package com.pcsp.driveauditfx.client.events;


import com.pcsp.driveauditfx.client.socket.ClientSideSocket;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;

public class DriveListener implements Runnable {
    private ClientSideSocket clientSocket;
    private DriveEventHandler driveHandler;

    public DriveListener(ClientSideSocket clientSocket, DriveEventHandler driveHandler) {
        this.clientSocket = clientSocket;
        this.driveHandler = driveHandler;
        this.driveHandler.setClientSocket(clientSocket);
    }

    @Override
    public void run() {
        System.out.println("Initialized Drive Watcher: ");

        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            Path dirToWatch = Paths.get("/dev");
            dirToWatch.register(watchService, ENTRY_CREATE, ENTRY_DELETE);

            while (true) {
                WatchKey key = watchService.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> eventKind = event.kind();
                    if (eventKind == OVERFLOW) {
                        System.out.println("Event overflow occurred");
                        continue;
                    }
                    WatchEvent<Path> currEvent = (WatchEvent<Path>) event;
                    String driveHandle = String.valueOf(currEvent.context());
                    processEvent(eventKind, driveHandle);

                }
                // Reset the key
                boolean isKeyValid = key.reset();
                if (!isKeyValid) {
                    System.out.println("No longer watching " + dirToWatch);
                    break;
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();

        }
    }

    public void processEvent(WatchEvent.Kind<?> eventKind, String driveHandle) {
    try {
        if (eventKind == ENTRY_CREATE && driveHandle.contains("sd") && !driveHandle.matches("^.*\\d$")) {
            driveHandler.handleDriveConnected(driveHandle);
        }
        if (eventKind == ENTRY_DELETE && driveHandle.contains("sd") && !driveHandle.matches("^.*\\d$")) {
            driveHandler.handleDriveDisconnected(driveHandle);
        }
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (Exception e) {
        e.printStackTrace();
    }
    }
}
