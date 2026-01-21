package fr.panncake.lootbox.managers;

import fr.panncake.lootbox.config.LanguageConfiguration;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class LanguageManager {

    private final JavaPlugin plugin;
    private LanguageConfiguration current;
    private String currentLang;

    public LanguageManager(@NonNull JavaPlugin plugin, @NonNull String defaultLang) {
        this.plugin = plugin;
        this.currentLang = defaultLang;
        load();
    }

    public void load() {
        this.current = new LanguageConfiguration(plugin, currentLang);
    }

    public void reload() throws Exception {
        this.current.reload();
    }

    public void setLanguage(@NonNull String lang) {
        this.currentLang = lang;
        load();
    }
}
