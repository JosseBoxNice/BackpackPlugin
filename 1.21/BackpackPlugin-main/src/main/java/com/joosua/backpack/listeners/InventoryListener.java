package com.joosua.backpack.listeners;

import com.joosua.backpack.BackpackPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InventoryListener implements Listener {

    private final BackpackPlugin plugin;
    // Pattern matches "Backpack" followed by optional '#' and spaces, then captures a number
    private static final Pattern TITLE_PATTERN = Pattern.compile("Backpack\\s*#?\\s*(\\d+)");

    public InventoryListener(BackpackPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        try {
            UUID uuid = event.getPlayer().getUniqueId();

            // Only save if this was a tracked backpack
            if (!plugin.getOpenBackpacks().containsKey(uuid)) return;

            String title = event.getView().getTitle();

            int number = plugin.getOpenBackpacks().remove(uuid); // Get & remove tracking
            plugin.getLogger().info("[InventoryListener] Inventory title on close: " + title);

            int max = plugin.getConfig().getInt("backpack.max-backpacks", 3);
            if (number < 1 || number > max) {
                plugin.getLogger().warning("[InventoryListener] Backpack number out of bounds: " + number + ", resetting to 1");
                number = 1;
            }

            plugin.getLogger().info("[InventoryListener] Saving backpack #" + number + " for player " + uuid);

            try {
                plugin.getBackpackManager().saveBackpack(uuid, number, event.getInventory());
            } catch (Exception e) {
                plugin.getLogger().severe("[InventoryListener] Failed to save backpack #" + number + " for player " + uuid + ": " + e.getMessage());
            }

        } catch (Exception e) {
            plugin.getLogger().severe("[InventoryListener] Unexpected error in InventoryCloseEvent: " + e.getMessage());
        }
    }
}
