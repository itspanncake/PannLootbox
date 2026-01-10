package fr.panncake.pannlootbox;

import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PannLootbox extends JavaPlugin {

    private static PannLootbox instance;

    public final Logger logger = LoggerFactory.getLogger("");


    @Override
    public void onEnable() {
        logger.info("Enabling PannLootbox v{}", getPluginMeta().getVersion());
        instance = this;

        logger.info("PannLootbox has been enabled!");
    }

    @Override
    public void onDisable() {
        logger.info("PannLootbox has been disabled!");
    }

    public static PannLootbox getInstance() { return instance; }
}
