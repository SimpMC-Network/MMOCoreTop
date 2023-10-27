package viezl.mmocoretop;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class LoreGui implements Listener {
    public static void updateLoreWithPlaceholders(Player player, ItemStack itemStack) {
        if (player != null && itemStack != null) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta != null && itemMeta.hasLore()) {
                List<String> lore = itemMeta.getLore();
                List<String> updatedLore = new ArrayList<>();

                for (String loreLine : lore) {
                    String updatedLine = PlaceholderAPI.setPlaceholders(player, loreLine);
                    updatedLore.add(updatedLine);
                }
                itemMeta.setLore(updatedLore);
                itemStack.setItemMeta(itemMeta);
            }
        }
    }
}
