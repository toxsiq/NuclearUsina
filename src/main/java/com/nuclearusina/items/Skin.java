package com.nuclearusina.items;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public enum Skin {
    REATOR("Reator", "Comum", 0.0),
    RADIOATIVO("Radioativo", "Rara", 0.25),
    ATOMICO("Atômico", "Épica", 0.50),
    NUCLEAR("Nuclear", "Lendária", 1.00);

    private final String displayName;
    private final String rarity;
    private final double bonusMultiplier;

    Skin(String displayName, String rarity, double bonusMultiplier) {
        this.displayName = displayName;
        this.rarity = rarity;
        this.bonusMultiplier = bonusMultiplier;
    }

    public String displayName() {
        return displayName;
    }

    public String rarity() {
        return rarity;
    }

    public double bonusMultiplier() {
        return bonusMultiplier;
    }

    public String id() {
        return name().toLowerCase(Locale.ROOT);
    }

    public static Skin fromId(String id) {
        for (Skin skin : values()) {
            if (skin.id().equalsIgnoreCase(id)) {
                return skin;
            }
        }
        return REATOR;
    }

    public static Set<String> ids() {
        return Arrays.stream(values()).map(Skin::id).collect(Collectors.toSet());
    }
}
