package fr.panncake.lootbox.config;

import dev.dejvokep.boostedyaml.YamlDocument;
import lombok.NonNull;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;

public final class LanguageConfiguration {

    private final YamlDocument yaml;

    public LanguageConfiguration(
            @NonNull JavaPlugin plugin,
            @NonNull String language
    ) {
        this.yaml = load(plugin, language);
    }

    @SneakyThrows
    private YamlDocument load(JavaPlugin plugin, String lang) {
        String path = "lang/" + lang + ".yml";

        InputStream resource = plugin.getResource(path);
        if (resource == null) {
            throw new IllegalStateException("Missing language file: " + path);
        }

        File langFile = new File(plugin.getDataFolder(), path);
        langFile.getParentFile().mkdirs();

        return YamlDocument.create(langFile, resource);
    }

    public Component get(@NonNull String path) {
        String value = yaml.getString(path, "<red>Missing lang key: " + path);
        return MiniMessage.miniMessage().deserialize(value);
    }

    public Component get(@NonNull String path, String def) {
        String value = yaml.getString(path, def);
        return MiniMessage.miniMessage().deserialize(value);
    }
}
