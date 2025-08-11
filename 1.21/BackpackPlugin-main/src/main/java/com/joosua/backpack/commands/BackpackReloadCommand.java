package com.joosua.backpack.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class BackpackReloadCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    public BackpackReloadCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String Label, String[] args) {

        if (!sender.hasPermission("backpackPlugin.reload")) {
            sender.sendMessage(ChatColor.RED + "You don’t have permission to do that!");
            return true;
        }

        plugin.reloadConfig(); // reload config.yml
        sender.sendMessage(ChatColor.GREEN + "Backpack plugin configuration reloaded!");

        return true;
    }
}
