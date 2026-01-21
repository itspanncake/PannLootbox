package fr.panncake.lootbox.lootbox;

import fr.panncake.lootbox.lootbox.models.Lootbox;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

public class LootboxBuilder {

    public static ItemStack createItem(Lootbox lootbox) {
        Material material = Material.matchMaterial(lootbox.getMaterial().name());
        assert material != null;
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        if (lootbox.isGlowing()) {
            meta.addEnchant(Enchantment.UNBREAKING, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        Component displayName = MiniMessage.miniMessage().deserialize(lootbox.getName())
                .decoration(TextDecoration.ITALIC, false);
        meta.displayName(displayName);

        List<Component> lore = lootbox.getLore().stream()
                .map(MiniMessage.miniMessage()::deserialize)
                .map(c -> c.decoration(TextDecoration.ITALIC, false))
                .collect(Collectors.toList());
        meta.lore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        item.setItemMeta(meta);
        return item;
    }
}
