package me.zacharycram.cosmiccraftweapons;

import me.zacharycram.cosmiccraftweapons.claim.ClaimStatus;
import me.zacharycram.cosmiccraftweapons.commands.ClaimCommand;
import me.zacharycram.cosmiccraftweapons.commands.WeaponsCommand;
import me.zacharycram.cosmiccraftweapons.events.PlayerListener;
import me.zacharycram.cosmiccraftweapons.events.WeaponListener;
import me.zacharycram.cosmiccraftweapons.utility.DataHandler;
import me.zacharycram.cosmiccraftweapons.utility.PlayerDataUtil;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class CosmicCraftWeaponsPlugin extends JavaPlugin {
    private PlayerDataUtil playerDataUtil;
    private ClaimStatus claimStatus;
    private BukkitAudiences adventure;
    private MiniMessage miniMessage;
    private DataHandler messagesYML;

    public void onEnable() {
        initUtil();
        initFiles();
        initCommands();
        initEvents();
    }

    public void onDisable() {
        if (adventure != null) {
            adventure.close();
            adventure = null;
        }
    }

    private void initFiles() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        reloadConfig();

        messagesYML = new DataHandler(this, "messages.yml");

        File storageFolder = new File(getDataFolder(), "storage");
        if (!storageFolder.exists()) {
            storageFolder.mkdir();
            getLogger().info("Creating a new storage folder");
        }
    }

    private void initUtil() {
        playerDataUtil = new PlayerDataUtil(this);
        claimStatus = new ClaimStatus(this);

        this.adventure = BukkitAudiences.create(this);
        this.miniMessage = MiniMessage.miniMessage();
    }

    private void initCommands() {
        new WeaponsCommand(this);
        new ClaimCommand(this);
    }

    private void initEvents() {
        new WeaponListener(this);
        new PlayerListener(this);
    }

    public PlayerDataUtil getPlayerDataUtil() {
        return playerDataUtil;
    }

    public ClaimStatus getClaimStatus() {
        return claimStatus;
    }

    public BukkitAudiences getAdventure() {
        return adventure;
    }

    public MiniMessage getMiniMessage() {
        return miniMessage;
    }

    public DataHandler getMessagesYML() {
        return messagesYML;
    }
}
