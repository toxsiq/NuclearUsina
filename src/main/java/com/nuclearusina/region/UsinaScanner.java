package com.nuclearusina.region;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

public class UsinaScanner {
    private final JavaPlugin plugin;
    private final UsinaRegion region;
    private final TntMarker marker;

    public UsinaScanner(JavaPlugin plugin, UsinaRegion region, TntMarker marker) {
        this.plugin = plugin;
        this.region = region;
        this.marker = marker;
    }

    public int scanAndMark() {
        World world = region.world();
        if (world == null) {
            return 0;
        }
        int count = 0;
        for (int x = region.minX(); x <= region.maxX(); x++) {
            for (int y = region.minY(); y <= region.maxY(); y++) {
                for (int z = region.minZ(); z <= region.maxZ(); z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType() == Material.TNT) {
                        marker.mark(block);
                        count++;
                    }
                }
            }
        }
        plugin.getLogger().info("TNTs marcadas na usina: " + count);
        return count;
    }
}
