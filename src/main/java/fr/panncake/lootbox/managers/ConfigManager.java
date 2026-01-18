package fr.panncake.lootbox.managers;

import dev.dejvokep.boostedyaml.YamlDocument;
import fr.panncake.lootbox.PannLootbox;
import fr.panncake.lootbox.config.YamlConfig;

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
        File langFile = new File(plugin.getDataFolder(), "lang/" + langCode + ".yml");

        InputStream defaults = plugin.getResource("lang/" + langCode + ".yml");

        if (!langFile.exists()) {
            if (defaults == null) {
                plugin.logger.warn("Lang '{}' not found in JAR, fallback to 'en.yml'", langCode);
                langFile = new File(plugin.getDataFolder(), "lang/en.yml");
                defaults = requireResource("lang/en.yml");
            } else {
                langFile.getParentFile().mkdirs();
            }
        }

        try {
            langConfig = new YamlConfig(langFile, defaults, YamlConfig.none());
            langConfig.load();
        } catch (IOException e) {
            plugin.logger.error("Failed to load lang file '{}'", langFile.getName(), e);
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
