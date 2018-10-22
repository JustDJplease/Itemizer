package me.nectarhoney.itemizer;

import net.minecraft.server.v1_13_R2.*;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemManager {
    /**
     * Apply a name to the held item.
     * Status: completed
     *
     * @param player Affected player.
     * @param args   Command arguments.
     */
    public void setName(Player player, String[] args) {
        ItemStack item = player.getEquipment().getItemInMainHand();
        StringBuilder name = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            if (name.length() == 0) {
                name.append(args[i]);
            } else {
                name.append(" ").append(args[i]);
            }
        }
        name = new StringBuilder(ChatColor.translateAlternateColorCodes('&', name.toString()));

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name.toString());
        item.setItemMeta(meta);

        player.sendMessage("§eYour item has been renamed to \"§r" + name + "§e\".");
    }

    /**
     * Change the material of the held item.
     * Status: completed
     *
     * @param player Affected player.
     * @param name   New materials name.
     */
    public void changeMaterial(Player player, String name) {
        ItemStack item = player.getEquipment().getItemInMainHand();
        Material material;
        try {
            material = Material.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException ex) {
            player.sendMessage("§cInvalid material specified.");
            return;
        }
        item.setType(material);
        player.sendMessage("§eYour item now is now a \"§6" + WordUtils.capitalizeFully(material.toString()) + "§e\".");
    }

    /**
     * Add a lore line.
     * Status: completed
     *
     * @param player Affected player.
     * @param args   Command arguments.
     */
    public void addLore(Player player, String[] args) {
        ItemStack item = player.getEquipment().getItemInMainHand();
        StringBuilder lore = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            if (lore.length() == 0) {
                lore.append(args[i]);
            } else {
                lore.append(" ").append(args[i]);
            }
        }
        lore = new StringBuilder(ChatColor.translateAlternateColorCodes('&', lore.toString()));

        ItemMeta meta = item.getItemMeta();
        List<String> lores = new ArrayList<>();
        if (meta.getLore() != null) {
            lores = meta.getLore();
        }
        lores.add(lore.toString());
        meta.setLore(lores);
        item.setItemMeta(meta);

        player.sendMessage("§eYou have added the text \"§r" + ChatColor.translateAlternateColorCodes('&', lore.toString()) + "§e\" to the lore.");
    }

    /**
     * Remove a line from the lore.
     * Status: completed
     *
     * @param player Affected player.
     * @param index  Affected line.
     */
    public void removeLore(Player player, int index) {
        ItemStack item = player.getEquipment().getItemInMainHand();

        ItemMeta meta = item.getItemMeta();
        List<String> lores = new ArrayList<>(meta.getLore());
        try {
            lores.remove(index - 1);
        } catch (IndexOutOfBoundsException ex) {
            player.sendMessage("§cLore line not found.");
            return;
        }
        meta.setLore(lores);
        item.setItemMeta(meta);
        int removedLine = index - 1;

        player.sendMessage("§eRemoved line \"§6" + removedLine + "§e\" from the lore.");
    }

    /**
     * Modify an items lore line.
     * Status: completed
     *
     * @param player Affected player.
     * @param index  Affected lore line.
     * @param args   Command arguments.
     */
    public void changeLore(Player player, int index, String[] args) {
        ItemStack item = player.getEquipment().getItemInMainHand();
        StringBuilder lore = new StringBuilder();
        for (int i = 3; i < args.length; i++) {
            if (lore.length() == 0) {
                lore.append(args[i]);
            } else {
                lore.append(" ").append(args[i]);
            }
        }
        lore = new StringBuilder(ChatColor.translateAlternateColorCodes('&', lore.toString()));

        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLore()) {
            player.sendMessage("§cItem does not have a lore.");
            return;
        }
        List<String> lores = new ArrayList<>(meta.getLore());
        try {
            lores.set(index - 1, lore.toString());
        } catch (IndexOutOfBoundsException ex) {
            player.sendMessage("§cLore line not found.");
            return;
        }
        meta.setLore(lores);
        item.setItemMeta(meta);
        int removedLine = index - 1;

        player.sendMessage("§eChanged lore line \"§6" + removedLine + "§e\" to \"§r" + lore.toString() + "§e\".");
    }

    /**
     * Adds an effect to a potion.
     * Status: completed
     *
     * @param player Affected player.
     * @param args   Command arguments.
     */
    public void addPotionEffect(Player player, String[] args) {
        ItemStack item = player.getEquipment().getItemInMainHand();
        PotionEffect pot;
        PotionEffectType type = PotionEffectType.getByName(args[2].toUpperCase());
        if (type == null) {
            player.sendMessage("§cInvalid potion type.");
            return;
        }
        try {
            pot = new PotionEffect(type, Integer.parseInt(args[4]), Integer.parseInt(args[3]));
        } catch (NumberFormatException ex) {
            player.sendMessage("§cInvalid time or duration.");
            return;
        }
        PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
        if (potionMeta.hasCustomEffect(pot.getType())) {
            player.sendMessage("§cThis potion already has the effect " + WordUtils.capitalizeFully(pot.getType().getName()) + ".");
            return;
        }
        potionMeta.addCustomEffect(pot, false);
        item.setItemMeta(potionMeta);
        player.sendMessage("§eAdded the effect \"§6" + WordUtils.capitalizeFully(pot.getType().getName()) + "§e\" to the potion.");
    }

    /**
     * Removes an effect from a potion.
     * Status: completed
     *
     * @param player Affected player.
     * @param string Command arguments
     */
    public void removePotions(Player player, String string) {
        ItemStack item = player.getEquipment().getItemInMainHand();
        PotionEffectType potionEffect = PotionEffectType.getByName(string.toUpperCase());
        if (potionEffect == null) {
            player.sendMessage("§cInvalid potion effect specified.");
            return;
        }
        PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
        if (!potionMeta.hasCustomEffect(potionEffect)) {
            player.sendMessage("§cThis potion does not have the " + potionEffect.getName() + " effect.");
            return;
        }
        potionMeta.removeCustomEffect(potionEffect);
        item.setItemMeta(potionMeta);
        player.sendMessage("§eRemoved the effect \"§6" + WordUtils.capitalizeFully(potionEffect.getName()) + "§e\" from the potion.");
    }

    /**
     * Shows a list of all available potion effect types.
     * Status: completed
     *
     * @param player Affected player.
     */
    public void listPotions(Player player) {
        StringBuilder sb = new StringBuilder();
        PotionEffectType[] arrayOfPotionEffectType;
        int j = (arrayOfPotionEffectType = PotionEffectType.values()).length;
        for (int i = 0; i < j; i++) {
            PotionEffectType eff = arrayOfPotionEffectType[i];
            if (eff != null) {
                sb.append("§e").append(WordUtils.capitalizeFully(eff.getName())).append(" §8▪ ");
            }
        }
        sb.setLength(sb.length() - 5);
        player.sendMessage(sb.toString());
    }

    /**
     * Sets a title of a book.
     * Status: completed
     *
     * @param player Affected player.
     * @param args   Command arguments.
     */
    public void bookTitle(Player player, String[] args) {
        StringBuilder name = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            if (name.length() == 0) {
                name.append(args[i]);
            } else {
                name.append(" ").append(args[i]);
            }
        }
        name = new StringBuilder(ChatColor.translateAlternateColorCodes('&', name.toString()));
        ItemStack item = player.getEquipment().getItemInMainHand();

        BookMeta bookMeta = (BookMeta) item.getItemMeta();
        bookMeta.setTitle(name.toString());
        item.setItemMeta(bookMeta);

        player.sendMessage("§eSet the title of the book to \"§r" + name.toString() + "§e\".");
    }

    /**
     * Sets a author of a book.
     * Status: completed
     *
     * @param player Affected player.
     * @param args   Command arguments.
     */
    public void bookAuthor(Player player, String[] args) {
        StringBuilder name = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            if (name.length() == 0) {
                name.append(args[i]);
            } else {
                name.append(" ").append(args[i]);
            }
        }
        name = new StringBuilder(ChatColor.translateAlternateColorCodes('&', name.toString()));
        ItemStack item = player.getEquipment().getItemInMainHand();

        BookMeta bookMeta = (BookMeta) item.getItemMeta();
        bookMeta.setAuthor(name.toString());
        item.setItemMeta(bookMeta);

        player.sendMessage("§eSet the author of the book to \"§r" + name.toString() + "§e\".");
    }

    /**
     * Sets the owner of a skull.
     * Status: completed
     *
     * @param player Affected player.
     * @param name   Skull owners name.
     */
    public void skullName(Player player, String name) {
        ItemStack item = player.getInventory().getItemInMainHand();
        SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
        OfflinePlayer offline = Bukkit.getOfflinePlayer(name);
        skullMeta.setOwningPlayer(offline);
        item.setItemMeta(skullMeta);
        player.sendMessage("§eSet the skull-owner of the head to \"§r" + offline.getName() + "§e\".");
    }

    /**
     * Removes all metadata from an item.
     * Status: completed
     *
     * @param player Affected player.
     */
    public void clearAll(Player player) {
        ItemStack item = player.getEquipment().getItemInMainHand();
        item.setItemMeta(null);

        player.sendMessage("§eRemoved all data from the item.");
    }

    /**
     * Shows a list of all available item flags.
     * Status: completed
     *
     * @param player Affected player.
     */
    public void showAllFlags(Player player) {
        StringBuilder sb = new StringBuilder();
        ItemFlag[] arrayOfItemFlag;
        int j = (arrayOfItemFlag = ItemFlag.values()).length;
        for (int i = 0; i < j; i++) {
            ItemFlag flag = arrayOfItemFlag[i];
            if (flag != null) {
                sb.append("§e").append(WordUtils.capitalizeFully(flag.toString())).append(" §8▪ ");
            }
        }
        sb.setLength(sb.length() - 5);
        player.sendMessage(sb.toString());
    }

    /**
     * Shows a list of all active item flags of an item.
     * Status: completed
     *
     * @param player Affected player.
     */
    public void showItemFlags(Player player) {
        ItemStack item = player.getEquipment().getItemInMainHand();
        if ((item.hasItemMeta()) && (item.getItemMeta().getItemFlags() != null)) {
            StringBuilder sb = new StringBuilder();
            for (ItemFlag flag : item.getItemMeta().getItemFlags()) {
                if (flag != null) {
                    sb.append("§e").append(WordUtils.capitalizeFully(flag.toString())).append(" §8▪ ");
                }
            }
            try {
                sb.setLength(sb.length() - 5);
            } catch (StringIndexOutOfBoundsException ex) {
                player.sendMessage("§cThis item does not have any item flags.");
                return;
            }
            player.sendMessage(sb.toString());
        } else {
            player.sendMessage("§cThis item does not have any item flags.");
        }
    }

    /**
     * Adds an item flag to an item.
     * Status: completed
     *
     * @param player Affected player.
     * @param string New item flag.
     */
    public void addFlag(Player player, String string) {
        ItemStack item = player.getEquipment().getItemInMainHand();
        ItemFlag flag;
        try {
            flag = ItemFlag.valueOf(string.toUpperCase());
        } catch (IllegalArgumentException ex) {
            player.sendMessage("§cInvalid flag specified.");
            return;
        }
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(flag);
        item.setItemMeta(meta);

        player.sendMessage("§eAdded the flag \"§6" + WordUtils.capitalizeFully(flag.toString()) + "§e\" to the item.");
    }

    /**
     * Removes an item flag from an item.
     * Status: completed
     *
     * @param player Affected player.
     * @param string New item flag.
     */
    public void removeFlag(Player player, String string) {
        ItemStack item = player.getEquipment().getItemInMainHand();
        ItemFlag flag;
        try {
            flag = ItemFlag.valueOf(string.toUpperCase());
        } catch (IllegalArgumentException ex) {
            player.sendMessage("§cInvalid flag specified.");
            return;
        }
        ItemMeta meta = item.getItemMeta();
        meta.removeItemFlags(flag);
        item.setItemMeta(meta);

        player.sendMessage("§eRemoved the flag \"§6" + WordUtils.capitalizeFully(flag.toString()) + "§e\" from the item.");
    }

    /**
     * Adds an enchantment to an item.
     * Status: completed
     *
     * @param player Affected player.
     * @param string Enchantment.
     * @param level  Strength.
     */
    public void addEnchant(Player player, String string, int level) {
        ItemStack item = player.getEquipment().getItemInMainHand();
        Enchantment ench;
        try {
            ench = Enchantment.getByName(string.toUpperCase());
        } catch (Exception ex) {
            player.sendMessage("§cInvalid enchantment specified.");
            return;
        }
        if (ench == null) {
            player.sendMessage("§cInvalid enchantment specified.");
            return;
        }
        item.addUnsafeEnchantment(ench, level);

        player.sendMessage("§eAdded the enchantment \"§6" + WordUtils.capitalizeFully(ench.getName()) + "§e\" to the item.");
    }

    /**
     * Shows a list of all available enchantments.
     * Status: completed
     *
     * @param player Affected player.
     */
    public void showAllEnchants(Player player) {
        StringBuilder sb = new StringBuilder();
        Enchantment[] arrayOfEnchantment;
        int j = (arrayOfEnchantment = Enchantment.values()).length;
        for (int i = 0; i < j; i++) {
            Enchantment ench = arrayOfEnchantment[i];
            if (ench != null) {
                sb.append("§e").append(WordUtils.capitalizeFully(ench.getName())).append(" §8▪ ");
            }
        }
        sb.setLength(sb.length() - 5);
        player.sendMessage(sb.toString());
    }

    /**
     * Shows a list of all active enchantments of the item.
     * Status: completed
     *
     * @param player Affected player.
     */
    public void showItemEnchants(Player player) {
        ItemStack item = player.getEquipment().getItemInMainHand();
        if ((item.hasItemMeta()) && (item.getItemMeta().getEnchants() != null)) {
            StringBuilder sb = new StringBuilder();
            for (Enchantment ench : item.getItemMeta().getEnchants().keySet()) {
                sb.append("§e").append(WordUtils.capitalizeFully(ench.getName())).append(" §8▪ ");
            }
            try {
                sb.setLength(sb.length() - 5);
            } catch (StringIndexOutOfBoundsException ex) {
                player.sendMessage("§cThis item does not have any enchantments.");
                return;
            }
            player.sendMessage(sb.toString());
        } else {
            player.sendMessage("§cThis item does not have any enchantments.");
        }
    }

    /**
     * Removes an enchantment from an item.
     * Status: completed
     *
     * @param player Affected player.
     * @param string The removed enchantment.
     */
    public void removeEnchant(Player player, String string) {
        ItemStack item = player.getEquipment().getItemInMainHand();
        Enchantment ench;
        try {
            ench = Enchantment.getByName(string.toUpperCase());
        } catch (Exception ex) {
            player.sendMessage("§cInvalid enchantment specified.");
            return;
        }
        if ((item.hasItemMeta()) && (item.getItemMeta().getEnchants() != null)) {
            if (item.getEnchantments().containsKey(ench)) {
                item.removeEnchantment(ench);
                player.sendMessage("§eRemoved the enchantment \"§6" + WordUtils.capitalizeFully(ench.getName()) + "§e\" from the item.");
            } else {
                player.sendMessage("§cThis item does not have this enchantment.");
            }
        } else {
            player.sendMessage("§cThis item does not have any enchantments.");
        }
    }

    /**
     * Version specific NMS starts here!
     */
    public enum Attributes {
        DAMAGE(0, "generic.attackDamage"), MOVEMENT_SPEED(2, "generic.movementSpeed"), ATTACK_SPEED(2, "generic.attackSpeed"), KNOCKBACK_RESISTANCE(2, "generic.knockbackResistance"), MAX_HEALTH(0, "generic.maxHealth"), ARMOR(0, "generic.armor"), LUCK(0, "generic.luck");

        private final int op;
        private final String name;

        Attributes(int op, String name) {
            this.op = op;
            this.name = name;
        }

        private static Attributes get(String s) {
            Attributes[] arrayOfAttributes;
            int j = (arrayOfAttributes = values()).length;
            for (int i = 0; i < j; i++) {
                Attributes a = arrayOfAttributes[i];
                if (a.name().toLowerCase().equalsIgnoreCase(s)) {
                    return a;
                }
            }
            return null;
        }

        private static Attributes getByMCName(String s) {
            Attributes[] arrayOfAttributes;
            int j = (arrayOfAttributes = values()).length;
            for (int i = 0; i < j; i++) {
                Attributes a = arrayOfAttributes[i];
                if (a.name.equalsIgnoreCase(s)) {
                    return a;
                }
            }
            return null;
        }
    }

    /**
     * Private
     */
    private NBTTagList getAttrList(net.minecraft.server.v1_13_R2.ItemStack stack) {
        if (stack.getTag() == null) {
            stack.setTag(new NBTTagCompound());
        }
        NBTTagList attrmod = stack.getTag().getList("AttributeModifiers", 10);
        if (attrmod == null) {
            stack.getTag().set("AttributeModifiers", new NBTTagList());
        }
        return stack.getTag().getList("AttributeModifiers", 10);
    }

    /**
     * Adds an attribute to an item.
     * Status: completed
     *
     * @param player Affected player.
     * @param args   Command arguments.
     */
    public void addAttr(Player player, String[] args) {
        int op;
        if (args.length < 4) {
            player.sendMessage("§6/itemizer attr add <§fname§6> <§fstrength§6> [§fslot§6] §8- §eAdd an attribute");
            return;
        }
        Attributes a = Attributes.get(args[2].toUpperCase());
        if (a == null) {
            player.sendMessage("§cInvalid attribute specified.");
            return;
        }
        double amount;
        try {
            amount = Double.parseDouble(args[3]);
        } catch (NumberFormatException nfe) {
            player.sendMessage("§cInvalid strength specified.");
            return;
        }
        net.minecraft.server.v1_13_R2.ItemStack nms = CraftItemStack.asNMSCopy(player.getInventory().getItemInMainHand());
        NBTTagList attrmod = getAttrList(nms);
        for (NBTBase anAttrmod : attrmod) {
            NBTTagCompound c = (NBTTagCompound) anAttrmod;
            if (c.getString("Name").equals(args[2].toUpperCase())) {
                player.sendMessage("§cThis item already contains this attribute.");
                return;
            }
        }
        NBTTagCompound c = new NBTTagCompound();
        c.set("Name", new NBTTagString(args[2].toUpperCase()));
        c.set("AttributeName", new NBTTagString(a.name));
        c.set("Amount", new NBTTagDouble(amount));
        op = a.op;
        c.set("Operation", new NBTTagInt(op));
        UUID randUUID = UUID.randomUUID();
        c.set("UUIDMost", new NBTTagLong(randUUID.getMostSignificantBits()));
        c.set("UUIDLeast", new NBTTagLong(randUUID.getLeastSignificantBits()));
        if (args.length == 5) {
            List<String> options = new ArrayList<>();
            options.add("mainhand");
            options.add("offhand");
            options.add("feet");
            options.add("legs");
            options.add("chest");
            options.add("head");
            if (options.contains(args[4].toLowerCase())) {
                c.set("Slot", new NBTTagString(args[4].toLowerCase()));
            } else {
                listOptions(player);
                return;
            }
        }
        attrmod.add(c);
        assert nms.getTag() != null;
        nms.getTag().set("AttributeModifiers", attrmod);
        org.bukkit.inventory.ItemStack i = CraftItemStack.asCraftMirror(nms);
        player.getInventory().setItemInMainHand(i);
        player.sendMessage("§eAdded the attribute to the item.");
    }

    /**
     * Removes an attribute from an item.
     * Status: completed
     *
     * @param player Affected player.
     * @param string Command arguments.
     */
    public void attrRemove(Player player, String string) {
        net.minecraft.server.v1_13_R2.ItemStack nms = CraftItemStack.asNMSCopy(player.getInventory().getItemInMainHand());
        NBTTagList attrmod = getAttrList(nms);
        NBTTagList nlist = new NBTTagList();
        boolean r = false;
        for (NBTBase anAttrmod : attrmod) {
            NBTTagCompound c = (NBTTagCompound) anAttrmod;
            if (!c.getString("Name").equals(string.toUpperCase())) {
                nlist.add(anAttrmod);
            } else {
                r = true;
            }
        }
        if (!r) {
            player.sendMessage("§cInvalid attribute specified.");
            return;
        }
        assert nms.getTag() != null;
        nms.getTag().set("AttributeModifiers", nlist);
        org.bukkit.inventory.ItemStack i = CraftItemStack.asCraftMirror(nms);
        player.getInventory().setItemInMainHand(i);
        player.sendMessage("§eRemoved the attribute from the item.");
    }

    /**
     * Shows a list of all active attributes of an item.
     * Status: completed
     *
     * @param player Affected player.
     */
    public void attrList(Player player) {
        net.minecraft.server.v1_13_R2.ItemStack nms = CraftItemStack.asNMSCopy(player.getInventory().getItemInMainHand());
        NBTTagList attrmod = getAttrList(nms);
        if (attrmod.size() == 0) {
            player.sendMessage("§cThis item has no attributes.");
            return;
        }
        for (NBTBase anAttrmod : attrmod) {
            NBTTagCompound c = (NBTTagCompound) anAttrmod;
            player.sendMessage(" §8▪ §e" + Attributes.getByMCName(WordUtils.capitalizeFully(c.getString("AttributeName"))) + " §8(§6" + c.getDouble("Amount") + "§8)");
        }
    }

    /**
     * Private
     */
    private void listOptions(Player player) {
        List<String> options = new ArrayList<>();
        options.add("mainhand");
        options.add("offhand");
        options.add("feet");
        options.add("legs");
        options.add("chest");
        options.add("head");

        StringBuilder sb = new StringBuilder();
        for (String s : options) {
            sb.append("§e").append(WordUtils.capitalizeFully(s)).append(" §8▪ ");
        }
        sb.setLength(sb.length() - 5);
        player.sendMessage(sb.toString());
    }

    /**
     * Shows a list of all available attributes.
     * Status: completed
     *
     * @param player Affected player.
     */
    public void attrListAll(Player player) {
        StringBuilder sb = new StringBuilder();
        Attributes[] arrayOfAttributes;
        int j = (arrayOfAttributes = Attributes.values()).length;
        for (int i = 0; i < j; i++) {
            Attributes s = arrayOfAttributes[i];
            sb.append("§e").append(WordUtils.capitalizeFully(s.toString())).append(" §8▪ ");
        }
        sb.setLength(sb.length() - 5);
        player.sendMessage(sb.toString());
    }
}
