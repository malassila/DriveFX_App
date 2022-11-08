import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;

public class DeviceCommandHandler {
    private static String commandOutput;

    public String testDrive(String deviceHandle) {
        try {
            runCommand("testDrive " + deviceHandle);
        } catch (Exception e) {
            System.out.println("Error running testDrive script");
            commandOutput = "Error running testDrive script";
        }
        return commandOutput;
    }

    public String wipeDrive(String deviceHandle) {
        try {
            runCommand("wipeDrive " + deviceHandle);
        } catch (Exception e) {
            System.out.println("Error running wipeDrive script");
            commandOutput = "Error running wipeDrive script";
        }
        return commandOutput;
    }

    public String initializeDrive(String deviceHandle) {
        try {
            runCommand("removeDrive " + deviceHandle);
        } catch (Exception e) {
            System.out.println("Error running initializeDrive script");
            commandOutput = "Error running initializeDrive script";
        }
        return commandOutput;
    }

    public String removeDrive(String deviceHandle, String connectedSlot) {
        try {
            runCommand("removeDrive " + deviceHandle + " " + connectedSlot);
        } catch (Exception e) {
            System.out.println("Error running removeDrive script");
            commandOutput = "Error running removeDrive script";
        }
        return commandOutput;
    }

    public static void runCommand(String command) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                commandOutput = null;
                try {
                    Process commandProcess = Runtime.getRuntime()
                            .exec(command);
                    commandProcess.waitFor();
                    String output;
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(commandProcess.getInputStream()));
                    while ((output = br.readLine()) != null) {
                        System.out.println(output);
                        commandOutput = commandOutput + "\n" + output;
                    }

                    System.out.println("exit: " + commandProcess.exitValue());
                    commandProcess.destroy();
                } catch (Exception e) {

                }
            }
        });
    }
}
