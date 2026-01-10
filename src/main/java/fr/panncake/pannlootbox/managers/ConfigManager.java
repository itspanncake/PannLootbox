package fr.panncake.pannlootbox.managers;

import dev.dejvokep.boostedyaml.YamlDocument;
import fr.panncake.pannlootbox.PannLootbox;
import fr.panncake.pannlootbox.config.YamlConfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public final class ConfigManager {

    private final PannLootbox plugin;

    private YamlConfig mainConfig;
    private YamlConfig langConfig;

    public ConfigManager(PannLootbox plugin) {
        this.plugin = plugin;
        loadAll();
    }

    public void loadAll() {
        loadMainConfig();
        loadLangConfig();
    }

    private void loadMainConfig() {
        String name = "configuration.yml";

        try {
            mainConfig = new YamlConfig(
                    new File(plugin.getDataFolder(), name),
                    requireResource(name),
                    YamlConfig.versioned("config-version")
            );
            mainConfig.load();
        } catch (IOException e) {
            plugin.logger.error("Failed to load '{}'", name, e);
        }
    }

    private void loadLangConfig() {
        String langCode = getConfig().getString("language", "en");
        String resourcePath = "lang/" + langCode + ".yml";

        InputStream defaults = plugin.getResource(resourcePath);
        if (defaults == null) {
            plugin.logger.warn("Lang '{}' not found, fallback to 'en.yml'", resourcePath);
            resourcePath = "lang/en.yml";
            defaults = requireResource(resourcePath);
        }

        try {
            langConfig = new YamlConfig(
                    new File(plugin.getDataFolder(), resourcePath),
                    defaults,
                    YamlConfig.none()
            );
            langConfig.load();
        } catch (IOException e) {
            plugin.logger.error("Failed to load lang file '{}'", resourcePath, e);
        }
    }

    public void reload() {
        try {
            mainConfig.reload();
            loadLangConfig();
        } catch (IOException e) {
            plugin.logger.error("Failed to reload configs", e);
        }
    }

    public YamlDocument getConfig() {
        return mainConfig.get();
    }

    public YamlDocument getLang() {
        return langConfig.get();
    }

    private InputStream requireResource(String path) {
        InputStream stream = plugin.getResource(path);
        if (stream == null)
            throw new IllegalStateException("Resource '" + path + "' not found in plugin JAR");
        return stream;
    }
}
