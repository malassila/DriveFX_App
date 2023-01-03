package com.pcsp.driveauditfx.shared.device;

import javafx.beans.property.*;

public class ServerModel {
    private StringProperty serverName;
    private StringProperty status;
    private IntegerProperty numOfConnected;
    private IntegerProperty numOfWiping;
    private IntegerProperty numOfCompleted;
    private IntegerProperty numOfFailed;


    public ServerModel(String serverName, String status, int numOfConnected, int numOfWiping, int numOfCompleted, int numOfFailed) {
        this.serverName = new SimpleStringProperty(serverName);
        this.status = new SimpleStringProperty(status);
        this.numOfConnected = new SimpleIntegerProperty(numOfConnected);
        this.numOfWiping = new SimpleIntegerProperty(numOfWiping);
        this.numOfCompleted = new SimpleIntegerProperty(numOfCompleted);
        this.numOfFailed = new SimpleIntegerProperty(numOfFailed);
    }

    public ServerModel() {

    }

    public String getServerName() {
        return serverName.get();
    }

    public StringProperty serverNameProperty() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName.set(serverName);
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public int getNumOfConnected() {
        return numOfConnected.get();
    }

    public IntegerProperty numOfConnectedProperty() {
        return numOfConnected;
    }

    public void setNumOfConnected(int numOfConnected) {
        this.numOfConnected.set(numOfConnected);
    }

    public int getNumOfWiping() {
        return numOfWiping.get();
    }

    public IntegerProperty numOfWipingProperty() {
        return numOfWiping;
    }

    public void setNumOfWiping(int numOfWiping) {
        this.numOfWiping.set(numOfWiping);
    }

    public int getNumOfCompleted() {
        return numOfCompleted.get();
    }

    public IntegerProperty numOfCompletedProperty() {
        return numOfCompleted;
    }

    public void setNumOfCompleted(int numOfCompleted) {
        this.numOfCompleted.set(numOfCompleted);
    }

    public int getNumOfFailed() {
        return numOfFailed.get();
    }

    public void setNumOfFailed(int numOfFailed) {
        this.numOfFailed.set(numOfFailed);
    }

    @Override
    public String toString() {
        return "ServerModel{" +
                "serverName=" + serverName +
                ", status=" + status +
                ", numOfConnected=" + numOfConnected +
                ", numOfWiping=" + numOfWiping +
                ", numOfCompleted=" + numOfCompleted +
                ", numOfFailed=" + numOfFailed +
                '}';
    }
}
