package me.zacharycram.cosmiccraftweapons.events;

import me.zacharycram.cosmiccraftweapons.CosmicCraftWeaponsPlugin;
import me.zacharycram.cosmiccraftweapons.utility.StringUtils;

import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class WeaponListener implements Listener {
    private final CosmicCraftWeaponsPlugin plugin;
    private final double pyroDmg;
    private final double dbDmg;
    private final double dbLifesteal;

    public WeaponListener(CosmicCraftWeaponsPlugin plugin) {
        this.plugin = plugin;
        this.pyroDmg = plugin.getConfig().getDouble("config.pyro_dmg");
        this.dbDmg = plugin.getConfig().getDouble("config.db_dmg");
        this.dbLifesteal = plugin.getConfig().getDouble("config.db_lifesteal");
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if (!(damager instanceof Player)) return;

        Player attacker = (Player) damager;
        World world = attacker.getLocation().getWorld();
        ItemStack item = attacker.getInventory().getItemInMainHand();
        if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore()) {
            for (String lore : item.getItemMeta().getLore()) { // TODO dynamic?
                if (StringUtils.stripColoredText(lore).equals("I come from the Iron Hills")) {
                    event.setDamage(event.getDamage() * pyroDmg);
                    world.playSound(attacker.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0F, 0.0F);
                    world.playEffect(attacker.getLocation(), Effect.ZOMBIE_DESTROY_DOOR, 1);
                }
                if (StringUtils.stripColoredText(lore).equals("Deathbringer")) {
                    event.setDamage(event.getDamage() * dbDmg);
                }
                if (StringUtils.stripColoredText(lore).equals("Lifesteal")) {
                    /* sometimes this can cause health to go over 20, resulting in a console stacktrace
                       so it is best to ignore the stacktrace */
                    try {
                        attacker.setHealth(attacker.getHealth() + dbLifesteal);
                    } catch (IllegalArgumentException exception) {}
                }
            }
        }
    }
}
