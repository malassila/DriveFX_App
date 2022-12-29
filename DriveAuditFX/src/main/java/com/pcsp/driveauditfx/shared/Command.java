package com.pcsp.driveauditfx.shared;

public enum Command {
    ADD("ADD"),
    REMOVE("REMOVE"),
    UPDATE("UPDATE");


    private String command;
    Command(String command) {
        this.command = command;
    }


}
