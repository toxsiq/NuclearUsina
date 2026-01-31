package com.nuclearusina.commands;

import com.nuclearusina.NuclearUsinaPlugin;
import com.nuclearusina.data.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EsqueiroCommand implements CommandExecutor {
    private final NuclearUsinaPlugin plugin;

    public EsqueiroCommand(NuclearUsinaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Apenas jogadores podem usar este comando.");
            return true;
        }
        PlayerData data = plugin.playerDataManager().get(player.getUniqueId());
        player.getInventory().addItem(plugin.esqueiroItem().create(data.activeSkin()));
        player.sendMessage(ChatColor.GREEN + "Você recebeu um Esqueiro da Usina.");
        return true;
    }
}
