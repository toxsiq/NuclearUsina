package com.nuclearusina.menus;

import com.nuclearusina.items.Skin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Set;

public class SkinMenu {
    public Inventory create(Set<Skin> unlocked, Skin active) {
        Inventory inventory = Bukkit.createInventory(new MenuHolder(MenuType.SKINS), 27,
                ChatColor.LIGHT_PURPLE + "Skins do Esqueiro");
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
        meta.setDisplayName((active ? ChatColor.GREEN : ChatColor.WHITE) + skin.displayName());
        meta.setLore(List.of(
                ChatColor.AQUA + "Raridade: " + skin.rarity(),
                ChatColor.YELLOW + "Bônus: " + (int) (skin.bonusMultiplier() * 100) + "%",
                (unlocked ? (active ? ChatColor.GREEN + "Ativa" : ChatColor.GREEN + "Clique para ativar")
                        : ChatColor.RED + "Bloqueada")
        ));
        item.setItemMeta(meta);
        return item;
    }
}
