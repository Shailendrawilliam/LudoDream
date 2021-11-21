package com.example.indianludobattle.Model;

public class JoinPlayerModel {
    private String gameEntry,gameId,gamePricePool,joinTime,playerId,playerName,contestTitle;
    private boolean isSelected;

    public JoinPlayerModel(String contestTitle,String gameEntry, String gameId, String gamePricePool, String joinTime, String playerId, String playerName,boolean isSelected) {
        this.contestTitle = contestTitle;
        this.gameEntry = gameEntry;
        this.gameId = gameId;
        this.gamePricePool = gamePricePool;
        this.joinTime = joinTime;
        this.playerId = playerId;
        this.playerName = playerName;
        this.isSelected = isSelected;

    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
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

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGamePricePool() {
        return gamePricePool;
    }

    public void setGamePricePool(String gamePricePool) {
        this.gamePricePool = gamePricePool;
    }

    public String getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(String joinTime) {
        this.joinTime = joinTime;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
