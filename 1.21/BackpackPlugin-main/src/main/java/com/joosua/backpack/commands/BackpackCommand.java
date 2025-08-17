package com.joosua.backpack.commands;

import com.joosua.backpack.BackpackPlugin;
import com.joosua.backpack.managers.BackpackManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BackpackCommand implements CommandExecutor {

    private final BackpackPlugin plugin;

    public BackpackCommand(BackpackPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }

        if (args.length == 0) {
            player.openInventory(plugin.getBackpackManager().getBackpack(player, 1));
            return true;
        }

        try {
            int number;
            number = Integer.parseInt(args[0]);

            // Handle /backpack <number>
            if (!player.hasPermission("backpack.use")) {
                player.sendMessage("§cYou do not have permission to use backpacks!");
                return true;
            }

            int globalMax = plugin.getConfig().getInt("backpack.max-backpacks", 3);
            int playerMax = plugin.getBackpackManager().getPlayerMaxBackpacks(player, globalMax);


            if (number < 1 || number > playerMax) {
                player.sendMessage("§cBackpack number must be between 1 and " + playerMax);
                return true;
            }

            plugin.getOpenBackpacks().put(player.getUniqueId(), number); // Track the backpack number
            player.openInventory(plugin.getBackpackManager().getBackpack(player, number));

            String msg = plugin.getConfig()
                    .getString("messages.opened-backpack", "&aOpened Backpack #%number%!")
                    .replace("%number%", String.valueOf(number))
                    .replace("&", "§");
            player.sendMessage(msg);

            return true;
        } catch (NumberFormatException e) {
            switch (args[0].toLowerCase()) {
                case "title":
                    if (!player.hasPermission("backpack.rename")) {
                        player.sendMessage("§cYou do not have permission to rename backpacks!");
                        return true;
                    }

                    if (args.length < 3) {
                        player.sendMessage("§eUsage: /backpack title <number> <title>");
                        return true;
                    }

                    int number;
                    number = Integer.parseInt(args[1]);

                    // Combine all args after the first as the title
                    StringBuilder titleBuilder = new StringBuilder();
                    for (int i = 2; i < args.length; i++) {
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
                case "plugin":
                    if (args.length > 1 && args[1].equalsIgnoreCase("reload")) {
                        if (!sender.hasPermission("backpack.plugin.reload")) {
                            sender.sendMessage(ChatColor.RED + "You don’t have permission to do that!");
                            return true;
                        }
                        plugin.reloadConfig();
                        sender.sendMessage(ChatColor.GREEN + "Backpack plugin configuration reloaded!");
                    } else {
                        sender.sendMessage(ChatColor.YELLOW + "Usage: /backpack plugin reload");
                    }
                    return true;
                default:
                    sender.sendMessage(ChatColor.RED + "Unknown command!");
                    return true;
            }
        }
    }
}
