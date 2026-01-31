package com.nuclearusina.data;

import com.nuclearusina.items.Skin;

import java.util.EnumSet;
import java.util.Set;

public class PlayerData {
    private double radiation;
    private double toxins;
    private int uraniumLevel;
    private int toxinatorLevel;
    private int aspiradorLevel;
    private final Set<Skin> unlockedSkins;
    private Skin activeSkin;

    public PlayerData() {
        this.radiation = 0.0;
        this.toxins = 0.0;
        this.uraniumLevel = 0;
        this.toxinatorLevel = 0;
        this.aspiradorLevel = 0;
        this.unlockedSkins = EnumSet.of(Skin.REATOR);
        this.activeSkin = Skin.REATOR;
    }

    public double radiation() {
        return radiation;
    }

    public void setRadiation(double radiation) {
        this.radiation = Math.max(0, radiation);
    }

    public double toxins() {
        return toxins;
    }

    public void setToxins(double toxins) {
        this.toxins = Math.max(0, toxins);
    }

    public int uraniumLevel() {
        return uraniumLevel;
    }

    public void setUraniumLevel(int uraniumLevel) {
        this.uraniumLevel = Math.max(0, uraniumLevel);
    }

    public int toxinatorLevel() {
        return toxinatorLevel;
    }

    public void setToxinatorLevel(int toxinatorLevel) {
        this.toxinatorLevel = Math.max(0, toxinatorLevel);
    }

    public int aspiradorLevel() {
        return aspiradorLevel;
    }

    public void setAspiradorLevel(int aspiradorLevel) {
        this.aspiradorLevel = Math.max(0, aspiradorLevel);
    }

    public Set<Skin> unlockedSkins() {
        return unlockedSkins;
    }

    public Skin activeSkin() {
        return activeSkin;
    }

    public void setActiveSkin(Skin activeSkin) {
        this.activeSkin = activeSkin == null ? Skin.REATOR : activeSkin;
    }
}
