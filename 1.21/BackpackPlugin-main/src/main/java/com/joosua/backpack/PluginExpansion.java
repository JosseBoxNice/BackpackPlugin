package com.joosua.backpack;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PluginExpansion extends PlaceholderExpansion {

    private final BackpackPlugin plugin;

    public PluginExpansion(BackpackPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean persist() {
        return true; // This ensures the expansion is persistent across reloads
    }

    @Override
    public boolean canRegister() {
        return true; // Always allow registration
    }

    @Override
    public @NotNull String getIdentifier() {
        return "backpackplugin"; // %backpackplugin_<placeholder>%
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (player == null) return "";

        if (identifier.equals("max-backpacks")) {
            return String.valueOf(plugin.getPlaceholderAPIManager().getMaxBackpacks(player)); // your own method
        }

        return null; // unknown placeholder
    }

}
