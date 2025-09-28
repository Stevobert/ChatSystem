package org.steve.chatsystem.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.steve.chatsystem.ChatSystem;
import org.steve.chatsystem.MessageManager;
import org.steve.chatsystem.config.PluginConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatSystemCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 0 || args.length == 1 && args[0].equalsIgnoreCase("ver")) {
            MessageManager.send(commandSender, "&2ChatSystem Version: &a" + ChatSystem.getInstance().getDescription().getVersion());
        } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            boolean validateConfig = PluginConfig.getInstance().load();
            if (validateConfig) {
                MessageManager.send(commandSender, "&aSuccessfully reloaded the config!");
            } else {
                MessageManager.send(commandSender, "&cFailed to reload config! The config is invalid, check the console for more info! Using default config instead.");
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1)
            return Arrays.asList("ver", "reload");
        return new ArrayList<>();
    }
}
