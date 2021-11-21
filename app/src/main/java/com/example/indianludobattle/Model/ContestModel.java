package com.example.indianludobattle.Model;

public class ContestModel {
    private String gameId,contestId,contestTitle,gameEntry,gamePricePool,totalJoined,useBonus,showStatus,time;
    private boolean joinedOrNot;
    public ContestModel(String gameId, String contestId, String contestTitle, String gameEntry, String gamePricePool, String totalJoined, String useBonus, String showStatus, String time,boolean joinedOrNot) {
        this.gameId = gameId;
        this.contestId = contestId;
        this.contestTitle = contestTitle;
        this.gameEntry = gameEntry;
        this.gamePricePool = gamePricePool;
        this.totalJoined = totalJoined;
        this.useBonus = useBonus;
        this.showStatus = showStatus;
        this.time = time;
        this.joinedOrNot=joinedOrNot;
    }

    public boolean isJoinedOrNot() {
        return joinedOrNot;
    }

    public void setJoinedOrNot(boolean joinedOrNot) {
        this.joinedOrNot = joinedOrNot;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getContestId() {
        return contestId;
    }

    public void setContestId(String contestId) {
        this.contestId = contestId;
    }

    public String getContestTitle() {
        return contestTitle;
    }

    public void setContestTitle(String contestTitle) {
        this.contestTitle = contestTitle;
    }

    public String getGameEntry() {
        return gameEntry;
    }

    public void setGameEntry(String gameEntry) {
        this.gameEntry = gameEntry;
    }

    public String getGamePricePool() {
        return gamePricePool;
    }

    public void setGamePricePool(String gamePricePool) {
        this.gamePricePool = gamePricePool;
    }

    public String getTotalJoined() {
        return totalJoined;
    }

    public void setTotalJoined(String totalJoined) {
        this.totalJoined = totalJoined;
    }

    public String getUseBonus() {
        return useBonus;
    }

    public void setUseBonus(String useBonus) {
        this.useBonus = useBonus;
    }

    public String getShowStatus() {
        return showStatus;
    }

    public void setShowStatus(String showStatus) {
        this.showStatus = showStatus;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
