package me.zacharycram.cosmiccraftweapons;

import me.zacharycram.cosmiccraftweapons.commands.WeaponsCommand;
import me.zacharycram.cosmiccraftweapons.events.WeaponListener;
import me.zacharycram.cosmiccraftweapons.utility.DataHandler;

import org.bukkit.plugin.java.JavaPlugin;

public class CosmicCraftWeaponsPlugin extends JavaPlugin {
    private DataHandler messagesYML;

    public void onEnable() {
        initFiles();
        initCommands();
        initEvents();
    }

    private void initFiles() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        reloadConfig();

        messagesYML = new DataHandler(this, "messages.yml");
    }

    private void initCommands() {
        new WeaponsCommand(this);
    }

    private void initEvents() {
        new WeaponListener(this);
    }

    public DataHandler getMessagesYML() {
        return messagesYML;
    }
}
