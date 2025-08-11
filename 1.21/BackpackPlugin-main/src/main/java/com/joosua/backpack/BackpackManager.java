package com.joosua.backpack;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BackpackManager {

    private final BackpackPlugin plugin;
    private final Map<UUID, Map<Integer, ItemStack[]>> contentsCache = new HashMap<>();
    private final Map<UUID, Map<Integer, String>> titleCache = new HashMap<>();

    public BackpackManager(BackpackPlugin plugin) {
        this.plugin = plugin;
    }

    /** Open or get a backpack with live permission-based sizing */
    public Inventory getBackpack(Player player, int number) {
        UUID uuid = player.getUniqueId();
        int allowedSize = getBackpackSize(player);

        // Load content
        ItemStack[] contents = getContents(uuid, number);
        ItemStack[] resized = resizeContents(contents, allowedSize);
        cacheContents(uuid, number, resized);

        // Get custom title
        String title = getBackpackTitle(uuid, number);
        if (title == null || title.isEmpty()) {
            title = plugin.getConfig().getString("backpack.title", "&6Your Backpack %number%")
                    .replace("%number%", String.valueOf(number))
                    .replace("&", "§");
        }

        Inventory inv = Bukkit.createInventory(null, allowedSize, title);
        inv.setContents(resized);
        return inv;
    }

    /** Resize items array to fit the current allowed size */
    private ItemStack[] resizeContents(ItemStack[] original, int allowedSize) {
        if (original == null) return new ItemStack[allowedSize];
        ItemStack[] resized = new ItemStack[allowedSize];
        Arrays.fill(resized, null);
        System.arraycopy(original, 0, resized, 0, Math.min(original.length, allowedSize));
        return resized;
    }

    /** Load or cache contents */
    private ItemStack[] getContents(UUID uuid, int number) {
        contentsCache.putIfAbsent(uuid, new HashMap<>());
        Map<Integer, ItemStack[]> playerData = contentsCache.get(uuid);

        if (playerData.containsKey(number)) return playerData.get(number);

        ItemStack[] loaded = loadContents(uuid, number);
        playerData.put(number, loaded);
        return loaded;
    }

    private void cacheContents(UUID uuid, int number, ItemStack[] contents) {
        contentsCache.putIfAbsent(uuid, new HashMap<>());
        contentsCache.get(uuid).put(number, contents);
    }

    private ItemStack[] loadContents(UUID uuid, int number) {
        File file = new File(plugin.getDataFolder(), "data/" + uuid + ".yml");
        if (!file.exists()) return new ItemStack[0];

        try {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            String base64 = config.getString("backpack" + number, "");
            return base64.isEmpty() ? new ItemStack[0] : InventoryUtils.fromBase64(base64);
        } catch (Exception e) {
            plugin.getLogger().severe("Error loading backpack " + number + " for " + uuid + ": " + e.getMessage());
            return new ItemStack[0];
        }
    }

    /** Save backpack contents */
    public void saveBackpack(UUID uuid, int number, Inventory inv) {
        cacheContents(uuid, number, inv.getContents());

        try {
            String base64 = InventoryUtils.toBase64(inv.getContents());
            File folder = new File(plugin.getDataFolder(), "data");
            if (!folder.exists()) folder.mkdirs();

            File file = new File(folder, uuid + ".yml");
            YamlConfiguration config = file.exists() ? YamlConfiguration.loadConfiguration(file) : new YamlConfiguration();

            config.set("backpack" + number, base64);

            // Save custom title if exists
            String customTitle = getBackpackTitle(uuid, number);
            if (customTitle != null && !customTitle.isEmpty()) {
                config.set("title" + number, customTitle);
            }

            config.save(file);

        } catch (IOException e) {
            plugin.getLogger().severe("Error saving backpack " + number + " for " + uuid + ": " + e.getMessage());
        }
    }

    public void saveAll() {
        contentsCache.forEach((uuid, backpacks) -> {
            backpacks.forEach((number, contents) -> {
                Inventory dummy = Bukkit.createInventory(null, Math.max(contents.length, 9));
                dummy.setContents(contents);
                saveBackpack(uuid, number, dummy);
            });
        });
    }

    private int getBackpackSize(Player player) {
        int defaultSize = plugin.getConfig().getInt("backpack.default-size", 27);
        if (player == null) return defaultSize;
        int maxSize = 0;
        for (int size = 9; size <= 54; size += 9) {
            if (player.hasPermission("backpack.size." + size)) {
                maxSize = size;
            }
        }
        return (maxSize > 0) ? maxSize : defaultSize;
    }

    /** Manage custom titles */
    public String getBackpackTitle(UUID uuid, int number) {
        titleCache.putIfAbsent(uuid, new HashMap<>());
        Map<Integer, String> titles = titleCache.get(uuid);

        if (titles.containsKey(number)) return titles.get(number);

        // Load from file if exists
        File file = new File(plugin.getDataFolder(), "data/" + uuid + ".yml");
        if (!file.exists()) return null;

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        String title = config.getString("title" + number, null);
        titles.put(number, title);
        return title;
    }

    public void setBackpackTitle(UUID uuid, int number, String title) {
        titleCache.putIfAbsent(uuid, new HashMap<>());
        titleCache.get(uuid).put(number, title);

        // Save immediately
        File folder = new File(plugin.getDataFolder(), "data");
        if (!folder.exists()) folder.mkdirs();

        File file = new File(folder, uuid + ".yml");
        YamlConfiguration config = file.exists() ? YamlConfiguration.loadConfiguration(file) : new YamlConfiguration();
        config.set("title" + number, title);

        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save custom title for backpack " + number + " of " + uuid);
        }
    }

    public void clearCache(UUID uuid) {
        contentsCache.remove(uuid);
        titleCache.remove(uuid);
    }
}
