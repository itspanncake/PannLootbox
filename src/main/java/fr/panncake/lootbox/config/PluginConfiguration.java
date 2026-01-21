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
        this.yaml = load(plugin);
    }

    @SneakyThrows
    private YamlDocument load(JavaPlugin plugin) {
        InputStream resource = plugin.getResource(FILE_NAME);
        if (resource == null) throw new IllegalStateException("Missing configuration resource: " + FILE_NAME);

        File file = new File(plugin.getDataFolder(), FILE_NAME);
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();

        return YamlDocument.create(file, resource);
    }

    public <T> Object get(@NonNull String path, T def) {
        return yaml.get(path, def);
    }

    public void set(@NonNull String path, Object value) {
        yaml.set(path, value);
        save();
    }

    public void save() {
        try { yaml.save(); }
        catch (Exception e) { throw new IllegalStateException("Failed to save configuration.yml", e); }
    }

    public void reload() throws Exception { yaml.reload(); }

    public String getLanguage() { return yaml.getString("language", "en"); }
}
