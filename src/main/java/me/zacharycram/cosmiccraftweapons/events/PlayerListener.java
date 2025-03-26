package me.zacharycram.cosmiccraftweapons.events;

import me.zacharycram.cosmiccraftweapons.CosmicCraftWeaponsPlugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {
    private final CosmicCraftWeaponsPlugin plugin;

    public PlayerListener(CosmicCraftWeaponsPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getPlayerDataUtil().createStorageFile(player.getUniqueId());

        // Notify the player that they have pending claims.
        if (plugin.getClaimStatus().checkPlayerHasClaim(player.getUniqueId())) {
            plugin.getClaimStatus().sendReminder(player);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        // Notify the player that they have pending claims.
        if (plugin.getClaimStatus().checkPlayerHasClaim(player.getUniqueId())) {
            plugin.getClaimStatus().sendReminder(player);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        // Notify the player that they have pending claims.
        if (plugin.getClaimStatus().checkPlayerHasClaim(player.getUniqueId())) {
            plugin.getClaimStatus().sendReminder(player);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        // Notify the player that they have pending claims.
        if (plugin.getClaimStatus().checkPlayerHasClaim(player.getUniqueId())) {
            plugin.getClaimStatus().sendReminder(player);
        }
    }
}
