package util;

import controller.players.PlayerController;

import java.util.List;

public class Config {

    private List<PlayerController> playerControllers;
    private boolean alternateFirstPlayer;
    private String endConditionType;
    private int endCondition;
    private boolean print;
    private boolean uploadToDatabase;
    private boolean exportToCSV;

    public Config() {
    }

    public Config(String endConditionType, int endCondition, boolean print, boolean uploadToDatabase, boolean exportToCSV) {
        this.endConditionType = endConditionType;
        this.endCondition = endCondition;
        this.print = print;
        this.uploadToDatabase = uploadToDatabase;
        this.exportToCSV = exportToCSV;
    }

    public List<PlayerController> getPlayerControllers() {
        return playerControllers;
    }

    public void setPlayerControllers(List<PlayerController> playerControllers) {
        this.playerControllers = playerControllers;
    }

    public boolean isAlternateFirstPlayer() {
        return alternateFirstPlayer;
    }

    public void setAlternateFirstPlayer(boolean alternateFirstPlayer) {
        this.alternateFirstPlayer = alternateFirstPlayer;
    }

    public String getEndConditionType() {
        return endConditionType;
    }

    public void setEndConditionType(String endConditionType) {
        this.endConditionType = endConditionType;
    }

    public int getEndConditionValue() {
        return endCondition;
    }

    public void setEndConditionValue(int endCondition) {
        this.endCondition = endCondition;
    }

    public boolean isPrint() {
        return print;
    }

    public void setPrint(boolean print) {
        this.print = print;
    }

    public boolean isUploadToDatabase() {
        return uploadToDatabase;
    }

    public void setUploadToDatabase(boolean uploadToDatabase) {
        this.uploadToDatabase = uploadToDatabase;
    }

    public boolean isExportToCSV() {
        return exportToCSV;
    }

    public void setExportToCSV(boolean exportToCSV) {
        this.exportToCSV = exportToCSV;
    }
}
