package com.pcsp.driveauditfx.client;

import java.net.Inet4Address;

public class ClientProperties {
    public static final String SERVER_NAME = "Server2";
    public static final String BOOT_DRIVE = "sda";

//    method to get the username of the local machine
    public static String getUserName() {
        return System.getProperty("user.name");
    }

    public static String getIpAddress() {
        return Inet4Address.getLoopbackAddress().getHostAddress();
    }
}
