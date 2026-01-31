package com.nuclearusina.listeners;

import com.nuclearusina.NuclearUsinaPlugin;
import com.nuclearusina.data.PlayerData;
import com.nuclearusina.items.EsqueiroItem;
import com.nuclearusina.items.Skin;
import com.nuclearusina.menus.EnchantMenu;
import com.nuclearusina.region.TntMarker;
import com.nuclearusina.region.UsinaRegion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.TNTPrimeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UsinaListener implements Listener {
    private final NuclearUsinaPlugin plugin;
    private final UsinaRegion region;
    private final TntMarker marker;
    private final EsqueiroItem esqueiroItem;
    private final Map<UUID, Long> breakCooldowns = new ConcurrentHashMap<>();
    private final double radiationBase;
    private final double radiationBonus;
    private final double toxinsBase;
    private final double toxinsBonus;
    private final double aspiradorBase;
    private final double aspiradorBonus;
    private final double aspiradorMax;
    private final long cooldownMillis;
    private final long respawnTicks;

    public UsinaListener(NuclearUsinaPlugin plugin) {
        this.plugin = plugin;
        this.region = plugin.region();
        this.marker = plugin.tntMarker();
        this.esqueiroItem = plugin.esqueiroItem();
        this.radiationBase = plugin.getConfig().getDouble("radiation.base-per-cycle", 1.0);
        this.radiationBonus = plugin.getConfig().getDouble("radiation.uranium-bonus-per-level", 0.5);
        this.toxinsBase = plugin.getConfig().getDouble("toxins.base-per-tnt", 1.0);
        this.toxinsBonus = plugin.getConfig().getDouble("toxins.toxinator-bonus-per-level", 0.5);
        this.aspiradorBase = plugin.getConfig().getDouble("aspirador.base-chance", 0.1);
        this.aspiradorBonus = plugin.getConfig().getDouble("aspirador.bonus-per-level", 0.05);
        this.aspiradorMax = plugin.getConfig().getDouble("aspirador.max-chance", 0.6);
        this.cooldownMillis = plugin.getConfig().getLong("tnt.break-cooldown-seconds", 2) * 1000L;
        this.respawnTicks = plugin.getConfig().getLong("tnt.respawn-seconds", 10) * 20L;
        startRadiationTask();
    }

    private void startRadiationTask() {
        long cycleTicks = plugin.getConfig().getLong("radiation.cycle-seconds", 5) * 20L;
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!region.isInside(player.getLocation())) {
                    continue;
                }
                PlayerData data = plugin.playerDataManager().get(player.getUniqueId());
                double total = radiationBase + (data.uraniumLevel() * radiationBonus);
                total += applySkinBonus(player, data.activeSkin(), total);
                data.setRadiation(data.radiation() + total);
            }
        }, cycleTicks, cycleTicks);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.playerDataManager().save(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        Block block = event.getBlock();
        if (block.getType() == Material.TNT && region.isInside(block.getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        Entity entity = event.getEntity();
        if (region.isInside(entity.getLocation())) {
            event.blockList().removeIf(block -> block.getType() == Material.TNT);
            if (entity.getType().name().contains("TNT")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onTntPrime(TNTPrimeEvent event) {
        if (region.isInside(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block.getType() == Material.TNT && region.isInside(block.getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        Player player = event.getPlayer();
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.TNT) {
            if (region.isInside(event.getClickedBlock().getLocation())) {
                if (event.getItem() == null || !esqueiroItem.isEsqueiro(event.getItem())) {
                    event.setCancelled(true);
                }
            }
        }
        if (event.getItem() == null) {
            return;
        }
        ItemStack item = event.getItem();
        if (!esqueiroItem.isEsqueiro(item)) {
            return;
        }
        if (player.isSneaking()) {
            player.openInventory(new EnchantMenu().create(plugin.playerDataManager().get(player.getUniqueId())));
            event.setCancelled(true);
            return;
        }
        if (event.getClickedBlock() == null || event.getClickedBlock().getType() != Material.TNT) {
            return;
        }
        Block block = event.getClickedBlock();
        if (!region.isInside(block.getLocation()) || !marker.isMarked(block)) {
            player.sendMessage(Component.text("Essa TNT não pertence à Usina.").color(NamedTextColor.RED));
            event.setCancelled(true);
            return;
        }
        if (!canBreak(player)) {
            player.sendMessage(Component.text("Aguarde antes de quebrar outra TNT.").color(NamedTextColor.YELLOW));
            event.setCancelled(true);
            return;
        }
        PlayerData data = plugin.playerDataManager().get(player.getUniqueId());
        boolean aspirador = Math.random() < getAspiradorChance(data.aspiradorLevel());
        Set<Block> targets = aspirador ? collectCluster(block) : Set.of(block);
        double toxinsPer = toxinsBase + (data.toxinatorLevel() * toxinsBonus);
        toxinsPer += applySkinBonus(player, data.activeSkin(), toxinsPer);
        double totalToxins = toxinsPer * targets.size();
        data.setToxins(data.toxins() + totalToxins);
        for (Block target : targets) {
            breakTnt(target);
        }
        event.setCancelled(true);
    }

    private boolean canBreak(Player player) {
        long now = System.currentTimeMillis();
        Long last = breakCooldowns.get(player.getUniqueId());
        if (last != null && now - last < cooldownMillis) {
            return false;
        }
        breakCooldowns.put(player.getUniqueId(), now);
        return true;
    }

    private void breakTnt(Block block) {
        marker.unmark(block);
        block.setType(Material.AIR);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            block.setType(Material.TNT);
            marker.mark(block);
        }, respawnTicks);
    }

    private Set<Block> collectCluster(Block origin) {
        Set<Block> visited = new HashSet<>();
        Queue<Block> queue = new ArrayDeque<>();
        queue.add(origin);
        visited.add(origin);
        while (!queue.isEmpty()) {
            Block current = queue.poll();
            for (Block neighbor : getNeighbors(current)) {
                if (neighbor.getType() == Material.TNT && region.isInside(neighbor.getLocation()) && marker.isMarked(neighbor)) {
                    if (visited.add(neighbor)) {
                        queue.add(neighbor);
                    }
                }
            }
        }
        return visited;
    }

    private Set<Block> getNeighbors(Block block) {
        Set<Block> neighbors = new HashSet<>();
        neighbors.add(block.getRelative(1, 0, 0));
        neighbors.add(block.getRelative(-1, 0, 0));
        neighbors.add(block.getRelative(0, 1, 0));
        neighbors.add(block.getRelative(0, -1, 0));
        neighbors.add(block.getRelative(0, 0, 1));
        neighbors.add(block.getRelative(0, 0, -1));
        return neighbors;
    }

    private double getAspiradorChance(int level) {
        double chance = aspiradorBase + (level * aspiradorBonus);
        return Math.min(chance, aspiradorMax);
    }

    private double applySkinBonus(Player player, Skin skin, double base) {
        ItemStack hand = player.getInventory().getItemInMainHand();
        if (!esqueiroItem.isEsqueiro(hand)) {
            return 0.0;
        }
        return base * skin.bonusMultiplier();
    }
}
