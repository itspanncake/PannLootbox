package fr.panncake.lootbox.config;

import dev.dejvokep.boostedyaml.YamlDocument;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;

@Getter
public final class PluginConfiguration {

    private static final String FILE_NAME = "configuration.yml";

    private final YamlDocument yaml;

    public PluginConfiguration(@NonNull JavaPlugin plugin) {
        this.yaml = loadYaml(plugin);
    }

    @SneakyThrows
    private YamlDocument loadYaml(JavaPlugin plugin) {
        InputStream resource = plugin.getResource(FILE_NAME);
        if (resource == null) {
            throw new IllegalStateException("Missing resource: " + FILE_NAME);
        }

        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) dataFolder.mkdirs();

        return YamlDocument.create(
                new File(dataFolder, FILE_NAME),
                resource
        );
    }

    public void save() {
        try {
            yaml.save();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to save configuration.yml", e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(@NonNull String path, T def) {
        return (T) yaml.get(path, def);
    }

    public void set(@NonNull String path, Object value) {
        yaml.set(path, value);
        save();
    }
}
