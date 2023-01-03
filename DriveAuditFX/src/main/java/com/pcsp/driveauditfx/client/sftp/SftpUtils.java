package com.pcsp.driveauditfx.client.sftp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.FileInputStream;

public class SftpUtils {
    public static void transferFile(String localFile, String remoteFile) throws Exception {
//        JSch jsch = new JSch();
//        Session session = jsch.getSession(user, host, port);
//        session.setPassword(password);
//        session.setConfig("StrictHostKeyChecking", "no");
//        session.connect();
//
//        ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
//        channel.connect();
//
//        // Transfer the file
//        FileInputStream fis = new FileInputStream(localFile);
//        channel.put(fis, remoteFile);
//        fis.close();
//
//        channel.disconnect();
//        session.disconnect();
    }
}
