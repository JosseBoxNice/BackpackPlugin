package com.joosua.backpack.commands;

import com.joosua.backpack.BackpackPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

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

        // Handle /backpack <number>
        if (!player.hasPermission("backpack.use")) {
            player.sendMessage("§cYou do not have permission to use backpacks!");
            return true;
        }

        int globalMax = plugin.getConfig().getInt("backpack.max-backpacks", 3);
        int playerMax = plugin.getBackpackManager().getPlayerMaxBackpacks(player, globalMax);

        int number = 1;
        if (args.length > 0) {
            try {
                number = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                player.sendMessage("§cInvalid backpack number!");
                return true;
            }
        }

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
    }

}
