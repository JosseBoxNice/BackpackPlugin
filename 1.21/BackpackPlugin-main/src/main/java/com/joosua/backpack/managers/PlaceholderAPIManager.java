package com.joosua.backpack.managers;

import com.joosua.backpack.BackpackPlugin;
import org.bukkit.entity.Player;

public class PlaceholderAPIManager {

    private final BackpackPlugin plugin;

    public PlaceholderAPIManager(BackpackPlugin plugin) {
        this.plugin = plugin;
    }

    public String getMaxBackpacks(Player player) {
        int globalMax = plugin.getConfig().getInt("backpack.max-backpacks", 3);
        int maxBackpacks = plugin.getBackpackManager().getPlayerMaxBackpacks(player, globalMax);
        return String.valueOf(maxBackpacks);
    }
}
