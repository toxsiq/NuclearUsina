package com.nuclearusina.items;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class SkinActivatorItem {
    private final NamespacedKey activatorKey;
    private final NamespacedKey skinKey;

    public SkinActivatorItem(NamespacedKey activatorKey, NamespacedKey skinKey) {
        this.activatorKey = activatorKey;
        this.skinKey = skinKey;
    }

    public ItemStack create(Skin skin) {
        ItemStack item = new ItemStack(Material.FLINT_AND_STEEL);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("Ativador de Skin - " + skin.displayName()).color(NamedTextColor.LIGHT_PURPLE));
        meta.lore(List.of(
                Component.text("Clique direito para desbloquear.").color(NamedTextColor.GRAY),
                Component.text("Skin: " + skin.displayName()).color(NamedTextColor.AQUA)
        ));
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(activatorKey, PersistentDataType.BYTE, (byte) 1);
        container.set(skinKey, PersistentDataType.STRING, skin.id());
        item.setItemMeta(meta);
        return item;
    }

    public boolean isActivator(ItemStack item) {
        if (item == null || item.getType() != Material.FLINT_AND_STEEL || !item.hasItemMeta()) {
            return false;
        }
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        return container.has(activatorKey, PersistentDataType.BYTE);
    }

    public Skin getSkin(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return Skin.REATOR;
        }
        String id = item.getItemMeta().getPersistentDataContainer().get(skinKey, PersistentDataType.STRING);
        return Skin.fromId(id == null ? Skin.REATOR.id() : id);
    }
}
