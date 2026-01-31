package com.nuclearusina.menus;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class MenuHolder implements InventoryHolder {
    private final MenuType type;

    public MenuHolder(MenuType type) {
        this.type = type;
    }

    public MenuType type() {
        return type;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
