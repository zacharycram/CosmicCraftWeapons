package me.zacharycram.cosmiccraftweapons.commands;

import me.zacharycram.cosmiccraftweapons.CosmicCraftWeaponsPlugin;
import me.zacharycram.cosmiccraftweapons.Messages;
import me.zacharycram.cosmiccraftweapons.claim.gui.ClaimGUI;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ClaimCommand implements CommandExecutor {
    private final CosmicCraftWeaponsPlugin plugin;

    public ClaimCommand(CosmicCraftWeaponsPlugin plugin) {
        this.plugin = plugin;
        plugin.getCommand("claim").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {
        FileConfiguration messagesYml = plugin.getMessagesYML().getConfig("messages.yml");

        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.CONSOLE.get(messagesYml));
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("weapons.claim")) {
            player.sendMessage(Messages.NO_PERMISSION.get(messagesYml));
            return true;
        }

        player.openInventory(new ClaimGUI(plugin, player).getInventory());
        return true;
    }
}
