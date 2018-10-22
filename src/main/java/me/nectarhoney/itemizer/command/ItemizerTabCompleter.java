package me.nectarhoney.itemizer.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ItemizerTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return Stream.of("name", "material", "lore", "potion", "attr", "flag", "enchant", "title", "author", "head", "clearall")
                    .filter(cmd -> cmd.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("lore")) {
                return Stream.of("add", "remove", "change")
                        .filter(cmd -> cmd.startsWith(args[1].toLowerCase()))
                        .collect(Collectors.toList());
            }
            if (args[0].equalsIgnoreCase("potion")) {
                return Stream.of("add", "remove", "list")
                        .filter(cmd -> cmd.startsWith(args[1].toLowerCase()))
                        .collect(Collectors.toList());
            }
            if (args[0].equalsIgnoreCase("attr")) {
                return Stream.of("add", "remove", "list", "listall")
                        .filter(cmd -> cmd.startsWith(args[1].toLowerCase()))
                        .collect(Collectors.toList());
            }
            if (args[0].equalsIgnoreCase("flag")) {
                return Stream.of("add", "remove", "list", "listall")
                        .filter(cmd -> cmd.startsWith(args[1].toLowerCase()))
                        .collect(Collectors.toList());
            }
            if (args[0].equalsIgnoreCase("enchant")) {
                return Stream.of("add", "remove", "list", "listall")
                        .filter(cmd -> cmd.startsWith(args[1].toLowerCase()))
                        .collect(Collectors.toList());
            }
        }
        return null;
    }
}
