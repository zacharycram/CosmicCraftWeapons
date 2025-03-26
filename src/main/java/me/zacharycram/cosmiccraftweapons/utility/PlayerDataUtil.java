package me.zacharycram.cosmiccraftweapons.utility;

import me.zacharycram.cosmiccraftweapons.CosmicCraftWeaponsPlugin;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class PlayerDataUtil {
    private final CosmicCraftWeaponsPlugin plugin;
    private final String path;

    public PlayerDataUtil(CosmicCraftWeaponsPlugin plugin) {
        this.plugin = plugin;
        this.path = plugin.getDataFolder().getAbsolutePath() + "/storage";
    }

    public List<ItemStack> getStorage(UUID uuid) {
        FileConfiguration config = getFileFromUUID(uuid);

        if (config == null) return new ArrayList<>();

        List<ItemStack> storage = (List<ItemStack>) config.get("items");
        return storage != null ? storage : new ArrayList<>();
    }

    public void addToStorage(UUID uuid, Collection<ItemStack> item) {
        if (!Bukkit.isPrimaryThread()) return; // Ensure main thread

        File file = getRawFileFromUUID(uuid);
        FileConfiguration config = getFileFromUUID(uuid);
        List<ItemStack> storage = getStorage(uuid);
        storage.addAll(item);

        config.set("items", storage);
        saveStorageFile(file, config);
    }

    public void createStorageFile(UUID uuid) {
        if (!Bukkit.isPrimaryThread()) return; // Ensure main thread

        File file = new File(path, uuid + ".yml");
        try {
            if (file.createNewFile()) {
                // generate file structure and save it
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                config.set("items", new ArrayList<>());
                config.save(file);
            }
        } catch (IOException exception) {
            plugin.getLogger().warning("Couldn't create storage file for " + uuid);
        }

    }

    public void saveStorageFile(File file, FileConfiguration config) {
        try {
            config.save(file);
        } catch (IOException exception) {
            plugin.getLogger().warning("Couldn't save storage file");
        }
    }

    public FileConfiguration getFileFromUUID(UUID uuid) {
        File file = getRawFileFromUUID(uuid);
        if (file.exists()) {
            return YamlConfiguration.loadConfiguration(file);
        }

        createStorageFile(uuid);
        return YamlConfiguration.loadConfiguration(getRawFileFromUUID(uuid));
    }

    public File getRawFileFromUUID(UUID uuid) {
        return new File(path, uuid + ".yml");
    }
}
