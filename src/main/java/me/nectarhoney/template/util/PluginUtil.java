package me.nectarhoney.template.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class PluginUtil {

    /**
     * Check if a plugin is loaded.
     *
     * @param name Name of the plugin to check.
     * @return If the plugin is loaded.
     */
    public static boolean isPluginMissing(String name) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(name);
        return plugin == null;
    }
}
