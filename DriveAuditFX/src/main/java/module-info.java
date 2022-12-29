module com.pcsp.driveauditfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires java.sql;
    requires jsch;


    opens com.pcsp.driveauditfx.server to javafx.fxml;
    exports com.pcsp.driveauditfx.server;
    exports com.pcsp.driveauditfx.server.FX.controller;
    opens com.pcsp.driveauditfx.server.FX to javafx.fxml;
    opens com.pcsp.driveauditfx.server.FX.controller to javafx.fxml;
}