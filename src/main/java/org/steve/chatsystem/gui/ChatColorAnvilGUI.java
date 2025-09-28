package org.steve.chatsystem.gui;

import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.steve.chatsystem.ChatSystem;
import org.steve.chatsystem.Color;
import org.steve.chatsystem.SoundManager;
import org.steve.chatsystem.config.PluginConfig;
import org.steve.chatsystem.database.DatabaseManager;
import org.steve.chatsystem.database.PlayerData;

import java.util.Collections;
import java.util.List;

import static org.steve.chatsystem.commands.ChatColor.*;

public class ChatColorAnvilGUI {
    public static void openGradientGUI(Player player, OfflinePlayer target) {
        PlayerData playerData = DatabaseManager.loadPlayerData(target);
        String currentColor = playerData.chatColor();
        String inputText = "Hex Color";
        if (Color.isMiniMessageGradient(currentColor)) {
            inputText = Color.cleanMiniMessageGradient(currentColor)[0];
        }
        new AnvilGUI.Builder()
                .onClose(stateSnapshot -> Bukkit.getScheduler().runTask(ChatSystem.getInstance(), () -> {
                    if (!Color.isHex(stateSnapshot.getText())) {
                        ChatColorGUI.openChatColorGUI(player, target);
                    } else {
                        openGradientGUI2(player, target, stateSnapshot.getText());
                    }
                }))
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }
                    String input = stateSnapshot.getText();
                    if (Color.isHex(input)) {
                        SoundManager.playSound(player, PluginConfig.soundsClickSuccess, PluginConfig.soundsClickSuccessVolume, PluginConfig.soundsClickSuccessPitch);
                        return List.of(AnvilGUI.ResponseAction.close());
                    } else {
                        SoundManager.playSound(player, PluginConfig.soundsClickFail, PluginConfig.soundsClickFailVolume, PluginConfig.soundsClickFailPitch);
                        return List.of(
                                AnvilGUI.ResponseAction.replaceInputText("Invalid Hex Color"),
                                AnvilGUI.ResponseAction.updateTitle("Enter 1. Hex Color", false)
                        );
                    }
                })
                .plugin(ChatSystem.getInstance())
                .title("Enter 1. Hex Color")
                .itemLeft(new ItemStack(Material.NAME_TAG))
                .text(inputText)
                .open(player);
    }

    private static void openGradientGUI2(Player player, OfflinePlayer target, String oldInput) {
        PlayerData playerData = DatabaseManager.loadPlayerData(target);
        String currentColor = playerData.chatColor();
        String inputText = "Hex Color";
        if (Color.isMiniMessageGradient(currentColor)) {
            inputText = Color.cleanMiniMessageGradient(currentColor)[1];
        }
        new AnvilGUI.Builder()
                .onClose(stateSnapshot -> Bukkit.getScheduler().runTask(ChatSystem.getInstance(), () ->
                        ChatColorGUI.openChatColorGUI(player, target)
                ))
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }
                    String input = stateSnapshot.getText();
                    if (Color.isHex(input)) {
                        setChatColor(player, target, oldInput, input);
                    } else {
                        SoundManager.playSound(player, PluginConfig.soundsClickFail, PluginConfig.soundsClickFailVolume, PluginConfig.soundsClickFailPitch);
                        return List.of(
                                AnvilGUI.ResponseAction.replaceInputText("Invalid Hex Color"),
                                AnvilGUI.ResponseAction.updateTitle("Enter 2. Hex Color", false)
                        );
                    }
                    SoundManager.playSound(player, PluginConfig.soundsClickSuccess, PluginConfig.soundsClickSuccessVolume, PluginConfig.soundsClickSuccessPitch);
                    return List.of(AnvilGUI.ResponseAction.close());
                })
                .plugin(ChatSystem.getInstance())
                .title("Enter 2. Hex Color")
                .itemLeft(new ItemStack(Material.NAME_TAG))
                .text(inputText)
                .open(player);
    }

    public static void openHexGUI(Player player, OfflinePlayer target) {
        PlayerData playerData = DatabaseManager.loadPlayerData(target);
        String currentColor = playerData.chatColor();
        String inputText = "Hex Color";
        if (Color.isMiniMessageHex(currentColor)) {
            inputText = Color.cleanMiniMessageHex(currentColor);
        }
        new AnvilGUI.Builder()
                .onClose(stateSnapshot -> Bukkit.getScheduler().runTask(ChatSystem.getInstance(), () ->
                        ChatColorGUI.openChatColorGUI(player, target)
                ))
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }
                    String input = stateSnapshot.getText();
                    if (Color.isHex(input)) {
                        setChatColor(player, target, input);
                    } else {
                        SoundManager.playSound(player, PluginConfig.soundsClickFail, PluginConfig.soundsClickFailVolume, PluginConfig.soundsClickFailPitch);
                        return List.of(
                                AnvilGUI.ResponseAction.replaceInputText("Invalid Hex Color"),
                                AnvilGUI.ResponseAction.updateTitle("Enter Hex Color", false)
                        );
                    }
                    SoundManager.playSound(player, PluginConfig.soundsClickSuccess, PluginConfig.soundsClickSuccessVolume, PluginConfig.soundsClickSuccessPitch);
                    return List.of(AnvilGUI.ResponseAction.close());
                })
                .plugin(ChatSystem.getInstance())
                .title("Enter Hex Color")
                .itemLeft(new ItemStack(Material.NAME_TAG))
                .text(inputText)
                .open(player);
    }
}
