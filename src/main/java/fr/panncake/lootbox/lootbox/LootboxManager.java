package fr.panncake.lootbox.lootbox;

import dev.dejvokep.boostedyaml.YamlDocument;
import fr.panncake.lootbox.lootbox.enums.LootboxMaterial;
import fr.panncake.lootbox.lootbox.enums.LootboxType;
import fr.panncake.lootbox.lootbox.models.Lootbox;
import fr.panncake.lootbox.lootbox.models.LootboxItem;
import fr.panncake.lootbox.utils.LocationSerializer;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;

@Getter
public class LootboxManager {

    private final JavaPlugin plugin;
    private final Map<String, Lootbox> lootboxes = new HashMap<>();

    public LootboxManager(JavaPlugin plugin) {
        this.plugin = plugin;
        reload();
    }

    public Optional<Lootbox> getLootbox(String id) { return Optional.ofNullable(lootboxes.get(id)); }

    public void reload() {
        lootboxes.clear();
        File folder = new File(plugin.getDataFolder(), "lootboxes");
        if (!folder.exists()) folder.mkdirs();
        copyDefaults(folder);

        File[] files = folder.listFiles((d, n) -> n.endsWith(".yml"));
        if (files == null) return;

        for (File file : files) {
            try {
                YamlDocument yaml = YamlDocument.create(file);
                Lootbox lootbox = parse(yaml, file);
                lootboxes.put(lootbox.getId(), lootbox);
            } catch (Exception e) {
                plugin.getLogger().warning("Invalid lootbox: " + file.getName());
                e.printStackTrace();
            }
        }

        plugin.getLogger().info("Loaded " + lootboxes.size() + " lootboxes");
    }

    public void addLocation(Lootbox lootbox, Location loc) {
        String s = LocationSerializer.serialize(loc);
        if (!lootbox.getLocations().contains(s)) {
            lootbox.getLocations().add(s);
            saveLocations(lootbox);
        }
    }

    private void saveLocations(Lootbox lootbox) {
        try {
            YamlDocument yaml = YamlDocument.create(lootbox.getFile());
            yaml.set("lootbox-locations", lootbox.getLocations());
            yaml.save();
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to save locations: " + lootbox.getId());
            e.printStackTrace();
        }
    }

    private Lootbox parse(YamlDocument yaml, File file) {
        String id = require(yaml, "lootbox-id");
        String name = require(yaml, "lootbox-name");
        LootboxMaterial material = LootboxMaterial.valueOf(require(yaml, "lootbox-material").toUpperCase());
        LootboxType type = LootboxType.valueOf(require(yaml, "lootbox-type").toUpperCase());
        List<String> lore = yaml.getStringList("lootbox-lore");
        List<String> locations = yaml.getStringList("lootbox-locations");
        boolean glowing = yaml.getBoolean("lootbox-glowing");

        Map<Integer, LootboxItem> items = new HashMap<>();
        if (yaml.contains("lootbox-items")) {
            for (Object key : yaml.getSection("lootbox-items").getKeys()) {
                String path = "lootbox-items." + key;
                items.put(Integer.parseInt(key.toString()), LootboxItem.builder()
                        .material(require(yaml, path + ".material"))
                        .displayName(yaml.getString(path + ".display-name"))
                        .lore(yaml.getStringList(path + ".lore"))
                        .chance(yaml.getInt(path + ".chance"))
                        .amount(yaml.getString(path + ".amount"))
                        .glowing(yaml.getBoolean(path + ".glowing"))
                        .build());
            }
        }

        return Lootbox.builder()
                .file(file)
                .id(id)
                .name(name)
                .lore(lore)
                .material(material)
                .type(type)
                .locations(locations)
                .glowing(glowing)
                .items(items)
                .build();
    }

    private String require(YamlDocument yaml, String path) {
        if (!yaml.contains(path)) throw new IllegalArgumentException("Missing key: " + path);
        return yaml.getString(path);
    }

    private void copyDefaults(File folder) {
        if (Objects.requireNonNull(folder.list()).length > 0) return;
        String[] defaults = {"exampleLootbox.yml"};

        for (String fileName : defaults) {
            File dest = new File(folder, fileName);
            if (!dest.exists()) {
                try (InputStream is = plugin.getResource("lootboxes/" + fileName)) {
                    if (is != null) Files.copy(is, dest.toPath());
                } catch (Exception e) {
                    plugin.getLogger().severe("Failed to copy default lootbox: " + fileName);
                    e.printStackTrace();
                }
            }
        }
    }
}
