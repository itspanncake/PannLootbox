package fr.panncake.lootbox.lootbox.models;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class LootboxItem {
    private final String material;
    private final String displayName;
    private final List<String> lore;
    private final int chance;
    private final String amount;
    private final boolean glowing;
}
