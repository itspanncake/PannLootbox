package fr.panncake.lootbox.config;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public final class YamlConfig {

    private final File file;
    private final InputStream defaults;
    private final UpdaterSettings updater;

    private YamlDocument document;

    public YamlConfig(File file, InputStream defaults, UpdaterSettings updater) {
        this.file = file;
        this.defaults = defaults;
        this.updater = updater;
    }

    public void load() throws IOException {
        document = YamlDocument.create(
                file,
                defaults,
                GeneralSettings.DEFAULT,
                LoaderSettings.builder().setAutoUpdate(true).build(),
                DumperSettings.DEFAULT,
                updater
        );
    }

    public void reload() throws IOException {
        document.reload();
    }

    public YamlDocument get() {
        return document;
    }

    public static UpdaterSettings versioned(String key) {
        return UpdaterSettings.builder()
                .setVersioning(new BasicVersioning(key))
                .build();
    }

    public static UpdaterSettings none() {
        return UpdaterSettings.DEFAULT;
    }
}
