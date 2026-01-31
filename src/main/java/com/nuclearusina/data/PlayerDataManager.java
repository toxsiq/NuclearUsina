package com.nuclearusina.data;

import com.nuclearusina.items.Skin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PlayerDataManager {
    private final JavaPlugin plugin;
    private final Map<UUID, PlayerData> cache = new HashMap<>();
    private final File file;
    private FileConfiguration config;

    public PlayerDataManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "players.yml");
        load();
    }

    public void load() {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource("players.yml", false);
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public PlayerData get(UUID uuid) {
        return cache.computeIfAbsent(uuid, this::loadPlayer);
    }

    private PlayerData loadPlayer(UUID uuid) {
        PlayerData data = new PlayerData();
        ConfigurationSection section = config.getConfigurationSection("players." + uuid);
        if (section == null) {
            return data;
        }
        data.setRadiation(section.getDouble("radiation", 0.0));
        data.setToxins(section.getDouble("toxins", 0.0));
        data.setUraniumLevel(section.getInt("enchantments.uranium", 0));
        data.setToxinatorLevel(section.getInt("enchantments.toxinator", 0));
        data.setAspiradorLevel(section.getInt("enchantments.aspirador", 0));
        List<String> unlocked = section.getStringList("skins.unlocked");
        if (!unlocked.isEmpty()) {
            data.unlockedSkins().clear();
            unlocked.stream().map(Skin::fromId).forEach(data.unlockedSkins()::add);
        }
        data.setActiveSkin(Skin.fromId(section.getString("skins.active", Skin.REATOR.id())));
        return data;
    }

    public void save(UUID uuid) {
        PlayerData data = cache.get(uuid);
        if (data == null) {
            return;
        }
        ConfigurationSection section = config.createSection("players." + uuid);
        section.set("radiation", data.radiation());
        section.set("toxins", data.toxins());
        section.set("enchantments.uranium", data.uraniumLevel());
        section.set("enchantments.toxinator", data.toxinatorLevel());
        section.set("enchantments.aspirador", data.aspiradorLevel());
        section.set("skins.unlocked", data.unlockedSkins().stream().map(Skin::id).toList());
        section.set("skins.active", data.activeSkin().id());
        saveFile();
    }

    public void saveAll() {
        cache.keySet().forEach(this::save);
    }

    private void saveFile() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("Falha ao salvar players.yml: " + e.getMessage());
        }
    }
}
