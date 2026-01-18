package fr.panncake.lootbox.managers;

import dev.dejvokep.boostedyaml.YamlDocument;
import fr.panncake.lootbox.PannLootbox;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public final class LootboxManager {

    private static final PannLootbox plugin = PannLootbox.getInstance();

    public static YamlDocument lootbox() {
        return plugin.getConfigManager().getLootbox();
    }

    public static ItemStack createLootbox(String id) {
        YamlDocument lootbox = lootbox();
        String materialName = lootbox.getString(id + ".material");
        if (materialName == null) return null;

        Material material = Material.matchMaterial(materialName);
        if (material == null || !material.isBlock()) return null;

        ItemStack item = new ItemStack(material);
        item.editMeta(meta -> {
            meta.displayName(MiniMessage.miniMessage().deserialize(id)
                    .decoration(TextDecoration.ITALIC, false));

            meta.getPersistentDataContainer().set(
                    new NamespacedKey(plugin, "lootbox"),
                    PersistentDataType.STRING,
                    id
            );
        });

        return item;
    }
}
