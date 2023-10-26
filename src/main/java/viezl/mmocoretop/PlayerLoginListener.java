package viezl.mmocoretop;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Map;

public class PlayerLoginListener implements Listener {
    private final Map<String, Integer> playerData;

    public PlayerLoginListener(Map<String, Integer> playerData) {
        this.playerData = playerData;
    }

    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

        if (playerData.containsKey(playerName)) {
            int level = playerData.get(playerName);
            int currentLevel = player.getLevel();
            if (level != currentLevel) {
                playerData.put(playerName,currentLevel);
            }
        }
        else
        {
            int currentLevel = player.getLevel();
            playerData.put(playerName, currentLevel);
        }
    }
}
