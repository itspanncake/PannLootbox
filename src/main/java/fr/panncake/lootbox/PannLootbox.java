package fr.panncake.lootbox;

import fr.panncake.lootbox.commands.PluginCommands;
import fr.panncake.lootbox.config.LanguageConfiguration;
import fr.panncake.lootbox.config.PluginConfiguration;
import fr.panncake.lootbox.lootbox.LootboxManager;
import fr.panncake.lootbox.managers.LanguageManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class PannLootbox extends JavaPlugin {

    @Getter
    private static PannLootbox instance;
    @Getter
    private final Logger pluginLogger = LoggerFactory.getLogger("");

    @Getter
    private PluginConfiguration configuration;
    @Getter
    private LanguageManager languageManager;
    @Getter
    private LootboxManager lootboxManager;

    @Override
    public void onEnable() {
        this.pluginLogger.info("Enabling PannLootbox v{}", getPluginMeta().getVersion());
        instance = this;

        this.configuration = new PluginConfiguration(this);
        this.languageManager = new LanguageManager(this, configuration.getLanguage());

        this.lootboxManager = new LootboxManager(this);
        this.lootboxManager.reload();

        for (String lootboxId : lootboxManager.getLootboxes().keySet()) {
            if (lootboxManager.getLootbox(lootboxId).isEmpty()) {
                pluginLogger.error(" - Cannot load lootbox {}!", lootboxId);
            } else {
                pluginLogger.info(" - Lootbox loaded {}!", lootboxId);
            }
        }

        registerCommands();

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
                    "Main command of the plugin",
                    List.of("lb", "pannlootbox", "pannlb")
            );
        });
    }
}
