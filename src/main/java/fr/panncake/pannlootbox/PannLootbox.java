package fr.panncake.pannlootbox;

import fr.panncake.pannlootbox.managers.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PannLootbox extends JavaPlugin {

    private static PannLootbox instance;

    public final Logger logger = LoggerFactory.getLogger("");

    private ConfigManager configManager;

    @Override
    public void onEnable() {
        logger.info("Enabling PannLootbox v{}", getPluginMeta().getVersion());
        instance = this;

        this.configManager = new ConfigManager(this);

        logger.info("PannLootbox has been enabled!");
    }

    @Override
    public void onDisable() {
        logger.info("PannLootbox has been disabled!");
    }

    public ConfigManager getConfigManager() { return this.configManager; }

    public static PannLootbox getInstance() { return instance; }
}
