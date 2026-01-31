package com.nuclearusina.commands;

import com.nuclearusina.NuclearUsinaPlugin;
import com.nuclearusina.items.Skin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
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
                sender.sendMessage(Component.text("Apenas jogadores podem usar este comando.").color(NamedTextColor.RED));
                return true;
            }
            if (plugin.region().teleportLocation().getWorld() == null) {
                sender.sendMessage(Component.text("Mundo da usina não encontrado.").color(NamedTextColor.RED));
                return true;
            }
            player.teleport(plugin.region().teleportLocation());
            player.sendMessage(Component.text("Teleportado para a Usina Nuclear.").color(NamedTextColor.GREEN));
            return true;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("skin")) {
            if (!sender.hasPermission("nuclearusina.admin")) {
                sender.sendMessage(Component.text("Sem permissão.").color(NamedTextColor.RED));
                return true;
            }
            if (args.length < 4 || !args[1].equalsIgnoreCase("give")) {
                sender.sendMessage(Component.text("Uso: /usina skin give <jogador> <skin>").color(NamedTextColor.YELLOW));
                return true;
            }
            Player target = Bukkit.getPlayer(args[2]);
            if (target == null) {
                sender.sendMessage(Component.text("Jogador não encontrado.").color(NamedTextColor.RED));
                return true;
            }
            Skin skin = Skin.fromId(args[3].toLowerCase(Locale.ROOT));
            target.getInventory().addItem(plugin.skinActivatorItem().create(skin));
            sender.sendMessage(Component.text("Ativador entregue para " + target.getName()).color(NamedTextColor.GREEN));
            return true;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("scan")) {
            if (!sender.hasPermission("nuclearusina.admin")) {
                sender.sendMessage(Component.text("Sem permissão.").color(NamedTextColor.RED));
                return true;
            }
            int total = plugin.scanner().scanAndMark();
            sender.sendMessage(Component.text("Scan concluído. TNTs marcadas: " + total).color(NamedTextColor.GREEN));
            return true;
        }

        sender.sendMessage(Component.text("Comando desconhecido.").color(NamedTextColor.RED));
        return true;
    }
}
