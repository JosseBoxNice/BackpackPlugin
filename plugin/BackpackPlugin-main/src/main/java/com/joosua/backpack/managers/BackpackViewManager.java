package com.joosua.backpack.managers;

import com.joosua.backpack.BackpackPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class BackpackViewManager {
    private final BackpackPlugin plugin;

    public BackpackViewManager(BackpackPlugin plugin) {
        this.plugin = plugin;
    }

    public Inventory openBackpack(String username, int number, CommandSender sender) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(username);
        UUID playerId = player.getUniqueId();
        if (!plugin.getConfig().contains("backpacksizes." + playerId)) {
            sender.sendMessage(ChatColor.RED + "This player doesnt have a backpack!");
            return null;
        }
        int allowedSize  = plugin.getConfig().getInt("backpacksizes." + playerId);
        ItemStack[] contents = plugin.getBackpackManager().getContents(playerId, number);
        ItemStack[] resized = plugin.getBackpackManager().resizeContents(contents, allowedSize);
        plugin.getBackpackManager().cacheContents(playerId, number, resized);
        // Get custom title
        String title = plugin.getBackpackManager().getBackpackTitle(playerId, number);
        if (title == null || title.isEmpty()) {
            title = plugin.getConfig().getString("backpack.title", "&6Your Backpack %number%")
                    .replace("%number%", String.valueOf(number))
                    .replace("&", "§");
        }
        Inventory inv = Bukkit.createInventory(null, allowedSize, title);
        inv.setContents(resized);
        return inv;
    }

}
