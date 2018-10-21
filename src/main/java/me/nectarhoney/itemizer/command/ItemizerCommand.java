package me.nectarhoney.itemizer.command;

import me.nectarhoney.itemizer.Itemizer;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemizerCommand implements CommandExecutor {

    private Itemizer itemizer;

    public ItemizerCommand(Itemizer itemizer) {
        this.itemizer = itemizer;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("§cThis command can only be executed by a player.");
            return true;
        }
        Player player = (Player) commandSender;

        if (args.length == 0) {
            if (!player.hasPermission("itemizer.help") && !player.isOp()) {
                player.sendMessage("§cYou do not have permission.");
                return true;
            }
            player.sendMessage("§7§m--------------§8[ §6§lItemizer Commands §8]§7§m--------------");
            player.sendMessage("§6/" + label + " name <§fname§6> §8- §eRename an item");
            player.sendMessage("§6/" + label + " material <§fmaterial§6> §8- §eChange the material");
            player.sendMessage("§6/" + label + " lore §8- §eEdit the lore");
            player.sendMessage("§6/" + label + " potion §8- §eEdit a potion");
            player.sendMessage("§6/" + label + " attr §8- §eEdit the attributes");
            player.sendMessage("§6/" + label + " flag §8- §eEdit the item flags");
            player.sendMessage("§6/" + label + " enchant §8- §eEnchant the item");
            player.sendMessage("§6/" + label + " title <§ftitle§6> §8- §eSets the title of a book");
            player.sendMessage("§6/" + label + " author <§fname§6> §8- §eSets the author of a book");
            player.sendMessage("§6/" + label + " head <§fname§6> §8- §eSets the owner of a head");
            player.sendMessage("§6/" + label + " clearall §8- §eClears all data of an item");
            return true;

        } else if (args[0].equalsIgnoreCase("name")) {
            if (!player.hasPermission("itemizer.name") && !player.isOp()) {
                player.sendMessage("§cYou do not have permission.");
                return true;
            }
            if (hasEmptyHand(player)) {
                player.sendMessage("§cYou must be holding an item to modify it.");
                return true;
            }
            itemizer.getItemManager().setName(player, args);
            return true;
        } else if (args[0].equalsIgnoreCase("material")) {
            if (!player.hasPermission("itemizer.material") && !player.isOp()) {
                player.sendMessage("§cYou do not have permission.");
                return true;
            }
            if (hasEmptyHand(player)) {
                player.sendMessage("§cYou must be holding an item to modify it.");
                return true;
            }
            if ((args.length > 1) && (args[1] != null)) {
                itemizer.getItemManager().changeMaterial(player, args[1]);
            } else {
                player.sendMessage("§6/" + label + " material <§fmaterial§6> §8- §eChange the material");
            }
            return true;
        } else if (args[0].equalsIgnoreCase("lore")) {
            if (!player.hasPermission("itemizer.lore") && !player.isOp()) {
                player.sendMessage("§cYou do not have permission.");
                return true;
            }
            if (hasEmptyHand(player)) {
                player.sendMessage("§cYou must be holding an item to modify it.");
                return true;
            }
            if (args.length < 3) {
                player.sendMessage("§7§m--------------§8[ §6§lLore Commands §8]§7§m--------------");
                player.sendMessage("§6/" + label + " lore add <§ftext§6> §8- §eAdd a lore line");
                player.sendMessage("§6/" + label + " lore remove <§findex§6> §8- §eRemove a lore line");
                player.sendMessage("§6/" + label + " lore change <§findex§6> <§ftext§6> §8- §eModify a lore line");
            } else {
                if (args[1].equalsIgnoreCase("add")) {
                    itemizer.getItemManager().addLore(player, args);
                } else if (args[1].equalsIgnoreCase("remove")) {
                    itemizer.getItemManager().removeLore(player, Integer.parseInt(args[2]));
                } else if (args[1].equalsIgnoreCase("change")) {
                    itemizer.getItemManager().changeLore(player, Integer.parseInt(args[2]), args);
                } else {
                    player.sendMessage("§7§m--------------§8[ §6§lLore Commands §8]§7§m--------------");
                    player.sendMessage("§6/" + label + " lore add <§ftext§6> §8- §eAdd a lore line");
                    player.sendMessage("§6/" + label + " lore remove <§findex§6> §8- §eRemove a lore line");
                    player.sendMessage("§6/" + label + " lore change <§findex§6> <§ftext§6> §8- §eModify a lore line");
                }
            }
            return true;
        } else if (args[0].equalsIgnoreCase("potion")) {
            if (!player.hasPermission("itemizer.potion") && !player.isOp()) {
                player.sendMessage("§cYou do not have permission.");
                return true;
            }
            if (isNotHoldingPotion(player)) {
                player.sendMessage("§cYou must be holding a potion to modify it.");
                return true;
            }
            if (args.length < 2) {
                player.sendMessage("§7§m--------------§8[ §6§lPotion Commands §8]§7§m--------------");
                player.sendMessage("§6/" + label + " potion add <§feffect§6> [§flevel§6] <§ftime[tick]§6> §8- §eAdd a potion effect");
                player.sendMessage("§6/" + label + " potion remove <§feffect§6> §8- §eRemove a potion effect");
                player.sendMessage("§6/" + label + " potion list §8- §eList all potion effects available");
            } else {
                if (args[1].equalsIgnoreCase("add")) {
                    itemizer.getItemManager().addPotionEffect(player, args);
                } else if (args[1].equalsIgnoreCase("remove")) {
                    itemizer.getItemManager().removePotions(player, args[2]);
                } else if (args[1].equalsIgnoreCase("list")) {
                    itemizer.getItemManager().listPotions(player);
                } else {
                    player.sendMessage("§7§m--------------§8[ §6§lPotion Commands §8]§7§m--------------");
                    player.sendMessage("§6/" + label + " potion add <§feffect§6> [§flevel§6] <§ftime[tick]§6> §8- §eAdd a potion effect");
                    player.sendMessage("§6/" + label + " potion remove <§feffect§6> §8- §eRemove a potion effect");
                    player.sendMessage("§6/" + label + " potion list §8- §eList all potion effects available");
                }
            }
            return true;
        } else if (args[0].equalsIgnoreCase("attr")) {
            if (!player.hasPermission("itemizer.attr") && !player.isOp()) {
                player.sendMessage("§cYou do not have permission.");
                return true;
            }
            if (hasEmptyHand(player)) {
                player.sendMessage("§cYou must be holding an item to modify it.");
                return true;
            }
            if (args.length < 2) {
                player.sendMessage("§7§m--------------§8[ §6§lAttribute Commands §8]§7§m--------------");
                player.sendMessage("§6/" + label + " attr add <§fname§6> <§fstrength§6> [§fslot§6] §8- §eAdd an attribute");
                player.sendMessage("§6/" + label + " attr remove <§fname§6> §8- §eRemove an attribute");
                player.sendMessage("§6/" + label + " attr list §8- §eList all active attributes");
                player.sendMessage("§6/" + label + " attr listall §8- §eList all available attributes");
            } else {
                if (args[1].equalsIgnoreCase("add")) {
                    itemizer.getItemManager().addAttr(player, args);
                } else if (args[1].equalsIgnoreCase("remove")) {
                    itemizer.getItemManager().attrRemove(player, args[2]);
                } else if (args[1].equalsIgnoreCase("list")) {
                    itemizer.getItemManager().attrList(player);
                } else if (args[1].equalsIgnoreCase("listall")) {
                    itemizer.getItemManager().attrListAll(player);
                } else {
                    player.sendMessage("§7§m--------------§8[ §6§lAttribute Commands §8]§7§m--------------");
                    player.sendMessage("§6/" + label + " attr add <§fname§6> <§fstrength§6> [§fslot§6] §8- §eAdd an attribute");
                    player.sendMessage("§6/" + label + " attr remove <§fname§6> §8- §eRemove an attribute");
                    player.sendMessage("§6/" + label + " attr list §8- §eList all active attributes");
                    player.sendMessage("§6/" + label + " attr listall §8- §eList all available attributes");
                }
            }
            return true;
        } else if (args[0].equalsIgnoreCase("flag")) {
            if (!player.hasPermission("itemizer.flag") && !player.isOp()) {
                player.sendMessage("§cYou do not have permission.");
                return true;
            }
            if (hasEmptyHand(player)) {
                player.sendMessage("§cYou must be holding an item to modify it.");
                return true;
            }
            if (args.length < 2) {
                player.sendMessage("§7§m--------------§8[ §6§lFlag Commands §8]§7§m--------------");
                player.sendMessage("§6/" + label + " flag add <§fname§6> §8- §eAdd a flag");
                player.sendMessage("§6/" + label + " flag remove <§fname§6> §8- §eRemove a flag");
                player.sendMessage("§6/" + label + " flag list §8- §eList all active flags");
                player.sendMessage("§6/" + label + " flag listall §8- §eList all available flags");
            } else {
                if (args[1].equalsIgnoreCase("add")) {
                    itemizer.getItemManager().addFlag(player, args[2]);
                } else if (args[1].equalsIgnoreCase("remove")) {
                    itemizer.getItemManager().removeFlag(player, args[2]);
                } else if (args[1].equalsIgnoreCase("list")) {
                    itemizer.getItemManager().showItemFlags(player);
                } else if (args[1].equalsIgnoreCase("listall")) {
                    itemizer.getItemManager().showAllFlags(player);
                } else {
                    player.sendMessage("§7§m--------------§8[ §6§lFlag Commands §8]§7§m--------------");
                    player.sendMessage("§6/" + label + " flag add <§fname§6> §8- §eAdd a flag");
                    player.sendMessage("§6/" + label + " flag remove <§fname§6> §8- §eRemove a flag");
                    player.sendMessage("§6/" + label + " flag list §8- §eList all active flags");
                    player.sendMessage("§6/" + label + " flag listall §8- §eList all available flags");
                }
            }
            return true;
        } else if (args[0].equalsIgnoreCase("enchant")) {
            if (!player.hasPermission("itemizer.enchant") && !player.isOp()) {
                player.sendMessage("§cYou do not have permission.");
                return true;
            }
            if (hasEmptyHand(player)) {
                player.sendMessage("§cYou must be holding an item to modify it.");
                return true;
            }
            if (args.length < 2) {
                player.sendMessage("§7§m--------------§8[ §6§lEnchantment Commands §8]§7§m--------------");
                player.sendMessage("§6/" + label + " enchant add <§fname§6> [§flevel§6] §8- §eAdd an enchantment");
                player.sendMessage("§6/" + label + " enchant remove <§fname§6> §8- §eRemove an enchantment");
                player.sendMessage("§6/" + label + " enchant list §8- §eList all active enchantments");
                player.sendMessage("§6/" + label + " enchant listall §8- §eList all available enchantments");
            } else {
                if (args[1].equalsIgnoreCase("add")) {
                    if (args.length != 4) {
                        player.sendMessage("§6/" + label + " enchant add <§fname§6> [§flevel§6] §8- §eAdd an enchantment");
                        return true;
                    }
                    itemizer.getItemManager().addEnchant(player, args[2], Integer.parseInt(args[3]));
                } else if (args[1].equalsIgnoreCase("remove")) {
                    itemizer.getItemManager().removeEnchant(player, args[2]);
                } else if (args[1].equalsIgnoreCase("list")) {
                    itemizer.getItemManager().showItemEnchants(player);
                } else if (args[1].equalsIgnoreCase("listall")) {
                    itemizer.getItemManager().showAllEnchants(player);
                } else {
                    player.sendMessage("§7§m--------------§8[ §6§lEnchantment Commands §8]§7§m--------------");
                    player.sendMessage("§6/" + label + " enchant add <§fname§6> [§flevel§6] §8- §eAdd an enchantment");
                    player.sendMessage("§6/" + label + " enchant remove <§fname§6> §8- §eRemove an enchantment");
                    player.sendMessage("§6/" + label + " enchant list §8- §eList all active enchantments");
                    player.sendMessage("§6/" + label + " enchant listall §8- §eList all available enchantments");
                }
            }
            return true;
        } else if (args[0].equalsIgnoreCase("title")) {
            if (!player.hasPermission("itemizer.title") && !player.isOp()) {
                player.sendMessage("§cYou do not have permission.");
                return true;
            }
            if (isNotHoldingBook(player)) {
                player.sendMessage("§cYou must be holding a book to modify it.");
                return true;
            }
            if (args.length != 2) {
                player.sendMessage("§6/" + label + " title <§ftitle§6> §8- §eSets the title of a book");
                return true;
            }
            itemizer.getItemManager().bookTitle(player, args);
            return true;
        } else if (args[0].equalsIgnoreCase("author")) {
            if (!player.hasPermission("itemizer.author") && !player.isOp()) {
                player.sendMessage("§cYou do not have permission.");
                return true;
            }
            if (isNotHoldingBook(player)) {
                player.sendMessage("§cYou must be holding a book to modify it.");
                return true;
            }
            if (args.length != 2) {
                player.sendMessage("§6/" + label + " author <§fname§6> §8- §eSets the author of a book");
                return true;
            }
            itemizer.getItemManager().bookAuthor(player, args);
            return true;
        } else if (args[0].equalsIgnoreCase("head")) {
            if (!player.hasPermission("itemizer.head") && !player.isOp()) {
                player.sendMessage("§cYou do not have permission.");
                return true;
            }
            if (isNotHoldingHead(player)) {
                player.sendMessage("§cYou must be holding a head to modify it.");
                return true;
            }
            if (args.length != 2) {
                player.sendMessage("§6/" + label + " head <§fname§6> §8- §eSets the owner of a head");
                return true;
            }
            player.sendMessage("§4Notice: §cThis method of setting a skull owner is deprecated.");
            player.sendMessage("§cUse of this command may lead to client / server crashes!");
            itemizer.getItemManager().skullName(player, args[1]);
            return true;
        } else if (args[0].equalsIgnoreCase("clearall")) {
            if (!player.hasPermission("itemizer.clearall") && !player.isOp()) {
                player.sendMessage("§cYou do not have permission.");
                return true;
            }
            if (hasEmptyHand(player)) {
                player.sendMessage("§cYou must be holding an item to modify it.");
                return true;
            }
            itemizer.getItemManager().clearAll(player);
            return true;
        }
        player.sendMessage("§7§m--------------§8[ §6§lItemizer Commands §8]§7§m--------------");
        player.sendMessage("§6/" + label + " name <§fname§6> §8- §eRename an item");
        player.sendMessage("§6/" + label + " material <§fmaterial§6> §8- §eChange the material");
        player.sendMessage("§6/" + label + " lore §8- §eEdit the lore");
        player.sendMessage("§6/" + label + " potion §8- §eEdit a potion");
        player.sendMessage("§6/" + label + " attr §8- §eEdit the attributes");
        player.sendMessage("§6/" + label + " flag §8- §eEdit the item flags");
        player.sendMessage("§6/" + label + " enchant §8- §eEnchant the item");
        player.sendMessage("§6/" + label + " title <§ftitle§6> §8- §eSets the title of a book");
        player.sendMessage("§6/" + label + " author <§fname§6> §8- §eSets the author of a book");
        player.sendMessage("§6/" + label + " head <§fname§6> §8- §eSets the owner of a head");
        player.sendMessage("§6/" + label + " clearall §8- §eClears all data of an item");
        return true;
    }

    private boolean isNotHoldingHead(Player player) {
        Material type = player.getEquipment().getItemInMainHand().getType();
        return type != Material.PLAYER_HEAD;
    }

    private boolean isNotHoldingBook(Player player) {
        Material type = player.getEquipment().getItemInMainHand().getType();
        return type != Material.WRITTEN_BOOK;
    }

    private boolean isNotHoldingPotion(Player player) {
        Material type = player.getEquipment().getItemInMainHand().getType();
        return type != Material.POTION && type != Material.LINGERING_POTION && type != Material.SPLASH_POTION;
    }

    private boolean hasEmptyHand(Player player) {
        ItemStack itemStack = player.getEquipment().getItemInMainHand();
        if (itemStack == null) return true;
        return itemStack.getType() == Material.AIR;
    }
}
