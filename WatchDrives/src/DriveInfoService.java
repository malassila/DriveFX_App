import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class DriveInfoService {

    public static void getFileData(String deviceHandle) {
        File smartFile = new File("/SMART/" + deviceHandle + ".txt");
        File driveInfoFile = new File("/SMART/" + deviceHandle + "Info.txt");
        try {
            BufferedReader smartReader = new BufferedReader(new FileReader(smartFile));
            BufferedReader infoReader = new BufferedReader(new FileReader(driveInfoFile));
            String currentLine;
            while ((currentLine = smartReader.readLine()) != null) {
                System.out.println(currentLine);
                processLine(currentLine);
            }
            while ((currentLine = infoReader.readLine()) != null) {
                System.out.println(currentLine);
                processLine(currentLine);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void processLine(String currentLine) {

    }
}
