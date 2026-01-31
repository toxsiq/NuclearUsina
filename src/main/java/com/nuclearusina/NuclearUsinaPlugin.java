package com.nuclearusina;

import com.nuclearusina.commands.EsqueiroCommand;
import com.nuclearusina.commands.UsinaCommand;
import com.nuclearusina.data.PlayerDataManager;
import com.nuclearusina.items.EsqueiroItem;
import com.nuclearusina.items.SkinActivatorItem;
import com.nuclearusina.listeners.MenuListener;
import com.nuclearusina.listeners.SkinActivatorListener;
import com.nuclearusina.listeners.UsinaListener;
import com.nuclearusina.region.TntMarker;
import com.nuclearusina.region.UsinaRegion;
import com.nuclearusina.region.UsinaScanner;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public class NuclearUsinaPlugin extends JavaPlugin {
    private PlayerDataManager playerDataManager;
    private UsinaRegion region;
    private TntMarker tntMarker;
    private EsqueiroItem esqueiroItem;
    private SkinActivatorItem skinActivatorItem;
    private UsinaScanner scanner;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.playerDataManager = new PlayerDataManager(this);
        this.region = new UsinaRegion(this);
        this.tntMarker = new TntMarker(this);
        this.esqueiroItem = new EsqueiroItem(new NamespacedKey(this, "esqueiro"), new NamespacedKey(this, "esqueiro_skin"));
        this.skinActivatorItem = new SkinActivatorItem(new NamespacedKey(this, "skin_activator"), new NamespacedKey(this, "skin_activator_id"));
        this.scanner = new UsinaScanner(this, region, tntMarker);

        getCommand("usina").setExecutor(new UsinaCommand(this));
        getCommand("esqueiro").setExecutor(new EsqueiroCommand(this));

        getServer().getPluginManager().registerEvents(new UsinaListener(this), this);
        getServer().getPluginManager().registerEvents(new MenuListener(this), this);
        getServer().getPluginManager().registerEvents(new SkinActivatorListener(this), this);

        getServer().getScheduler().runTask(this, () -> scanner.scanAndMark());
    }

    @Override
    public void onDisable() {
        playerDataManager.saveAll();
    }

    public PlayerDataManager playerDataManager() {
        return playerDataManager;
    }

    public UsinaRegion region() {
        return region;
    }

    public TntMarker tntMarker() {
        return tntMarker;
    }

    public EsqueiroItem esqueiroItem() {
        return esqueiroItem;
    }

    public SkinActivatorItem skinActivatorItem() {
        return skinActivatorItem;
    }

    public UsinaScanner scanner() {
        return scanner;
    }
}
