package viezl.mmocoretop;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;

public class PlayerLogoutListener implements Listener {
    private final Map<String, Integer> playerData;

    public PlayerLogoutListener(Map<String, Integer> playerData) {
        this.playerData = playerData;
    }

    @EventHandler
    public void onPlayerLogout(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        int currentLevel = player.getLevel();
        playerData.put(playerName,currentLevel);
    }
}
