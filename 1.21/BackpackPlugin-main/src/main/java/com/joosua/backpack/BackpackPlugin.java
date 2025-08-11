package com.joosua.backpack;

import com.joosua.backpack.commands.BackpackCommand;
import com.joosua.backpack.commands.BackpackTitleCommand;
import com.joosua.backpack.listeners.InventoryListener;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BackpackPlugin extends JavaPlugin {

    private BackpackManager backpackManager;
    private final Map<UUID, Integer> openBackpacks = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        saveResource("messages.yml", false);

        backpackManager = new BackpackManager(this);

        if (getCommand("backpack") != null) {
            getCommand("backpack").setExecutor(new BackpackCommand(this));
        }

        if (getCommand("backpacktitle") != null) {
            getCommand("backpacktitle").setExecutor(new BackpackTitleCommand(this));
        }

        getServer().getPluginManager().registerEvents(new InventoryListener(this), this);

        for (Player player : getServer().getOnlinePlayers()) {
            backpackManager.clearCache(player.getUniqueId());
        }

        getLogger().info("Backpack plugin enabled!");
    }

    @Override
    public void onDisable() {
        // Save all backpacks before shutdown
        if (backpackManager != null) {
            backpackManager.saveAll();
        }
        getLogger().info("Backpack plugin disabled!");
    }

    public Map<UUID, Integer> getOpenBackpacks() {
        return openBackpacks;
    }

    public BackpackManager getBackpackManager() {
        return backpackManager;
    }
}
