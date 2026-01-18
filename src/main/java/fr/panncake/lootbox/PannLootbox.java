package fr.panncake.lootbox;

import fr.panncake.lootbox.commands.PluginCommands;
import fr.panncake.lootbox.listeners.LootboxPlaceListener;
import fr.panncake.lootbox.managers.ConfigManager;
import fr.panncake.lootbox.managers.LootboxManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PannLootbox extends JavaPlugin {

    private static PannLootbox instance;

    public final Logger logger = LoggerFactory.getLogger("");

    private ConfigManager configManager;

    @Override
    public void onEnable() {
        logger.info("Enabling PannLootbox v{}", getPluginMeta().getVersion());
        instance = this;

        this.configManager = new ConfigManager(this);
        registerCommands();

        this.getServer().getPluginManager().registerEvents(new LootboxPlaceListener(), this);

        logger.info("PannLootbox has been enabled!");
    }

    @Override
    public void onDisable() {
        logger.info("PannLootbox has been disabled!");
    }

    public ConfigManager getConfigManager() { return this.configManager; }

    public static PannLootbox getInstance() { return instance; }

    private void registerCommands() {
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(
                    PluginCommands.register(),
                    "Contains main commands of the plugin",
                    List.of("lb", "pannlootbox", "pannlb")
            );
        });
    }
}
