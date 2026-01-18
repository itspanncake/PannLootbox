package fr.panncake.lootbox.listeners;

import dev.dejvokep.boostedyaml.YamlDocument;
import fr.panncake.lootbox.PannLootbox;
import fr.panncake.lootbox.managers.LootboxManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class LootboxPlaceListener implements Listener {

    private final PannLootbox plugin = PannLootbox.getInstance();

    @EventHandler
    public void onLootboxPlace(BlockPlaceEvent e) {
        ItemStack item = e.getItemInHand();
        if (!item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(plugin, "lootbox");
        if (!meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) return;

        String lootboxId = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        if (lootboxId == null) return;

        YamlDocument lootbox = LootboxManager.lootbox();
        if (!lootbox.contains(lootboxId) || !lootbox.contains(lootboxId + ".loots")) return;

        if (!(e.getBlockPlaced().getState() instanceof InventoryHolder inv)) return;
        Inventory chestInv = inv.getInventory();

        List<?> loots = lootbox.getList(lootboxId + ".loots");
        Random random = new Random();

        for (Object obj : loots) {
            if (!(obj instanceof Map<?, ?> lootMap)) continue;

            String lootItemName = (String) lootMap.get("item");
            Material lootMaterial = Material.matchMaterial(lootItemName);
            if (lootMaterial == null) continue;

            int chance = (lootMap.get("chance") instanceof Number n) ? n.intValue() : 0;
            int amount = (lootMap.get("amount") instanceof Number n2) ? n2.intValue() : 1;

            if (random.nextInt(100) < chance) {
                ItemStack loot = new ItemStack(lootMaterial, amount);

                int slot;
                do {
                    slot = random.nextInt(chestInv.getSize());
                } while (chestInv.getItem(slot) != null);

                chestInv.setItem(slot, loot);

                plugin.logger.info("Lootbox {} placed: Loot={}, Chance={}%, Amount={} at slot {}",
                        lootboxId, lootItemName, chance, amount, slot);
            }
        }
    }
}
