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

    public void onLootboxPlace(BlockPlaceEvent e) {
        ItemStack item = e.getItemInHand();
        if (!item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(plugin, "lootbox");
        String lootboxId = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);

        if (lootboxId == null) return;

        YamlDocument lootbox = LootboxManager.lootbox();
        List<?> loots = lootbox.getList(lootboxId + ".loots");
        if (loots == null || loots.isEmpty()) return;

        if (!(e.getBlockPlaced().getState() instanceof InventoryHolder holder)) return;

        Inventory chestInv = holder.getInventory();
        Random random = new Random();

        for (Object obj : loots) {
            if (!(obj instanceof Map<?, ?> lootMap)) continue;

            Material lootMaterial = Material.matchMaterial(String.valueOf(lootMap.get("item")));
            if (lootMaterial == null) continue;

            int chance = (lootMap.get("chance") instanceof Number n) ? n.intValue() : 0;
            int amount = (lootMap.get("amount") instanceof Number n2) ? n2.intValue() : 1;

            if (random.nextInt(100) < chance) {
                ItemStack loot = new ItemStack(lootMaterial, amount);

                if (chestInv.firstEmpty() == -1) break;

                int slot;
                int attempts = 0;
                do {
                    slot = random.nextInt(chestInv.getSize());
                    attempts++;
                } while (chestInv.getItem(slot) != null && attempts < 30);

                if (chestInv.getItem(slot) != null) slot = chestInv.firstEmpty();

                chestInv.setItem(slot, loot);
            }
        }

        e.getBlockPlaced().getState().update(true);
    }
}
