package me.zacharycram.cosmiccraftweapons.claim.gui;

import me.zacharycram.cosmiccraftweapons.CosmicCraftWeaponsPlugin;
import me.zacharycram.cosmiccraftweapons.Messages;
import me.zacharycram.cosmiccraftweapons.utility.ItemBuilder;
import me.zacharycram.cosmiccraftweapons.utility.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClaimGUI implements Listener, InventoryHolder {
    private final CosmicCraftWeaponsPlugin plugin;
    private final Player player;
    private Inventory inventory;

    public ClaimGUI(CosmicCraftWeaponsPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public Inventory getInventory() {
        inventory = Bukkit.createInventory(
                this,
                InventoryType.HOPPER,
                StringUtils.color(plugin.getConfig().getString("config.claim.gui.title"))
        );
        drawStatic();
        return inventory;
    }

    private void drawStatic() {
        boolean hasPendingClaim = plugin.getClaimStatus().checkPlayerHasClaim(player.getUniqueId());
        int storageItemAmount = plugin.getPlayerDataUtil().getStorage(player.getUniqueId()).size();

        ItemStack item;
        if (hasPendingClaim) {
            List<String> lore = plugin.getConfig().getStringList("config.claim.gui.pending_display.lore");
            lore.replaceAll(str -> str
                    .replace("%amount%", String.valueOf(storageItemAmount))
            );

            item = new ItemBuilder(
                    Material.valueOf(plugin.getConfig().getString("config.claim.gui.pending_display.material")),
                    1,
                    (byte) plugin.getConfig().getInt("config.claim.gui.pending_display.damage")
            )
                    .setName(plugin.getConfig().getString("config.claim.gui.pending_display.name"))
                    .setLore(lore)
                    .setGlow(plugin.getConfig().getBoolean("config.claim.gui.pending_display.glow"))
                    .build();
        } else {
            item = new ItemBuilder(
                    Material.valueOf(plugin.getConfig().getString("config.claim.gui.none_display.material")),
                    1,
                    (byte) plugin.getConfig().getInt("config.claim.gui.none_display.damage")
            )
                    .setName(plugin.getConfig().getString("config.claim.gui.none_display.name"))
                    .setLore(plugin.getConfig().getStringList("config.claim.gui.none_display.lore"))
                    .setGlow(plugin.getConfig().getBoolean("config.claim.gui.none_display.glow"))
                    .build();
        }

        inventory.setItem(2, item);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTopInventory().getHolder() != this) return;

        event.setCancelled(true);

        if (event.getClickedInventory() == null || event.getCurrentItem() == null || event.getCurrentItem()
                .getType() == Material.AIR || event.getClickedInventory().getHolder() != this) return;

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) return;

        ClickType click = event.getClick();
        if (click == ClickType.NUMBER_KEY || click == ClickType.MIDDLE) return;

        FileConfiguration messagesYml = plugin.getMessagesYML().getConfig("messages.yml");

        if (event.getSlot() == 2) {
            player.closeInventory();

            if (plugin.getClaimStatus().checkPlayerHasClaim(player.getUniqueId())) {

                List<ItemStack> storage = plugin.getPlayerDataUtil().getStorage(player.getUniqueId());

                // remove storage data
                File file = plugin.getPlayerDataUtil().getRawFileFromUUID(player.getUniqueId());
                FileConfiguration config = plugin.getPlayerDataUtil().getFileFromUUID(player.getUniqueId());
                config.set("items", new ArrayList<>());
                plugin.getPlayerDataUtil().saveStorageFile(file, config);

                storage.forEach(item -> player.getInventory().addItem(item));
                player.sendMessage(Messages.CLAIMED_PENDING.get(messagesYml)
                        .replace("%amount%", String.valueOf(storage.size()))
                );
            } else {
                player.sendMessage(Messages.NO_PENDING.get(messagesYml));
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() != this) return;
        InventoryClickEvent.getHandlerList().unregister(this);
        InventoryCloseEvent.getHandlerList().unregister(this);
        inventory = null;
    }
}
