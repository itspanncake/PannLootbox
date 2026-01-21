package fr.panncake.lootbox.lootbox.models;

import fr.panncake.lootbox.lootbox.enums.LootboxMaterial;
import fr.panncake.lootbox.lootbox.enums.LootboxType;
import lombok.Builder;
import lombok.Getter;

import java.io.File;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class Lootbox {
    private final File file;
    private final String id;
    private final String name;
    private final List<String> lore;
    private final LootboxMaterial material;
    private final LootboxType type;
    private final List<String> locations;
    private final boolean glowing;
    private final Map<Integer, LootboxItem> items;
}
