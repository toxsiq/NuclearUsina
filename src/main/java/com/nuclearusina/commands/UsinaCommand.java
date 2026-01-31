package com.nuclearusina.commands;

import com.nuclearusina.NuclearUsinaPlugin;
import com.nuclearusina.items.Skin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class UsinaCommand implements CommandExecutor {
    private final NuclearUsinaPlugin plugin;

    public UsinaCommand(NuclearUsinaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(ChatColor.RED + "Apenas jogadores podem usar este comando.");
                return true;
            }
            if (plugin.region().teleportLocation().getWorld() == null) {
                sender.sendMessage(ChatColor.RED + "Mundo da usina não encontrado.");
                return true;
            }
            player.teleport(plugin.region().teleportLocation());
            player.sendMessage(ChatColor.GREEN + "Teleportado para a Usina Nuclear.");
            return true;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("skin")) {
            if (!sender.hasPermission("nuclearusina.admin")) {
                sender.sendMessage(ChatColor.RED + "Sem permissão.");
                return true;
            }
            if (args.length < 4 || !args[1].equalsIgnoreCase("give")) {
                sender.sendMessage(ChatColor.YELLOW + "Uso: /usina skin give <jogador> <skin>");
                return true;
            }
            Player target = Bukkit.getPlayer(args[2]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Jogador não encontrado.");
                return true;
            }
            Skin skin = Skin.fromId(args[3].toLowerCase(Locale.ROOT));
            target.getInventory().addItem(plugin.skinActivatorItem().create(skin));
            sender.sendMessage(ChatColor.GREEN + "Ativador entregue para " + target.getName());
            return true;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("scan")) {
            if (!sender.hasPermission("nuclearusina.admin")) {
                sender.sendMessage(ChatColor.RED + "Sem permissão.");
                return true;
            }
            int total = plugin.scanner().scanAndMark();
            sender.sendMessage(ChatColor.GREEN + "Scan concluído. TNTs marcadas: " + total);
            return true;
        }

        sender.sendMessage(ChatColor.RED + "Comando desconhecido.");
        return true;
    }
}
