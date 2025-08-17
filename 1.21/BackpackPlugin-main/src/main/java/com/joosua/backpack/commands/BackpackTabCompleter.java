package com.joosua.backpack.commands;

import org.bukkit.command.TabCompleter;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BackpackTabCompleter implements TabCompleter {

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (args.length == 1) {
            return Arrays.asList("title", "plugin");
        }

        if (args[0].equalsIgnoreCase("plugin")) {
            return Arrays.asList("reload");
        }

        return Collections.emptyList();
    }

}