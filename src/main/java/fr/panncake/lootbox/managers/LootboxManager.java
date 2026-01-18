package fr.panncake.lootbox.managers;

import dev.dejvokep.boostedyaml.YamlDocument;
import fr.panncake.lootbox.PannLootbox;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public final class LootboxManager {

    private static final PannLootbox plugin = PannLootbox.getInstance();

    public static YamlDocument lootbox() {
        return plugin.getConfigManager().getLootbox();
    }

    public static ItemStack createLootbox(String id) {
        YamlDocument lootbox = lootbox();
        if (!lootbox.contains(id)) return null;

        Material lootboxMaterial = Material.matchMaterial(lootbox.getString(id + ".material"));
        if (lootboxMaterial == null || !lootboxMaterial.isBlock() || (lootboxMaterial != Material.CHEST && lootboxMaterial != Material.BARREL)) {
            plugin.logger.warn("The lootbox {} does not have a valid material configured or is not supported.", lootboxMaterial);
            return null;
        }

        ItemStack lbItem = new ItemStack(lootboxMaterial);
        ItemMeta lbMeta = lbItem.getItemMeta();

        // TODO: Add more lootbox customization options
        lbMeta.displayName(MiniMessage.miniMessage().deserialize(id).decorate(TextDecoration.ITALIC.withState(TextDecoration.State.FALSE).decoration()));

        NamespacedKey key = new NamespacedKey(plugin, "lootbox");
        lbMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, id);

        lbItem.setItemMeta(lbMeta);

        return lbItem;
    }
}
