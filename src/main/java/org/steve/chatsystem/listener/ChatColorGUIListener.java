package org.steve.chatsystem.listener;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.metadata.MetadataValue;
import org.jetbrains.annotations.NotNull;
import org.steve.chatsystem.SoundManager;
import org.steve.chatsystem.config.PluginConfig;
import org.steve.chatsystem.database.DatabaseManager;
import org.steve.chatsystem.database.PlayerData;
import org.steve.chatsystem.gui.ChatColorAnvilGUI;
import org.steve.chatsystem.gui.ChatColorGUI;
import org.steve.chatsystem.ChatSystem;
import org.steve.chatsystem.MessageManager;

import java.util.*;

import static org.steve.chatsystem.commands.ChatColor.*;

public class ChatColorGUIListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();

        if (player.hasMetadata("ChatColorGUI")) {
            HashMap<Integer, String> mapSlots = getIntegerStringHashMap();
            event.setCancelled(true);

            List<Integer> colorSlots = new ArrayList<>();
            for (int i = 0; i < 16; i++) {
                colorSlots.add(i + (int) Math.floor((double) i / 9) + 9);
            }

            String targetUUID = player.getMetadata("ChatColorGUITarget").stream()
                    .filter(v -> Objects.equals(v.getOwningPlugin(), ChatSystem.getInstance()))
                    .findFirst()
                    .map(MetadataValue::value)
                    .orElse("null")
                    .toString();
            if (Objects.equals(targetUUID, "null")) {
                MessageManager.send(player, "&cTarget not found!");
                player.closeInventory();
                return;
            }
            OfflinePlayer target = Bukkit.getOfflinePlayer(UUID.fromString(targetUUID));

            if (colorSlots.contains(slot)) {
                setChatColor(player, target, mapSlots.get(slot));
                if (!targetUUID.equals(player.getUniqueId().toString())) sendChatColorExecutor(player, target, mapSlots.get(slot));
                SoundManager.playSound(player, PluginConfig.soundsClickSuccess, PluginConfig.soundsClickSuccessVolume, PluginConfig.soundsClickSuccessPitch);
                ChatColorGUI.openChatColorGUI(player, target);
            }

            switch (slot) {
                case 28:
                    SoundManager.playSound(player, PluginConfig.soundsClickSuccess, PluginConfig.soundsClickSuccessVolume, PluginConfig.soundsClickSuccessPitch);
                    ChatColorAnvilGUI.openHexGUI(player, target);
                    break;
                case 30:
                    SoundManager.playSound(player, PluginConfig.soundsClickSuccess, PluginConfig.soundsClickSuccessVolume, PluginConfig.soundsClickSuccessPitch);
                    ChatColorAnvilGUI.openGradientGUI(player, target);
                    break;
                case 32:
                    if (DatabaseManager.loadPlayerData(target).isDefaultChatColor()) {
                        SoundManager.playSound(player, PluginConfig.soundsClickFail, PluginConfig.soundsClickFailVolume, PluginConfig.soundsClickFailPitch);
                        MessageManager.send(player, "&cThe style can't be changed if the chat color is default!");
                        break;
                    }
                    SoundManager.playSound(player, PluginConfig.soundsClickSuccess, PluginConfig.soundsClickSuccessVolume, PluginConfig.soundsClickSuccessPitch);
                    ChatColorGUI.openStyleGUI(player, target);
                    break;
                case 34:
                    SoundManager.playSound(player, PluginConfig.soundsClickSuccess, PluginConfig.soundsClickSuccessVolume, PluginConfig.soundsClickSuccessPitch);
                    resetChatColor(player, target);
                    ChatColorGUI.openChatColorGUI(player, target);
                    break;
            }

        } else if (player.hasMetadata("StyleGUI")) {
            event.setCancelled(true);

            String targetUUID = player.getMetadata("StyleGUITarget").stream()
                    .filter(v -> Objects.equals(v.getOwningPlugin(), ChatSystem.getInstance()))
                    .findFirst()
                    .map(MetadataValue::value)
                    .orElse("null")
                    .toString();
            if (Objects.equals(targetUUID, "null")) {
                MessageManager.send(player, "&cTarget not found!");
                player.closeInventory();
                return;
            }
            OfflinePlayer target = Bukkit.getOfflinePlayer(UUID.fromString(targetUUID));

            PlayerData playerData = DatabaseManager.loadPlayerData(target);

            switch (slot) {
                case 11 -> toggleStyle(player, target,
                        () -> DatabaseManager.setMagic(target, !playerData.magic()));
                case 12 -> toggleStyle(player, target,
                        () -> DatabaseManager.setBold(target, !playerData.bold()));
                case 13 -> toggleStyle(player, target,
                        () -> DatabaseManager.setStrikethrough(target, !playerData.strikethrough()));
                case 14 -> toggleStyle(player, target,
                        () -> DatabaseManager.setUnderlined(target, !playerData.underlined()));
                case 15 -> toggleStyle(player, target,
                        () -> DatabaseManager.setItalic(target, !playerData.italic()));
                case 22 -> {
                    SoundManager.playSound(player, PluginConfig.soundsClickSuccess, PluginConfig.soundsClickSuccessVolume, PluginConfig.soundsClickSuccessPitch);
                    ChatColorGUI.openChatColorGUI(player, target);
                }
            }

        }
    }

    private void toggleStyle(Player player, OfflinePlayer target, Runnable toggleAction) {
        SoundManager.playSound(player, PluginConfig.soundsClickSuccess, PluginConfig.soundsClickSuccessVolume, PluginConfig.soundsClickSuccessPitch);
        toggleAction.run();
        ChatColorGUI.openStyleGUI(player, target);
        PlayerData playerData = DatabaseManager.loadPlayerData(target);
        sendChatColorTarget(target, playerData.chatColor());
        sendChatColorExecutor(player, target, playerData.chatColor());

    }

    private static @NotNull HashMap<Integer, String> getIntegerStringHashMap() {
        HashMap<Integer, String> mapSlots = new HashMap<>();
        mapSlots.put(9, "f");
        mapSlots.put(10, "7");
        mapSlots.put(11, "8");
        mapSlots.put(12, "0");
        mapSlots.put(13, "1");
        mapSlots.put(14, "9");
        mapSlots.put(15, "b");
        mapSlots.put(16, "3");
        mapSlots.put(17, "2");
        mapSlots.put(19, "a");
        mapSlots.put(20, "e");
        mapSlots.put(21, "6");
        mapSlots.put(22, "c");
        mapSlots.put(23, "4");
        mapSlots.put(24, "d");
        mapSlots.put(25, "5");
        return mapSlots;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (player.hasMetadata("ChatColorGUI")) {
            player.removeMetadata("ChatColorGUI", ChatSystem.getInstance());
            player.removeMetadata("ChatColorGUITarget", ChatSystem.getInstance());
        } else if (player.hasMetadata("StyleGUI")) {
            player.removeMetadata("StyleGUI", ChatSystem.getInstance());
            player.removeMetadata("StyleGUITarget", ChatSystem.getInstance());

        }

    }
}
