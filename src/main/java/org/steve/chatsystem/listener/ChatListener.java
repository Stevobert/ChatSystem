package org.steve.chatsystem.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.clip.placeholderapi.PlaceholderAPI;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.steve.chatsystem.*;
import org.steve.chatsystem.commands.Adminchat;
import org.steve.chatsystem.commands.MuteChat;
import org.steve.chatsystem.commands.Staffchat;
import org.steve.chatsystem.config.PluginConfig;
import org.steve.chatsystem.database.DatabaseManager;
import org.steve.chatsystem.database.PlayerData;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.time.Instant;

public class ChatListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onChat(AsyncChatEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        Component message = event.message();
        String originalMessage = PlainTextComponentSerializer.plainText().serialize(message);
        PlayerData playerData = DatabaseManager.loadPlayerData(player);

        // staffchat
        if (player.hasPermission("chatsystem.command.staffchat") && playerData.staffchat()) {
            Staffchat.sendStaffchat(player, originalMessage);
            event.setCancelled(true);
            return;
        }

        // adminchat
        if (player.hasPermission("chatsystem.command.adminchat") && playerData.adminchat()) {
            Adminchat.sendAdminchat(player, originalMessage);
            event.setCancelled(true);
            return;
        }

        // mutechat
        if (!player.hasPermission("chatsystem.bypass.mutechat") && MuteChat.muteChat) {
            MessageManager.send(player, Color.coloredMessage(PluginConfig.messagesChatSendErrorsChatIsMuted, List.of(), List.of()));
            event.setCancelled(true);
            return;
        }

        // check cooldown
        Instant now = Instant.now();
        Instant lastMessage = Instant.parse((String) player.getMetadata("cooldown").stream()
                .filter(v -> Objects.equals(v.getOwningPlugin(), ChatSystem.getInstance()))
                .findFirst()
                .map(MetadataValue::value)
                .orElse(Instant.EPOCH.toString()));

        if (PluginConfig.enableChatCooldown) {
            if (!player.hasPermission("chatsystem.bypass.cooldown")) {
                if (Duration.between(lastMessage, now).getSeconds() >= PluginConfig.cooldownTime) {
                    player.setMetadata("cooldown", new FixedMetadataValue(ChatSystem.getInstance(), now.toString()));
                } else {
                    MessageManager.send(player, Color.coloredMessage(PluginConfig.messagesChatSendErrorsMessageCooldown, List.of("%time%"), List.of(String.valueOf(PluginConfig.cooldownTime))));
                    event.setCancelled(true);
                    return;
                }
            }
        }

        // check duplicated
        if (PluginConfig.enableNoDuplicateMessages) {
            if (!player.hasPermission("chatsystem.bypass.duplicated")) {
                String oldMessage = (String) player.getMetadata("duplicated").stream()
                        .filter(v -> Objects.equals(v.getOwningPlugin(), ChatSystem.getInstance()))
                        .findFirst()
                        .map(MetadataValue::value)
                        .orElse("null");

                if (!(oldMessage.equalsIgnoreCase(originalMessage))) {
                    if (Duration.between(lastMessage, now).getSeconds() >= PluginConfig.cooldownTime) {
                        player.setMetadata("duplicated", new FixedMetadataValue(ChatSystem.getInstance(), originalMessage));
                    }
                } else {
                    MessageManager.send(player, Color.coloredMessage(PluginConfig.messagesChatSendErrorsDuplicatedMessage, List.of(), List.of()));
                    event.setCancelled(true);
                    return;
                }
            }
        }

        // format
        message = Format.format(player, originalMessage, null);

        // ping
        Format.pingPlayer(player, originalMessage, null);
        if (player.hasPermission("chatsystem.ping.everyone")) Format.pingEveryone(player, originalMessage, null);

        // change message
        Component NameTag;
        if (PluginConfig.chatformatEnableCustom) {
            NameTag = Color.colored(PlaceholderAPI.setPlaceholders(event.getPlayer(), PluginConfig.chatformatCustom));
        } else {
            String prefix = PluginConfig.chatformatPrefix;
            String suffix = PluginConfig.chatformatSuffix;
            String arrow = PluginConfig.chatformatArrow;
            NameTag = Color.colored(PlaceholderAPI.setPlaceholders(event.getPlayer(), prefix + player.getName() + suffix + arrow));
        }

        Component finalMessage = message;
        event.renderer((source, sourceDisplayName, msg, viewer) -> NameTag.append(finalMessage));
    }
}
