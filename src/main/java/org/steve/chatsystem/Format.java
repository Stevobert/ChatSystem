package org.steve.chatsystem;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.steve.chatsystem.config.PluginConfig;
import org.steve.chatsystem.database.DatabaseDefault;
import org.steve.chatsystem.database.DatabaseManager;
import org.steve.chatsystem.database.PlayerData;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Format {
    public static Component format(CommandSender sender, String msg, String permission) {
        boolean isPlayer = sender instanceof Player;
        String chatcolor;
        Player player = null;
        Component showItem = Component.empty();

        if (isPlayer) {
            player = (Player) sender;
            PlayerData playerData = DatabaseManager.loadPlayerData(player);
            chatcolor = playerData.chatColor() + playerData.getStyle();
        } else {
            chatcolor = Color.normalizeColor(PluginConfig.defaultChatcolor) + DatabaseDefault.getDefaultPlayerdata().getStyle();
        }
        Component message = Component.text(msg);

        if (isPlayer) {
            if (player.hasPermission("chatsystem.showitem") && player.getInventory().getItemInMainHand().getType() != Material.AIR) {
                ItemStack item = player.getInventory().getItemInMainHand();
                ItemMeta meta = item.getItemMeta();
                Component itemName;

                if (meta != null && meta.hasDisplayName()) {
                    itemName = meta.displayName();
                } else {
                    String formattedName = Arrays.stream(item.getType().toString().split("_"))
                            .map(word -> word.charAt(0) + word.substring(1).toLowerCase())
                            .collect(Collectors.joining(" "));
                    formattedName = TextTools.toProperCase(formattedName);
                    itemName = Component.text(formattedName, NamedTextColor.GRAY);
                }

                showItem = Component.text("[", NamedTextColor.GRAY)
                        .append(itemName == null ? Component.empty() : itemName)
                        .append(Component.text("]", NamedTextColor.GRAY))
                        .append(Component.space())
                        .append(Component.text("x", NamedTextColor.DARK_GRAY))
                        .append(Component.text(item.getAmount(), NamedTextColor.DARK_GRAY))
                        .hoverEvent(item.asHoverEvent())
                        .decoration(TextDecoration.OBFUSCATED, false)
                        .decoration(TextDecoration.BOLD, false)
                        .decoration(TextDecoration.STRIKETHROUGH, false)
                        .decoration(TextDecoration.UNDERLINED, false)
                        .decoration(TextDecoration.ITALIC, false);
            }
        }

        if (!isPlayer || player.hasPermission("chatsystem.chatcolor.use-color-codes")) {
            message = Color.colored(msg);
        }

        if (!isPlayer || player.hasPermission("chatsystem.ping.everyone")) {
            Pattern pattern = Pattern.compile(Pattern.quote(PluginConfig.everyonePingReplacePhrase), Pattern.CASE_INSENSITIVE);

            Component replacement;

            if (PluginConfig.everyonePingEnableCustom) {
                replacement = Color.colored(PluginConfig.everyonePingReplacement);
            } else {
                replacement = MiniMessage.miniMessage().deserialize("<yellow>@everyone</yellow>")
                        .decoration(TextDecoration.OBFUSCATED, false)
                        .decoration(TextDecoration.BOLD, false)
                        .decoration(TextDecoration.STRIKETHROUGH, false)
                        .decoration(TextDecoration.UNDERLINED, false)
                        .decoration(TextDecoration.ITALIC, false);
            }

            String serializedMessage = MiniMessage.miniMessage().serialize(message);
            serializedMessage = pattern.matcher(serializedMessage).replaceAll("<ping>");

            message = MiniMessage.miniMessage().deserialize(serializedMessage, Placeholder.component("ping", replacement));
        }

        for (Player loopPlayer : Bukkit.getOnlinePlayers()) {
            String name = loopPlayer.getName();
            if (permission != null && !loopPlayer.hasPermission(permission) && msg.toLowerCase().contains(name.toLowerCase())) continue;

            Pattern pattern = Pattern.compile(Pattern.quote(name), Pattern.CASE_INSENSITIVE);

            Component replacement;

            if (PluginConfig.pingEnableCustom) {
                String format = PluginConfig.pingCustom;
                String reset =  format.startsWith("&r") ? "<!obfuscated><!bold><!strikethrough><!underlined><!italic><white>" : "";
                MiniMessage mm = MiniMessage.miniMessage();
                Component legacy = Color.colored(format);
                String minimessage = mm.serialize(legacy).replace("\\", "").replace("%pinged%", name);
                replacement = mm.deserialize(reset + minimessage);
            } else {
                replacement= MiniMessage.miniMessage().deserialize("<yellow>" + name + "</yellow>")
                        .decoration(TextDecoration.OBFUSCATED, false)
                        .decoration(TextDecoration.BOLD, false)
                        .decoration(TextDecoration.STRIKETHROUGH, false)
                        .decoration(TextDecoration.UNDERLINED, false)
                        .decoration(TextDecoration.ITALIC, false);
            }

            String serializedMessage = MiniMessage.miniMessage().serialize(message);
            serializedMessage = pattern.matcher(serializedMessage).replaceAll("<name>");

            message = MiniMessage.miniMessage().deserialize(serializedMessage, Placeholder.component("name", replacement));
        }

        if (!showItem.style().isEmpty()) {
            final Component item = showItem;
            message = message.replaceText(builder -> builder.matchLiteral("[i]").replacement(item));
        }
        String template = chatcolor + "<content>";
        message = MiniMessage.miniMessage().deserialize(template, Placeholder.component("content", message));

        return message;
    }

    public static void pingPlayer(CommandSender sender, String message, String permission) {
        for (Player loopPlayer : Bukkit.getOnlinePlayers()) {
            if (permission != null && !loopPlayer.hasPermission(permission)) continue;

            String name = loopPlayer.getName();
            if (!message.toLowerCase().contains(name.toLowerCase())) continue;

            PlayerData loopPlayerData = DatabaseManager.loadPlayerData(loopPlayer);
            if (!loopPlayerData.disablepings()) {
                MessageManager.sendTitle(loopPlayer, "", "&c" + sender.getName() + " mentioned you!");
                SoundManager.playSound(loopPlayer, PluginConfig.soundsPing, PluginConfig.soundsPingVolume, PluginConfig.soundsPingPitch);
            }
        }
    }

    public static void pingEveryone(CommandSender sender, String message, String permission) {
        for (Player loopPlayer : Bukkit.getOnlinePlayers()) {
            if (permission != null && !loopPlayer.hasPermission(permission)) continue;
            if (!message.contains(PluginConfig.everyonePingEnableCustom ? PluginConfig.everyonePingReplacePhrase : "@everyone")) continue;
            PlayerData loopPlayerData = DatabaseManager.loadPlayerData(loopPlayer);
            if (!loopPlayerData.disablepings()) {
                MessageManager.sendTitle(loopPlayer, "", "&c" + sender.getName() + " mentioned you!");
                SoundManager.playSound(loopPlayer, PluginConfig.soundsPing, PluginConfig.soundsPingVolume, PluginConfig.soundsPingPitch);
            }
        }
    }
}
