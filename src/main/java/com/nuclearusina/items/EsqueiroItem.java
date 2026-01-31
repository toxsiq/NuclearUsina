package com.nuclearusina.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class EsqueiroItem {
    private final NamespacedKey key;
    private final NamespacedKey skinKey;

    public EsqueiroItem(NamespacedKey key, NamespacedKey skinKey) {
        this.key = key;
        this.skinKey = skinKey;
    }

    public ItemStack create(Skin skin) {
        ItemStack item = new ItemStack(Material.FLINT_AND_STEEL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Esqueiro - " + skin.displayName());
        meta.setLore(List.of(
                ChatColor.GRAY + "Ferramenta da Usina Nuclear.",
                ChatColor.AQUA + "Skin: " + skin.displayName() + " (" + skin.rarity() + ")"
        ));
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(key, PersistentDataType.BYTE, (byte) 1);
        container.set(skinKey, PersistentDataType.STRING, skin.id());
        item.setItemMeta(meta);
        return item;
    }

    public boolean isEsqueiro(ItemStack item) {
        if (item == null || item.getType() != Material.FLINT_AND_STEEL || !item.hasItemMeta()) {
            return false;
        }
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        return container.has(key, PersistentDataType.BYTE);
    }

    public Skin getSkin(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return Skin.REATOR;
        }
        String id = item.getItemMeta().getPersistentDataContainer().get(skinKey, PersistentDataType.STRING);
        return Skin.fromId(id == null ? Skin.REATOR.id() : id);
    }
}
