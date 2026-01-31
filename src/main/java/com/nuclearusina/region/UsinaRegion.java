package com.nuclearusina.region;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public class UsinaRegion {
    private final World world;
    private final int minX;
    private final int minY;
    private final int minZ;
    private final int maxX;
    private final int maxY;
    private final int maxZ;
    private final Location teleportLocation;

    public UsinaRegion(JavaPlugin plugin) {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("usina");
        String worldName = section.getString("world", "world");
        this.world = plugin.getServer().getWorld(worldName);
        ConfigurationSection min = section.getConfigurationSection("region.min");
        ConfigurationSection max = section.getConfigurationSection("region.max");
        this.minX = Math.min(min.getInt("x"), max.getInt("x"));
        this.minY = Math.min(min.getInt("y"), max.getInt("y"));
        this.minZ = Math.min(min.getInt("z"), max.getInt("z"));
        this.maxX = Math.max(min.getInt("x"), max.getInt("x"));
        this.maxY = Math.max(min.getInt("y"), max.getInt("y"));
        this.maxZ = Math.max(min.getInt("z"), max.getInt("z"));
        ConfigurationSection tp = section.getConfigurationSection("teleport");
        this.teleportLocation = new Location(world,
                tp.getDouble("x"),
                tp.getDouble("y"),
                tp.getDouble("z"),
                (float) tp.getDouble("yaw"),
                (float) tp.getDouble("pitch"));
    }

    public boolean isInside(Location location) {
        if (location == null || world == null || !location.getWorld().equals(world)) {
            return false;
        }
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        return x >= minX && x <= maxX
                && y >= minY && y <= maxY
                && z >= minZ && z <= maxZ;
    }

    public World world() {
        return world;
    }

    public int minX() {
        return minX;
    }

    public int maxX() {
        return maxX;
    }

    public int minY() {
        return minY;
    }

    public int maxY() {
        return maxY;
    }

    public int minZ() {
        return minZ;
    }

    public int maxZ() {
        return maxZ;
    }

    public Location teleportLocation() {
        return teleportLocation.clone();
    }
}
