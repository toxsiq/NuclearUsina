package com.nuclearusina.commands;

import com.nuclearusina.NuclearUsinaPlugin;
import com.nuclearusina.data.PlayerData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
            sender.sendMessage(Component.text("Apenas jogadores podem usar este comando.").color(NamedTextColor.RED));
            return true;
        }
        PlayerData data = plugin.playerDataManager().get(player.getUniqueId());
        player.getInventory().addItem(plugin.esqueiroItem().create(data.activeSkin()));
        player.sendMessage(Component.text("Você recebeu um Esqueiro da Usina.").color(NamedTextColor.GREEN));
        return true;
    }
}
