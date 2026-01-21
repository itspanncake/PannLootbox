package fr.panncake.lootbox.config;

import dev.dejvokep.boostedyaml.YamlDocument;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.util.List;

@Getter
public final class LanguageConfiguration {

    private final YamlDocument yaml;

    public LanguageConfiguration(@NonNull JavaPlugin plugin, @NonNull String language) {
        this.yaml = load(plugin, language);
    }

    @SneakyThrows
    private YamlDocument load(JavaPlugin plugin, String lang) {
        String path = "lang/" + lang + ".yml";
        InputStream resource = plugin.getResource(path);
        if (resource == null) throw new IllegalStateException("Missing language file: " + path);

        File file = new File(plugin.getDataFolder(), path);
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();

        return YamlDocument.create(file, resource);
    }

    public String get(@NonNull String path) { return yaml.getString(path, "<red>Missing lang key: " + path); }
    public String get(@NonNull String path, String def) { return yaml.getString(path, def); }
    public List<String> getList(@NonNull String path) { return yaml.getStringList(path); }
    public List<String> getList(@NonNull String path, List<String> def) {
        List<String> list = yaml.getStringList(path);
        return list.isEmpty() ? def : list;
    }
    public void reload() throws Exception { yaml.reload(); }
}
