package fr.panncake.lootbox.config;

import dev.dejvokep.boostedyaml.YamlDocument;

import java.util.List;

public class LangProvider {
    private final YamlDocument lang;

    public LangProvider(YamlDocument lang) {
        this.lang = lang;
    }

    public String get(String path) {
        if (path == null) return null;
        return lang.getString(path);
    }

    public List<String> getList(String path) {
        if (path == null) return List.of();
        return lang.getStringList(path);
    }
}
