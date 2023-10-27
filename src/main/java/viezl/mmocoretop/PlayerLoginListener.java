package viezl.mmocoretop;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class PlayerLoginListener implements Listener {
    private final Map<String, Integer> playerData;
    private final File dataFile;
    public PlayerLoginListener(Map<String, Integer> playerData,File dataFile) {
        this.playerData = playerData;
        this.dataFile = dataFile;
    }
    private void savePlayerData() {
        Yaml yaml = new Yaml();
        try (FileWriter writer = new FileWriter(dataFile)) {
            yaml.dump(playerData, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            savePlayerData();
        }
        else
        {
            int currentLevel = player.getLevel();
            playerData.put(playerName, currentLevel);
            savePlayerData();
        }
    }
}

