package org.steve.chatsystem.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.steve.chatsystem.Color;
import org.steve.chatsystem.MessageManager;
import org.steve.chatsystem.config.PluginConfig;

import java.util.List;

public class ClearChat implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length >= 1) {
            return false;
        }
        clearChat(commandSender);
        return true;
    }

    public static void clearChat(CommandSender user) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("chatsystem.bypass.clearchat")) continue;
            MessageManager.send(p, "\n".repeat(1000));
        }
        MessageManager.broadcastMessage(Color.coloredMessage(PluginConfig.messagesClearchatClearchat, List.of("%name%"), List.of(user.getName())));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        return List.of();
    }
}
