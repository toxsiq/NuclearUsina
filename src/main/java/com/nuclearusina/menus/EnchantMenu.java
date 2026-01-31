package com.nuclearusina.menus;

import com.nuclearusina.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
                ChatColor.GREEN + "Encantamentos do Esqueiro");
        inventory.setItem(SLOT_URANIUM, createEnchantItem("Uranium", ChatColor.GREEN,
                "Aumenta a Radiação recebida.", "Custo: Toxinas", data.uraniumLevel()));
        inventory.setItem(SLOT_TOXINATOR, createEnchantItem("Toxinator", ChatColor.DARK_GREEN,
                "Aumenta Toxinas por TNT.", "Custo: Radiação", data.toxinatorLevel()));
        inventory.setItem(SLOT_ASPIRADOR, createEnchantItem("Aspirador", ChatColor.AQUA,
                "Chance de quebrar conjunto de TNT.", "Custo: Radiação", data.aspiradorLevel()));
        inventory.setItem(SLOT_SORTUDO, createInfoItem("Sortudo", ChatColor.GOLD,
                "Em breve: LuckyBlock Tóxico."));
        inventory.setItem(SLOT_CHAVEIRO, createInfoItem("Chaveiro", ChatColor.YELLOW,
                "Em breve: Chave da Usina."));
        inventory.setItem(SLOT_SKINS, createInfoItem("Skins do Esqueiro", ChatColor.LIGHT_PURPLE,
                "Clique para ver skins disponíveis."));
        return inventory;
    }

    private ItemStack createEnchantItem(String name, ChatColor color, String description, String cost, int level) {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(color + name);
        meta.setLore(List.of(
                ChatColor.GRAY + description,
                ChatColor.AQUA + cost,
                ChatColor.YELLOW + "Nível atual: " + level
        ));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createInfoItem(String name, ChatColor color, String description) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(color + name);
        meta.setLore(List.of(ChatColor.GRAY + description));
        item.setItemMeta(meta);
        return item;
    }
}
