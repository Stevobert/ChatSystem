package org.steve.chatsystem.commands;

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
import org.steve.chatsystem.database.DatabaseManager;
import org.steve.chatsystem.database.PlayerData;

import java.util.List;

public class Togglepings implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(commandSender instanceof Player player)) {
            MessageManager.send(commandSender, Color.colored(PluginConfig.messagesConsoleCommandError));
        } else if (args.length >= 1) {
            return false;
        } else {
            PlayerData playerData = DatabaseManager.loadPlayerData(player);

            if (playerData.disablepings()) {
                MessageManager.send(commandSender, Color.coloredMessage(PluginConfig.messagesTogglepingsEnable, List.of(), List.of()));
                DatabaseManager.setDisablePings(player, false);
            } else {
                MessageManager.send(commandSender, Color.coloredMessage(PluginConfig.messagesTogglepingsDisable, List.of(), List.of()));
                DatabaseManager.setDisablePings(player, true);
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        return List.of();
    }
}
