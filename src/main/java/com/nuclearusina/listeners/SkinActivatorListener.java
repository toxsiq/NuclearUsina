package com.nuclearusina.listeners;

import com.nuclearusina.NuclearUsinaPlugin;
import com.nuclearusina.data.PlayerData;
import com.nuclearusina.items.Skin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.EquipmentSlot;

public class SkinActivatorListener implements Listener {
    private final NuclearUsinaPlugin plugin;

    public SkinActivatorListener(NuclearUsinaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        ItemStack item = event.getItem();
        if (item == null || !plugin.skinActivatorItem().isActivator(item)) {
            return;
        }
        Player player = event.getPlayer();
        Skin skin = plugin.skinActivatorItem().getSkin(item);
        PlayerData data = plugin.playerDataManager().get(player.getUniqueId());
        if (data.unlockedSkins().contains(skin)) {
            player.sendMessage(Component.text("Você já desbloqueou essa skin.").color(NamedTextColor.YELLOW));
            event.setCancelled(true);
            return;
        }
        data.unlockedSkins().add(skin);
        player.sendMessage(Component.text("Skin desbloqueada: " + skin.displayName()).color(NamedTextColor.GREEN));
        item.setAmount(item.getAmount() - 1);
        event.setCancelled(true);
    }
}
