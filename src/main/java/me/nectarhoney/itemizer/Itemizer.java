package me.nectarhoney.itemizer;

import lombok.Getter;
import lombok.Setter;
import me.nectarhoney.itemizer.command.ItemizerCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class Itemizer extends JavaPlugin {

    @Getter @Setter public ItemManager itemManager;

    @Override
    public void onEnable() {
        getCommand("itemizer").setExecutor(new ItemizerCommand(this));
        setItemManager(new ItemManager());
    }

    @Override
    public void onDisable() {
        setItemManager(null);
    }
}
