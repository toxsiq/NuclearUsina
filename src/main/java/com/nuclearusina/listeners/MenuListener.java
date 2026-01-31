package com.nuclearusina.listeners;

import com.nuclearusina.NuclearUsinaPlugin;
import com.nuclearusina.data.PlayerData;
import com.nuclearusina.items.Skin;
import com.nuclearusina.menus.EnchantMenu;
import com.nuclearusina.menus.MenuHolder;
import com.nuclearusina.menus.MenuType;
import com.nuclearusina.menus.SkinMenu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MenuListener implements Listener {
    private final NuclearUsinaPlugin plugin;

    public MenuListener(NuclearUsinaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof MenuHolder holder)) {
            return;
        }
        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }
        PlayerData data = plugin.playerDataManager().get(player.getUniqueId());
        if (holder.type() == MenuType.ENCHANTS) {
            handleEnchantMenu(event.getSlot(), player, data);
            return;
        }
        if (holder.type() == MenuType.SKINS) {
            handleSkinMenu(event.getSlot(), player, data);
        }
    }

    private void handleEnchantMenu(int slot, Player player, PlayerData data) {
        if (slot == EnchantMenu.SLOT_SKINS) {
            player.openInventory(new SkinMenu().create(data.unlockedSkins(), data.activeSkin()));
            return;
        }
        int uraniumCost = plugin.getConfig().getInt("enchant-costs.uranium-base", 10) * (data.uraniumLevel() + 1);
        int toxinatorCost = plugin.getConfig().getInt("enchant-costs.toxinator-base", 10) * (data.toxinatorLevel() + 1);
        int aspiradorCost = plugin.getConfig().getInt("enchant-costs.aspirador-base", 15) * (data.aspiradorLevel() + 1);
        if (slot == EnchantMenu.SLOT_URANIUM) {
            if (data.toxins() < uraniumCost) {
                player.sendMessage(ChatColor.RED + "Toxinas insuficientes.");
                return;
            }
            data.setToxins(data.toxins() - uraniumCost);
            data.setUraniumLevel(data.uraniumLevel() + 1);
            player.sendMessage(ChatColor.GREEN + "Uranium evoluído para nível " + data.uraniumLevel());
            player.openInventory(new EnchantMenu().create(data));
        }
        if (slot == EnchantMenu.SLOT_TOXINATOR) {
            if (data.radiation() < toxinatorCost) {
                player.sendMessage(ChatColor.RED + "Radiação insuficiente.");
                return;
            }
            data.setRadiation(data.radiation() - toxinatorCost);
            data.setToxinatorLevel(data.toxinatorLevel() + 1);
            player.sendMessage(ChatColor.GREEN + "Toxinator evoluído para nível " + data.toxinatorLevel());
            player.openInventory(new EnchantMenu().create(data));
        }
        if (slot == EnchantMenu.SLOT_ASPIRADOR) {
            if (data.radiation() < aspiradorCost) {
                player.sendMessage(ChatColor.RED + "Radiação insuficiente.");
                return;
            }
            data.setRadiation(data.radiation() - aspiradorCost);
            data.setAspiradorLevel(data.aspiradorLevel() + 1);
            player.sendMessage(ChatColor.GREEN + "Aspirador evoluído para nível " + data.aspiradorLevel());
            player.openInventory(new EnchantMenu().create(data));
        }
    }

    private void handleSkinMenu(int slot, Player player, PlayerData data) {
        int index = switch (slot) {
            case 10 -> 0;
            case 12 -> 1;
            case 14 -> 2;
            case 16 -> 3;
            default -> -1;
        };
        if (index < 0 || index >= Skin.values().length) {
            return;
        }
        Skin skin = Skin.values()[index];
        if (!data.unlockedSkins().contains(skin)) {
            player.sendMessage(ChatColor.RED + "Skin bloqueada.");
            return;
        }
        data.setActiveSkin(skin);
        updateEsqueiroInHand(player, skin);
        player.sendMessage(ChatColor.GREEN + "Skin ativa: " + skin.displayName());
        player.openInventory(new SkinMenu().create(data.unlockedSkins(), data.activeSkin()));
    }

    private void updateEsqueiroInHand(Player player, Skin skin) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (plugin.esqueiroItem().isEsqueiro(item)) {
            player.getInventory().setItemInMainHand(plugin.esqueiroItem().create(skin));
        }
    }
}
