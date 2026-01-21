package fr.panncake.lootbox.utils;

import fr.panncake.lootbox.lootbox.models.Lootbox;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PlaceholderUtils {

    public static Map<String, String> lootboxPlaceholders(Lootbox lootbox) {
        Map<String, String> placeholders = new HashMap<>();

        placeholders.put("LOOTBOX", lootbox.getId());
        placeholders.put("LOOTBOX_NAME", lootbox.getName());
        placeholders.put("LOOTBOX_MATERIAL", lootbox.getMaterial().name());
        placeholders.put("LOOTBOX_TYPE", lootbox.getType().name());
        placeholders.put("LOOTBOX_LOCATIONS", formatLocations(lootbox));

        return placeholders;
    }

    private static String formatLocations(Lootbox lootbox) {
        if (lootbox.getLocations().isEmpty()) {
            return "none";
        }

        return lootbox.getLocations().stream()
                .map(loc -> {
                    String[] p = loc.split(";");
                    return p[0] + ": " + p[1] + " " + p[2] + " " + p[3];
                })
                .collect(Collectors.joining(", "));
    }
}
