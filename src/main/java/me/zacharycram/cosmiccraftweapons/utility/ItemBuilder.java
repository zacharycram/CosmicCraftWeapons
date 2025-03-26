package me.zacharycram.cosmiccraftweapons.utility;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ItemBuilder { // remove unnecessary methods
    private final ItemStack itemStack;

    public ItemBuilder(Material m) {
        this(m, 1);
    }

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack.clone();
    }

    public ItemBuilder(Material m, int amount) {
        this.itemStack = new ItemStack(m, amount);
    }

    public ItemBuilder(Material m, int amount, byte durability) {
        this.itemStack = new ItemStack(m, amount, durability);
    }

    public ItemBuilder setName(String name) {
        name = StringUtils.color(name);
        ItemMeta im = this.itemStack.getItemMeta();
        im.setDisplayName(name);
        this.itemStack.setItemMeta(im);
        return this;
    }

    public ItemBuilder addUnsafeEnchantment(Enchantment ench, int level) {
        this.itemStack.addUnsafeEnchantment(ench, level);
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment ench) {
        this.itemStack.removeEnchantment(ench);
        return this;
    }

    public ItemBuilder addEnchant(Enchantment ench, int level) {
        ItemMeta im = this.itemStack.getItemMeta();
        im.addEnchant(ench, level, true);
        this.itemStack.setItemMeta(im);
        return this;
    }

    public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments) {
        this.itemStack.addEnchantments(enchantments);
        return this;
    }

    public ItemBuilder setLore(String lore) {
        lore = StringUtils.color(lore);
        ItemMeta im = this.itemStack.getItemMeta();
        ArrayList<String> arrayList = new ArrayList<>();
        String[] str = lore.split("\\r?\\n");
        if (str[0] == null) {
            arrayList.add(lore);
        } else {
            arrayList.addAll(Arrays.asList(str));
        }
        im.setLore(arrayList);
        this.itemStack.setItemMeta(im);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        lore = StringUtils.color(lore);
        ItemMeta im = this.itemStack.getItemMeta();
        im.setLore(lore);
        this.itemStack.setItemMeta(im);
        return this;
    }

    public ItemBuilder setGlow(boolean glow) {
        if (glow) {
            ItemMeta im = this.itemStack.getItemMeta();
            im.addEnchant(Enchantment.DURABILITY, 1, true);
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            this.itemStack.setItemMeta(im);
        }
        return this;
    }

    public ItemStack build() {
        return this.itemStack;
    }
}