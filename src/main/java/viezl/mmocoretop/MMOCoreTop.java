package viezl.mmocoretop;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.yaml.snakeyaml.Yaml;
import java.io.FileReader;
import java.io.FileWriter;

public class MMOCoreTop extends JavaPlugin {

    private File dataFile;
    private Map<String, Integer> playerData = new HashMap<>();

    @Override
    public void onEnable() {
        dataFile = new File(getDataFolder(), "player_data.yml");
        if (!dataFile.getParentFile().exists()) {
            dataFile.getParentFile().mkdirs();
        }
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
                createSampleDataFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        getServer().getPluginManager().registerEvents(new PlayerLoginListener(playerData,dataFile), this);
        getServer().getPluginManager().registerEvents(new PlayerLogoutListener(playerData), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(),this);
        getServer().getPluginManager().registerEvents(new LoreGui(),this);
        loadPlayerData();
        getCommand("topplayers").setExecutor(new TopPlayersCommand());
    }


    private class TopPlayersCommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                String playerName = player.getName();

                if (playerData.containsKey(playerName)) {
                    int currentLevel = player.getLevel();
                    playerData.put(playerName, currentLevel);
                    savePlayerData(); // Lưu dữ liệu người chơi sau khi cập nhật.
                }
                else
                {
                    int currentLevel = player.getLevel();
                    playerData.put(playerName, currentLevel);
                    savePlayerData();
                }

                List<Map.Entry<String, Integer>> topPlayers = playerData.entrySet().stream()
                        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                        .limit(18)
                        .collect(Collectors.toList());

                Inventory gui = Bukkit.createInventory(player, 18, ChatColor.AQUA+"Top Người Chơi Có Câp Cao Nhất");

                for (int i = 0; i < topPlayers.size(); i++) {
                    Map.Entry<String, Integer> entry = topPlayers.get(i);
                    String Name = entry.getKey();
                    Integer y=i+1;
                    int playerLevel = entry.getValue();
                    ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
                    ItemMeta meta = playerHead.getItemMeta();
                    meta.setDisplayName(ChatColor.AQUA+"Người chơi top #"+ChatColor.GOLD+y+" "+ChatColor.YELLOW+Name);
                    List<String> lore = new ArrayList<>();
                    lore.add(ChatColor.YELLOW+ "Cấp độ : " + ChatColor.GOLD + playerLevel);
                    lore.add(ChatColor.YELLOW+ "Nhân vật : " + ChatColor.WHITE + "%mmocore_class%");
                    lore.add(" ");
                    lore.add(ChatColor.WHITE+"Hehe đây là một người chơi đáng kính");
                    meta.setLore(lore);
                    playerHead.setItemMeta(meta);
                    LoreGui.updateLoreWithPlaceholders(player,playerHead);
                    gui.setItem(i,playerHead);
                }

                player.openInventory(gui);
            }
            else {
                sender.sendMessage("Chỉ người chơi có thể sử dụng lệnh này.");
            }
            return true;
        }
    }

    private class InventoryListener implements Listener {
        @EventHandler
        public void onInventoryClick(InventoryClickEvent event) {
            // Kiểm tra nếu người chơi đang mở GUI "Top Players"
            if (event.getView().getTitle().equals(ChatColor.AQUA+"Top Người Chơi Có Câp Cao Nhất")) {
                Player player = (Player) event.getWhoClicked();

                // Ngăn tất cả hành động tương tác với GUI
                event.setCancelled(true);

                // Kiểm tra nếu người chơi cố gắng lấy vật phẩm ra khỏi GUI và ngăn chặn nó
                if (event.getRawSlot() < 18) {
                    event.setCancelled(true);
                }
            }
        }
    }

    // Load player data from the YAML file.
    private void loadPlayerData() {
        Yaml yaml = new Yaml();
        try (FileReader reader = new FileReader(dataFile)) {
            playerData = yaml.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Save player data to the YAML file.
    private void savePlayerData() {
        Yaml yaml = new Yaml();
        try (FileWriter writer = new FileWriter(dataFile)) {
            yaml.dump(playerData, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Update player data when a player logs in.
    private void onPlayerLogin(Player player) {
        int level = player.getLevel();
        playerData.put(player.getName(), level);
        savePlayerData();
    }

    // Update player data when a player logs out.
    private void onPlayerLogout(Player player) {
        int level = player.getLevel();
        playerData.put(player.getName(), level);
        savePlayerData();
    }
    private void createSampleDataFile() {
        playerData.put("ViezL", 0);
        savePlayerData();
    }
}