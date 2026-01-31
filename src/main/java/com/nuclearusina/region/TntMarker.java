package com.nuclearusina.region;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class TntMarker {
    private final NamespacedKey key;

    public TntMarker(JavaPlugin plugin) {
        this.key = new NamespacedKey(plugin, "usina_tnt_locations");
    }

    public boolean isMarked(Block block) {
        if (block == null) {
            return false;
        }
        Chunk chunk = block.getChunk();
        Set<String> entries = getEntries(chunk);
        String encoded = encode(block.getLocation());
        return entries.contains(encoded);
    }

    public void mark(Block block) {
        if (block == null) {
            return;
        }
        Chunk chunk = block.getChunk();
        Set<String> entries = getEntries(chunk);
        entries.add(encode(block.getLocation()));
        saveEntries(chunk, entries);
    }

    public void unmark(Block block) {
        if (block == null) {
            return;
        }
        Chunk chunk = block.getChunk();
        Set<String> entries = getEntries(chunk);
        entries.remove(encode(block.getLocation()));
        saveEntries(chunk, entries);
    }

    private Set<String> getEntries(Chunk chunk) {
        PersistentDataContainer container = chunk.getPersistentDataContainer();
        String raw = container.get(key, PersistentDataType.STRING);
        if (raw == null || raw.isBlank()) {
            return new HashSet<>();
        }
        return Arrays.stream(raw.split(";"))
                .filter(entry -> !entry.isBlank())
                .collect(Collectors.toSet());
    }

    private void saveEntries(Chunk chunk, Set<String> entries) {
        PersistentDataContainer container = chunk.getPersistentDataContainer();
        if (entries.isEmpty()) {
            container.remove(key);
            return;
        }
        String raw = String.join(";", entries);
        container.set(key, PersistentDataType.STRING, raw);
    }

    private String encode(Location location) {
        return location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();
    }
}
