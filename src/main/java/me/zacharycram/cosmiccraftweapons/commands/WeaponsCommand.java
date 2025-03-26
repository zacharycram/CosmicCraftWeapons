package me.zacharycram.cosmiccraftweapons.commands;

import me.zacharycram.cosmiccraftweapons.CosmicCraftWeaponsPlugin;
import me.zacharycram.cosmiccraftweapons.Messages;
import me.zacharycram.cosmiccraftweapons.utility.ItemBuilder;
import me.zacharycram.cosmiccraftweapons.utility.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeaponsCommand implements CommandExecutor {
    private final CosmicCraftWeaponsPlugin plugin;
    private final boolean isWeaponInfoEnabled;
    private final String dateFormat;
    private final String timeFormat;
    private final List<String> pyroAliases;
    private final List<String> dbAliases;

    public WeaponsCommand(CosmicCraftWeaponsPlugin plugin) {
        this.plugin = plugin;
        this.isWeaponInfoEnabled = plugin.getConfig().getBoolean("config.enable_weapon_information_lore");
        this.dateFormat = plugin.getConfig().getString("config.date");
        this.timeFormat = plugin.getConfig().getString("config.time");
        this.pyroAliases = plugin.getConfig().getStringList("config.pyro_aliases");
        this.dbAliases = plugin.getConfig().getStringList("config.db_aliases");
        plugin.getCommand("weapons").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {
        FileConfiguration messagesYml = plugin.getMessagesYML().getConfig("messages.yml");

        if (!sender.hasPermission("weapons.use")) {
            sender.sendMessage(Messages.NO_PERMISSION.get(messagesYml));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("&dCosmicCraftWeapons coded by zacharycram");
            for (String str : messagesYml.getStringList("messages.main_command_help")) {
                sender.sendMessage(StringUtils.color(str));
            }
        }

        if (args.length == 1) {
            if (!sender.hasPermission("weapons.reload")) {
                sender.sendMessage(Messages.NO_PERMISSION.get(messagesYml));
                return true;
            }

            Bukkit.getPluginManager().disablePlugin(plugin);
            Bukkit.getPluginManager().enablePlugin(plugin);
            sender.sendMessage(Messages.PLUGIN_RELOAD.get(messagesYml));
        }

        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("give")) {
                Player recipient = Bukkit.getPlayer(args[1]);
                if (recipient == null) {
                    sender.sendMessage(Messages.OFFLINE_PLAYER.get(messagesYml));
                    return true;
                }

                String itemAlias = args[2];

                List<String> aliasesCheckList = new ArrayList<>();
                aliasesCheckList.addAll(pyroAliases);
                aliasesCheckList.addAll(dbAliases);
                if (!aliasesCheckList.contains(itemAlias)) {
                    sender.sendMessage(Messages.INVALID_ITEM.get(messagesYml));
                    return true;
                }

                sender.sendMessage(Messages.GAVE_ITEM.get(messagesYml)
                        .replace("%player%", recipient.getName())
                );
                giveItemToPlayer(recipient, itemAlias);
            }
        }
        return true;
    }

    private void giveItemToPlayer(Player player, String itemAlias) {
        boolean isPyro = pyroAliases.contains(itemAlias);

        List<String> weaponLore = new ArrayList<>();

        Map<Enchantment, Integer> enchantments = new HashMap<>();
        enchantments.put(Enchantment.DAMAGE_ALL, 5);
        enchantments.put(Enchantment.FIRE_ASPECT, 2);

        ItemBuilder itemBuilder;
        // TODO dynamic
        if (isPyro) {
            weaponLore.add("&4I come from the Iron Hills");
            itemBuilder = new ItemBuilder(Material.DIAMOND_AXE)
                    .setName("&4Pyro Axe")
                    ;
        } else {
            weaponLore.add("&4&oDeathbringer");
            weaponLore.add("&4&oLifesteal");
            itemBuilder = new ItemBuilder(Material.DIAMOND_SWORD)
                    .setName("&8&lD&4&le&8&la&4&lt&8&lh B&4&lr&8&li&4&ln&8&lg&4&le&8&lr")
            ;
        }

        if (isWeaponInfoEnabled) {
            List<String> toAppend = plugin.getConfig().getStringList("config.append_weapon_lore");
            toAppend.replaceAll(str -> str
                    .replace("%player%", player.getName())
                    .replace("%date%", StringUtils.getCurrentDate(dateFormat))
                    .replace("%time%", StringUtils.getCurrentTime(timeFormat))
            );
            weaponLore.addAll(toAppend);
        }

        itemBuilder.setLore(weaponLore);
        enchantments.forEach(itemBuilder::addUnsafeEnchantment);

        HashMap<Integer, ItemStack> leftOverItems = player.getInventory().addItem(itemBuilder.build());
        if (!leftOverItems.isEmpty()) {
            player.sendMessage(Messages.INVENTORY_FULL.get(plugin.getMessagesYML().getConfig("messages.yml")));
            plugin.getPlayerDataUtil().addToStorage(player.getUniqueId(), leftOverItems.values());
        } else {
            player.sendMessage(Messages.RECEIVED_ITEM.get(plugin.getMessagesYML().getConfig("messages.yml")));
        }
    }
}