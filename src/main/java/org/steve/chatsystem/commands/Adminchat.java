package org.steve.chatsystem.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.steve.chatsystem.Color;
import org.steve.chatsystem.Format;
import org.steve.chatsystem.MessageManager;
import org.steve.chatsystem.config.PluginConfig;
import org.steve.chatsystem.database.DatabaseManager;
import org.steve.chatsystem.database.PlayerData;

import java.util.List;

public class Adminchat implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length >= 1) {
            sendAdminchat(commandSender, String.join(" ", args));
            return true;
        }
        if (!(commandSender instanceof Player player)) {
            MessageManager.send(commandSender, Color.colored(PluginConfig.messagesConsoleCommandError));
            return true;
        }

        PlayerData playerData = DatabaseManager.loadPlayerData(player);
        if (playerData.staffchat()) {
            MessageManager.send(commandSender, Color.coloredMessage(PluginConfig.messagesStaffchatDisable, List.of("%prefix%"), List.of(PluginConfig.staffchatPrefix)));
            DatabaseManager.setStaffchat(player, false);
        }
        if (playerData.adminchat()) {
            MessageManager.send(commandSender, Color.coloredMessage(PluginConfig.messagesAdminchatDisable, List.of("%prefix%"), List.of(PluginConfig.adminchatPrefix)));
            DatabaseManager.setAdminchat(player, false);
        } else {
            MessageManager.send(commandSender, Color.coloredMessage(PluginConfig.messagesAdminchatEnable, List.of("%prefix%"), List.of(PluginConfig.adminchatPrefix)));
            DatabaseManager.setAdminchat(player, true);
        }
        return true;
    }

    public static void sendAdminchat(CommandSender sender, String msg) {
        String permission = "chatsystem.command.adminchat";

        Component message = Format.format(sender, msg, permission);

        Format.pingPlayer(sender, msg, permission);

        if (sender.hasPermission(permission)) Format.pingEveryone(sender, msg, permission);

        Component NameTag = Color.colored("&c" + sender.getName() + ": ");
        String staffchatPrefix = PluginConfig.adminchatPrefix + " ";

        if (sender instanceof Player player) {
            if (PluginConfig.adminchatEnableCustom) {
                NameTag = Color.colored(PlaceholderAPI.setPlaceholders(player, PluginConfig.adminchatCustom));
            } else {
                if (PluginConfig.chatformatEnableCustom) {
                    NameTag = Color.colored(PlaceholderAPI.setPlaceholders(player, PluginConfig.chatformatCustom));
                } else {
                    String prefix = PluginConfig.chatformatPrefix;
                    String suffix = PluginConfig.chatformatSuffix;
                    String arrow = PluginConfig.chatformatArrow;
                    NameTag = Color.colored(PlaceholderAPI.setPlaceholders(player, prefix + player.getName() + suffix + arrow));
                }
            }
        }

        message = Color.colored(staffchatPrefix).append(NameTag).append(message);

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission(permission)) {
                MessageManager.send(p, message);
            }
        }
        MessageManager.send(Bukkit.getConsoleSender(), message);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        return List.of();
    }
}
