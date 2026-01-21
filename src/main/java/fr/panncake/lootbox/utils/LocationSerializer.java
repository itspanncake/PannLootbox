package fr.panncake.lootbox.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public final class LocationSerializer {

    public static String serialize(Location loc) {
        return loc.getWorld().getName() + ";" + loc.getBlockX() + ";" + loc.getBlockY() + ";" + loc.getBlockZ();
    }

    public static Location deserialize(String s) {
        String[] p = s.split(";");
        return new Location(Bukkit.getWorld(p[0]), Integer.parseInt(p[1]), Integer.parseInt(p[2]), Integer.parseInt(p[3]));
    }
}
