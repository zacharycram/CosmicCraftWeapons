package me.zacharycram.cosmiccraftweapons.claim;

import me.zacharycram.cosmiccraftweapons.CosmicCraftWeaponsPlugin;
import me.zacharycram.cosmiccraftweapons.utility.StringUtils;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class ClaimStatus {
    private final CosmicCraftWeaponsPlugin plugin;
    private final HashMap<UUID, Long> claimReminderDelayMap;
    private final long delay;

    public ClaimStatus(CosmicCraftWeaponsPlugin plugin) {
        this.plugin = plugin;
        this.claimReminderDelayMap = new HashMap<>();
        this.delay = plugin.getConfig().getLong("config.claim.delay");
    }

    public void sendReminder(Player player) {
        long currentTime = System.currentTimeMillis();
        int storageItemCount = plugin.getPlayerDataUtil().getStorage(player.getUniqueId()).size();

        // Check and update delay map
        claimReminderDelayMap.compute(player.getUniqueId(), (uuid, nextAllowedTime) -> {
            if (nextAllowedTime == null || currentTime >= nextAllowedTime) {
                Audience audience = plugin.getAdventure().player(player);
                player.sendTitle(
                        StringUtils.color(plugin.getConfig().getString("config.claim.alert_title.title")
                                .replace("%player%", player.getName())
                                .replace("%amount%", String.valueOf(storageItemCount))
                        ),
                        StringUtils.color(plugin.getConfig().getString("config.claim.alert_title.subtitle")
                                .replace("%player%", player.getName())
                                .replace("%amount%", String.valueOf(storageItemCount))
                        ),
                        plugin.getConfig().getInt("config.claim.alert_title.fade_in"),
                        plugin.getConfig().getInt("config.claim.alert_title.stay"),
                        plugin.getConfig().getInt("config.claim.alert_title.fade_out")
                );

                for (String string : plugin.getConfig().getStringList("config.claim.alert_message")) {
                    Component component = plugin.getMiniMessage().deserialize(string);
                    audience.sendMessage(component);
                }

                return currentTime + delay;
            }
            return nextAllowedTime;
        });
    }

    public boolean checkPlayerHasClaim(UUID uuid) {
        return !plugin.getPlayerDataUtil().getStorage(uuid).isEmpty();
    }
}
