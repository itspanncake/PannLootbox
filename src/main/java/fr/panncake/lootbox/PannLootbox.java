package fr.panncake.lootbox;

import fr.panncake.lootbox.commands.PluginCommands;
import fr.panncake.lootbox.config.LanguageConfiguration;
import fr.panncake.lootbox.config.PluginConfiguration;
import fr.panncake.lootbox.listeners.LootboxPlaceListener;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PannLootbox extends JavaPlugin {

    @Getter
    private static PannLootbox instance;
    @Getter
    private final Logger pluginLogger = LoggerFactory.getLogger("");

    @Getter
    private PluginConfiguration configuration;
    @Getter
    private LanguageConfiguration language;

    @Override
    public void onEnable() {
        this.pluginLogger.info("Enabling PannLootbox v{}", getPluginMeta().getVersion());
        instance = this;

        this.configuration = new PluginConfiguration(this);
        this.language = new LanguageConfiguration(this, configuration.get("language", "en"));

        registerCommands();

        this.getServer().getPluginManager().registerEvents(new LootboxPlaceListener(), this);

        this.pluginLogger.info("PannLootbox has been enabled!");
    }

    @Override
    public void onDisable() {
        this.pluginLogger.info("PannLootbox has been disabled!");
    }

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
