package com.nuclearusina.menus;

import com.nuclearusina.data.PlayerData;
import com.nuclearusina.items.Skin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class EnchantMenu {
    public static final int SLOT_URANIUM = 10;
    public static final int SLOT_TOXINATOR = 12;
    public static final int SLOT_ASPIRADOR = 14;
    public static final int SLOT_SORTUDO = 16;
    public static final int SLOT_CHAVEIRO = 18;
    public static final int SLOT_SKINS = 22;

    public Inventory create(PlayerData data) {
        Inventory inventory = Bukkit.createInventory(new MenuHolder(MenuType.ENCHANTS), 27,
                Component.text("Encantamentos do Esqueiro").color(NamedTextColor.GREEN));
        inventory.setItem(SLOT_URANIUM, createEnchantItem("Uranium", NamedTextColor.GREEN,
                "Aumenta a Radiação recebida.", "Custo: Toxinas", data.uraniumLevel()));
        inventory.setItem(SLOT_TOXINATOR, createEnchantItem("Toxinator", NamedTextColor.DARK_GREEN,
                "Aumenta Toxinas por TNT.", "Custo: Radiação", data.toxinatorLevel()));
        inventory.setItem(SLOT_ASPIRADOR, createEnchantItem("Aspirador", NamedTextColor.AQUA,
                "Chance de quebrar conjunto de TNT.", "Custo: Radiação", data.aspiradorLevel()));
        inventory.setItem(SLOT_SORTUDO, createInfoItem("Sortudo", NamedTextColor.GOLD,
                "Em breve: LuckyBlock Tóxico."));
        inventory.setItem(SLOT_CHAVEIRO, createInfoItem("Chaveiro", NamedTextColor.YELLOW,
                "Em breve: Chave da Usina."));
        inventory.setItem(SLOT_SKINS, createInfoItem("Skins do Esqueiro", NamedTextColor.LIGHT_PURPLE,
                "Clique para ver skins disponíveis."));
        return inventory;
    }

    private ItemStack createEnchantItem(String name, NamedTextColor color, String description, String cost, int level) {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(name).color(color));
        meta.lore(List.of(
                Component.text(description).color(NamedTextColor.GRAY),
                Component.text(cost).color(NamedTextColor.AQUA),
                Component.text("Nível atual: " + level).color(NamedTextColor.YELLOW)
        ));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createInfoItem(String name, NamedTextColor color, String description) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(name).color(color));
        meta.lore(List.of(Component.text(description).color(NamedTextColor.GRAY)));
        item.setItemMeta(meta);
        return item;
    }
}
