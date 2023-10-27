package viezl.mmocoretop;

public class PlayerData {
    private String playerName;
    private int level;

    public PlayerData(String playerName, int level) {
        this.playerName = playerName;
        this.level = level;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getLevel() {
        return level;
    }
}
