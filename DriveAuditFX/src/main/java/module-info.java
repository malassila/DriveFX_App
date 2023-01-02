module com.pcsp.driveauditfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires java.sql;
    requires jsch;
    requires Java.WebSocket;


    opens com.pcsp.driveauditfx to javafx.fxml;
    opens com.pcsp.driveauditfx.shared.device to javafx.base;
//    exports com.pcsp.driveauditfx;
    exports com.pcsp.driveauditfx.server.FX.controller;
//    opens com.pcsp.driveauditfx.server.FX to javafx.fxml;
//    exports com.pcsp.driveauditfx.server.FX;
    opens com.pcsp.driveauditfx.server.FX.controller to javafx.fxml;
    exports com.pcsp.driveauditfx;
}