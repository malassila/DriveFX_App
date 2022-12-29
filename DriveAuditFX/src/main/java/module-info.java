module com.pcsp.driveauditfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires java.sql;
    requires jsch;


    opens com.pcsp.driveauditfx to javafx.fxml;
//    exports com.pcsp.driveauditfx;
    exports com.pcsp.driveauditfx.server.FX.controller;
//    opens com.pcsp.driveauditfx.server.FX to javafx.fxml;
//    exports com.pcsp.driveauditfx.server.FX;
    opens com.pcsp.driveauditfx.server.FX.controller to javafx.fxml;
    exports com.pcsp.driveauditfx;
}