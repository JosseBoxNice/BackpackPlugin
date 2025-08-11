package com.joosua.backpack.commands;

import com.joosua.backpack.BackpackManager;
import com.joosua.backpack.BackpackPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BackpackTitleCommand implements CommandExecutor {

    private final BackpackPlugin plugin;

    public BackpackTitleCommand(BackpackPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }

        if (!player.hasPermission("backpack.rename")) {
            player.sendMessage("§cYou do not have permission to rename backpacks!");
            return true;
        }

        if (args.length < 2) {
            player.sendMessage("§eUsage: /backpacktitle <number> <title>");
            return true;
        }

        int number;
        try {
            number = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage("§cInvalid backpack number!");
            return true;
        }

        // Combine all args after the first as the title
        StringBuilder titleBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            titleBuilder.append(args[i]).append(" ");
        }
        String title = titleBuilder.toString().trim();

        // Apply color if player has permission
        if (player.hasPermission("backpack.color")) {
            title = title.replace("&", "§");
        }

        // Save the title
        BackpackManager manager = plugin.getBackpackManager();
        manager.setBackpackTitle(player.getUniqueId(), number, title);

        player.sendMessage("§aBackpack #" + number + " title set to: " + title);
        return true; // IMPORTANT: prevent usage message
    }
}

