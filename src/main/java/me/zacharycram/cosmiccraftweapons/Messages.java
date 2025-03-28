package me.zacharycram.cosmiccraftweapons;

import me.zacharycram.cosmiccraftweapons.utility.StringUtils;

import org.bukkit.configuration.file.FileConfiguration;

public enum Messages {
    PREFIX("messages.prefix"),
    CONSOLE("messages.console"),
    NO_PERMISSION("messages.no_perm"),
    OFFLINE_PLAYER("messages.offline_player"),
    INVALID_ITEM("messages.invalid_item"),
    INVENTORY_FULL("messages.inventory_full"),
    RECEIVED_ITEM("messages.received_item"),
    GAVE_ITEM("messages.gave_item"),
    PLUGIN_RELOAD("messages.plugin_reload"),
    CLAIMED_PENDING("messages.claimed_pending"),
    NO_PENDING("messages.no_pending"),
    ;

    private final String path;

    Messages(String path) {
        this.path = path;
    }

    public String get(FileConfiguration file) {
        return StringUtils.color(file.getString(path)
                .replace("%prefix%", file.getString(PREFIX.path))
        );
    }
}
