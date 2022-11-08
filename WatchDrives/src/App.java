
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.WatchKey;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class App {

    private static BufferedReader bufferedReader;
    private static BufferedWriter bufferedWriter;
    private static ServerSocket client;
    private static DeviceCommandHandler deviceHandler;
    private static DateTimeFormatter date = DateTimeFormatter.ofPattern("HH:mm:ss - MM/dd");

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Start");
        String serverName = "Server 1";
        Thread t1 = new Thread(new Runnable() {
            Socket socket;

            @Override
            public void run() {
                try {

                    socket = new Socket("192.168.1.66", 8089);
                    client = new ServerSocket(socket, serverName);
                    client.sendResponse("MESSAGE: " + serverName + " has connected!");
                    bufferedWriter = new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream()));
                    bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });
        t1.start();

        try (WatchService service = FileSystems.getDefault().newWatchService()) {
            Map<WatchKey, Path> keyMap = new HashMap<>();
            Path path = Paths.get("/dev");
            keyMap.put(path.register(service,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE),
                    path);

            WatchKey watchKey;

            do {
                watchKey = service.take();
                Path eventDir = keyMap.get(watchKey);

                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    WatchEvent.Kind<?> kindOfEvent = event.kind();
                    WatchEvent.Kind<?> createEvent = StandardWatchEventKinds.ENTRY_CREATE;
                    Path linuxDeviceHandle = (Path) event.context();

                    if (String.valueOf(linuxDeviceHandle).contains("sd")) {

                        System.out.println(eventDir + ": " + kindOfEvent + ": " + linuxDeviceHandle);
                        System.out.println();

                        // Thread t = new Thread(new Runnable() {
                        // public void run() {
                        try {

                            LocalDateTime now = LocalDateTime.now();
                            String messagePrefix = date.format(now) + ": " + serverName + ": ";

                            if (kindOfEvent.equals(createEvent)) {
                                Drive drive = new Drive(String.valueOf(String.valueOf(linuxDeviceHandle)));
                                deviceHandler.testDrive(String.valueOf(String.valueOf(linuxDeviceHandle)));
                                client.sendResponse(messagePrefix + "Drive has been detected!");
                            } else {
                                client.sendResponse(messagePrefix + "Drive was removed!");
                            }
                            DriveInfoService.getFileData(String.valueOf(linuxDeviceHandle));

                            bufferedWriter.write(messagePrefix + "Wipe has completed");

                            bufferedWriter.newLine();

                            bufferedWriter.flush();

                        } catch (Exception e) {

                        }
                        // }
                        // });
                        // t.start();
                    }
                }
            } while (watchKey.reset());
        } catch (Exception e) {
        }
    }

    public void newWatchService() {

    }
}
