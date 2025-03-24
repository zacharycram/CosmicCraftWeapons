package me.zacharycram.cosmiccraftweapons.utility;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class DataHandler {
    private final JavaPlugin plugin;
    private FileConfiguration fileConfiguration = null;
    private File dataFile = null;

    public DataHandler(JavaPlugin plugin, String path) {
        this.plugin = plugin;
        saveDefaultConfig(path);
    }

    public void reloadConfig(String path) {
        if (dataFile == null) {
            this.dataFile = new File(plugin.getDataFolder(), path);
        }

        this.fileConfiguration = YamlConfiguration.loadConfiguration(dataFile);

        InputStream fileStream = plugin.getResource(path);
        if (fileStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(fileStream));
            fileConfiguration.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig(String path) {
        if (fileConfiguration == null) reloadConfig(path);
        return fileConfiguration;
    }

    public void saveConfig(String path) {
        if (fileConfiguration == null || dataFile == null) return;

        try {
            getConfig(path).save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Unable to save config to " + this.dataFile, e);
        }
    }

    public void saveDefaultConfig(String path) {
        if (dataFile == null) dataFile = new File(plugin.getDataFolder(), path);

        if (!dataFile.exists()) {
            plugin.saveResource(path, false);
        }
    }
}