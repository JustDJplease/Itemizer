package me.nectarhoney.template.util;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;

public class MessageUtil {
    @Getter @Setter public static String prefix = "§6[§ePrefix§6] §e";

    /**
     * Format a message and add the plugin prefix.
     *
     * @param string The message to format.
     * @return The formatted message.
     */
    public static String format(String string) {
        string = ChatColor.translateAlternateColorCodes('&', string);
        return getPrefix() + string;
    }
}
