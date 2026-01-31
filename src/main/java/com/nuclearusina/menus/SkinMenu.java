package com.nuclearusina.menus;

import com.nuclearusina.items.Skin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Set;

public class SkinMenu {
    public Inventory create(Set<Skin> unlocked, Skin active) {
        Inventory inventory = Bukkit.createInventory(new MenuHolder(MenuType.SKINS), 27,
                Component.text("Skins do Esqueiro").color(NamedTextColor.LIGHT_PURPLE));
        int slot = 10;
        for (Skin skin : Skin.values()) {
            inventory.setItem(slot, createSkinItem(skin, unlocked.contains(skin), active == skin));
            slot += 2;
        }
        return inventory;
    }

    private ItemStack createSkinItem(Skin skin, boolean unlocked, boolean active) {
        ItemStack item = new ItemStack(unlocked ? Material.NETHER_STAR : Material.GRAY_DYE);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(skin.displayName()).color(active ? NamedTextColor.GREEN : NamedTextColor.WHITE));
        meta.lore(List.of(
                Component.text("Raridade: " + skin.rarity()).color(NamedTextColor.AQUA),
                Component.text("Bônus: " + (int) (skin.bonusMultiplier() * 100) + "%").color(NamedTextColor.YELLOW),
                Component.text(unlocked ? (active ? "Ativa" : "Clique para ativar") : "Bloqueada")
                        .color(unlocked ? NamedTextColor.GREEN : NamedTextColor.RED)
        ));
        item.setItemMeta(meta);
        return item;
    }
}
