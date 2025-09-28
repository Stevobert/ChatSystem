package org.steve.chatsystem.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.steve.chatsystem.Color;
import org.steve.chatsystem.MessageManager;
import org.steve.chatsystem.config.PluginConfig;

import java.util.List;

public class MuteChat implements CommandExecutor, TabCompleter {
    public static boolean muteChat = false;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length >= 1) {
            return false;
        }

        if (muteChat) {
            muteChat = false;
            MessageManager.broadcastMessage(Color.coloredMessage(PluginConfig.messagesMutechatUnmute, List.of("%name%"), List.of(commandSender.getName())));
        } else {
            muteChat = true;
            MessageManager.broadcastMessage(Color.coloredMessage(PluginConfig.messagesMutechatMute, List.of("%name%"), List.of(commandSender.getName())));
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        return List.of();
    }
}
