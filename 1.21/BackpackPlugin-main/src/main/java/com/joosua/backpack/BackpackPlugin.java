package com.joosua.backpack;

import com.joosua.backpack.commands.BackpackCommand;
import com.joosua.backpack.commands.BackpackReloadCommand;
import com.joosua.backpack.commands.BackpackTitleCommand;
import com.joosua.backpack.listeners.InventoryListener;
import com.joosua.backpack.managers.BackpackManager;
import com.joosua.backpack.managers.PlaceholderAPIManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BackpackPlugin extends JavaPlugin {

    private BackpackManager backpackManager;
    private PlaceholderAPIManager placeholderAPIManager;
    private final Map<UUID, Integer> openBackpacks = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        File file = new File(getDataFolder(), "messages.yml");
        if (!file.exists()) {
            saveResource("messages.yml", false);
        }

        this.placeholderAPIManager = new PlaceholderAPIManager(this);

        backpackManager = new BackpackManager(this);

        if (getCommand("backpack") != null) {
            getCommand("backpack").setExecutor(new BackpackCommand(this));
        }

        if (getCommand("backpack-title") != null) {
            getCommand("backpack-title").setExecutor(new BackpackTitleCommand(this));
        }

        if (getCommand("backpack-reload") != null) {
            getCommand("backpack-reload").setExecutor(new BackpackReloadCommand(this));
        }

        getServer().getPluginManager().registerEvents(new InventoryListener(this), this);

        for (Player player : getServer().getOnlinePlayers()) {
            backpackManager.clearCache(player.getUniqueId());
        }

        getLogger().info("Backpack plugin enabled!");

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PluginExpansion(this).register();
        } else {
            getLogger().warning("PlaceholderAPI not found! Placeholders will not work.");
        }
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
    public PlaceholderAPIManager getPlaceholderAPIManager() {
        return placeholderAPIManager;
    }
}
