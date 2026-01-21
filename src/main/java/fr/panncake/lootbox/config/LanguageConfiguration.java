package fr.panncake.lootbox.config;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
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

        return YamlDocument.create(
                file,
                resource,
                GeneralSettings.DEFAULT,
                LoaderSettings.builder()
                        .setAutoUpdate(true)
                        .build()
        );
    }

    public String get(@NonNull String path) { return yaml.getString(path); }
    public List<String> getList(@NonNull String path) { return yaml.getStringList(path); }

    public void reload() throws Exception { yaml.reload(); }
}
