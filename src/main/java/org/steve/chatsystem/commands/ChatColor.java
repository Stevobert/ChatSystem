package org.steve.chatsystem.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.steve.chatsystem.database.PlayerData;
import org.steve.chatsystem.gui.ChatColorGUI;
import org.steve.chatsystem.Color;
import org.steve.chatsystem.MessageManager;
import org.steve.chatsystem.config.PluginConfig;
import org.steve.chatsystem.database.DatabaseManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChatColor implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        boolean isPlayer = commandSender instanceof Player;

        if (args.length > 7) {
            MessageManager.send(commandSender, "&cToo many arguments!");
            return true;
        }

        if (args.length == 0) {
            if (isPlayer) {
                //gui self
                ChatColorGUI.openChatColorGUI((Player) commandSender);
            } else {
                MessageManager.send(commandSender, Color.colored(PluginConfig.messagesConsoleCommandError));
            }
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (!target.hasPlayedBefore()) {
            MessageManager.send(commandSender, "&cInvalid player: " + args[0]);
            return true;
        }

        if (args.length == 1) {
            if (isPlayer) {
                //gui target
                ChatColorGUI.openChatColorGUI((Player) commandSender, target);
            } else {
                MessageManager.send(commandSender, Color.colored(PluginConfig.messagesConsoleCommandError));
            }
            return true;
        }

        switch (args[1].toLowerCase()) {
            case "#default":
                if (args.length >= 3) {
                    MessageManager.send(commandSender, "&cToo many arguments!");
                    return true;
                }
                resetChatColor(commandSender, target);
                break;
            case "#hex":
                if (args.length == 2) {
                    MessageManager.send(commandSender, "&cPlease provide a hex color!");
                    return true;
                }
                if (args.length >= 4) {
                    MessageManager.send(commandSender, "&cPlease only provide one hex color!");
                    return true;
                }
                if (!Color.isHex(args[2])) {
                    MessageManager.send(commandSender, "&cInvalid color: " + args[2]);
                    return true;
                }
                setChatColor(commandSender, target, args[2]);
                break;
            case "#gradient":
                if (args.length < 4) {
                    MessageManager.send(commandSender, "&cPlease provide two hex color!");
                    return true;
                }
                if (args.length > 4) {
                    MessageManager.send(commandSender, "&cPlease only provide two hex colors!");
                    return true;
                }
                if (!Color.isHex(args[2])) {
                    MessageManager.send(commandSender, "&cInvalid color: " + args[2]);
                    return true;
                }
                if (!Color.isHex(args[3])) {
                    MessageManager.send(commandSender, "&cInvalid color: " + args[3]);
                    return true;
                }
                setChatColor(commandSender, target, args[2], args[3]);
                break;
            case "#style":
                if (DatabaseManager.loadPlayerData(target).isDefaultChatColor()) {
                    MessageManager.send(commandSender, "&cThe style can't be changed if the chat color is default!");
                    return true;
                }

                if (args.length == 2) {
                    MessageManager.send(commandSender, "&cPlease provide a style!");
                    return true;
                }

                if (args[2].equalsIgnoreCase("none")) {
                    if (args.length == 3) {
                        resetStyle(commandSender, target);
                    } else {
                        MessageManager.send(commandSender, "&cToo many arguments!");
                    }
                    return true;
                }

                boolean magic = false;
                boolean bold = false;
                boolean strikethrough = false;
                boolean underlined = false;
                boolean italic = false;

                for (int i = 2; i < args.length; i++) {
                    switch (args[i].toLowerCase()) {
                        case "magic" -> magic = true;
                        case "bold" -> bold = true;
                        case "strikethrough" -> strikethrough = true;
                        case "underlined" -> underlined = true;
                        case "italic" -> italic = true;
                        default -> {
                            MessageManager.send(commandSender, Color.colored("&cInvalid style: " + args[i]));
                            return true;
                        }
                    }
                }
                editStyle(commandSender, target, magic, bold, strikethrough, underlined, italic);
                break;
            default:
                if (!Color.isColorCode(args[1])) {
                    MessageManager.send(commandSender, "&cInvalid color: " + args[1]);
                    return true;
                }
                setChatColor(commandSender, target, args[1]);
                break;
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(commandSender instanceof Player player)) return List.of();
        String currentColor = DatabaseManager.loadPlayerData(player).chatColor();
        List<String> style = new ArrayList<>(List.of("none", "magic", "bold", "strikethrough", "underlined", "italic"));
        if (args.length >= 2 && args[1].equalsIgnoreCase("#style")) {
            if (args.length >= 3 && args[2].equalsIgnoreCase("none")) return List.of();
            List<String> s = List.of();
            for (int i = 1; i < args.length; i++) {
                if (i >= 3) s.remove("none");
                s = style;
                s.remove(args[i]);
            }
            return s;
        }
        switch (args.length) {
            case 1:
                return Bukkit.getOnlinePlayers()
                        .stream()
                        .map(Player::getName)
                        .collect(Collectors.toList());
            case 2:
                return new ArrayList<>(Arrays.asList("0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f","#default","#hex","#gradient","#style"));
            case 3:
                if (args[1].equalsIgnoreCase("#hex")) {
                    if (Color.isMiniMessageHex(currentColor)) return List.of(Color.cleanMiniMessageHex(currentColor));
                } else if (args[1].equalsIgnoreCase("#gradient")) {
                    if (Color.isMiniMessageGradient(currentColor)) return List.of(Color.cleanMiniMessageGradient(currentColor)[0]);
                }
            case 4:
                if (args[1].equalsIgnoreCase("#gradient")) {
                    if (Color.isMiniMessageGradient(currentColor)) return List.of(Color.cleanMiniMessageGradient(currentColor)[1]);
                }
            }
        return List.of();
    }

    public static void resetStyle(CommandSender sender, OfflinePlayer target) {
        editStyle(sender, target, false, false, false, false, false);
    }

    public static void editStyle(CommandSender sender, OfflinePlayer target, boolean magic, boolean bold, boolean strikethrough, boolean underlined, boolean italic) {
        DatabaseManager.setMagic(target, magic);
        DatabaseManager.setBold(target, bold);
        DatabaseManager.setStrikethrough(target, strikethrough);
        DatabaseManager.setUnderlined(target, underlined);
        DatabaseManager.setItalic(target, italic);
        PlayerData playerData = DatabaseManager.loadPlayerData(target);
        sendChatColorTarget(target, playerData.chatColor());
        sendChatColorExecutor(sender, target, playerData.chatColor());
    }

    public static void resetChatColor(CommandSender sender, OfflinePlayer target) {
        DatabaseManager.setMagic(target, false);
        DatabaseManager.setBold(target, false);
        DatabaseManager.setStrikethrough(target, false);
        DatabaseManager.setUnderlined(target, false);
        DatabaseManager.setItalic(target, false);
        setChatColor(sender, target, "default");
    }

    public static void setChatColor(CommandSender sender, OfflinePlayer target, String color1, String color2) {
        setChatColor(sender, target, Color.normalizeColor(color1, color2));
    }

    public static void setChatColor(CommandSender sender, OfflinePlayer target, String color) {
        color = Color.normalizeColor(color);
        DatabaseManager.setChatColor(target, color);
        sendChatColorTarget(target, color);
        sendChatColorExecutor(sender, target, color);
    }

    public static void sendChatColorExecutor(CommandSender sender, OfflinePlayer target, String color) {
        PlayerData playerData = DatabaseManager.loadPlayerData(target);
        String name = target.getName() == null ? "" : target.getName();
        Component message = Color.coloredMessage(PluginConfig.messagesChatcolorCommandUpdateChatcolorSender, List.of("%name%"), List.of(name));
        message = message.replaceText(builder -> builder
                .match("%preview%")
                .replacement(getColorString(color, playerData.getStyle()))
        );
        MessageManager.send(sender, message);
    }

    public static void sendChatColorTarget(OfflinePlayer target, String color) {
        if (!target.isOnline() || !PluginConfig.enableChatcolorCommandTargetFeedback) return;
        PlayerData playerData = DatabaseManager.loadPlayerData(target);
        Component message = Color.coloredMessage(PluginConfig.messagesChatcolorCommandUpdateChatcolorTarget, List.of(), List.of());
        message = message.replaceText(builder -> builder
                .match("%preview%")
                .replacement(getColorString(color, playerData.getStyle()))
        );
        MessageManager.send((CommandSender) target, message);
    }

    public static Component getColorString(String color, String decoration) {
        Component info;
        Component styleInfo = Component.empty();
        String copy;
        if (color.equalsIgnoreCase("default")) color = Color.normalizeColor(PluginConfig.defaultChatcolor);
        if (Color.isMiniMessageHex(color)) {
            String c = Color.cleanMiniMessageHex(color);
            info = Color.colored(" &8(#" + c + ")");
            copy = c;
        } else if (Color.isMiniMessageGradient(color)) {
            String[] c = Color.cleanMiniMessageGradient(color);
            info = Color.colored(" &8(#" + c[0] + ", #" + c[1] + ")");
            copy = String.join(" ", c[0], c[1]);
        } else {
            String c = Color.getColorValue(color);
            info = Component.text(" (&" + c + ")").color(NamedTextColor.DARK_GRAY);
            copy = c;
        }

        if (!decoration.isEmpty()) {
            List<String> styles = Color.getStyleCodes(decoration);
            String copyStyle = String.join(" ", styles);
            styleInfo = Component.text(" (&" + String.join(", &", styles) + ")").color(NamedTextColor.DARK_GRAY);
            styleInfo = styleInfo.hoverEvent(HoverEvent.showText(Color.colored("&eCopy: " + copyStyle))).clickEvent(ClickEvent.copyToClipboard(copyStyle));
        }

        info = info.hoverEvent(HoverEvent.showText(Color.colored("&eCopy: " + copy))).clickEvent(ClickEvent.copyToClipboard(copy));
        info = info
                .decoration(TextDecoration.OBFUSCATED, false)
                .decoration(TextDecoration.BOLD, false)
                .decoration(TextDecoration.STRIKETHROUGH, false)
                .decoration(TextDecoration.UNDERLINED, false)
                .decoration(TextDecoration.ITALIC, false);

        styleInfo = styleInfo
                .decoration(TextDecoration.OBFUSCATED, false)
                .decoration(TextDecoration.BOLD, false)
                .decoration(TextDecoration.STRIKETHROUGH, false)
                .decoration(TextDecoration.UNDERLINED, false)
                .decoration(TextDecoration.ITALIC, false);

        if (!PluginConfig.enableStyleInfo) styleInfo = Component.empty();
        if (!PluginConfig.enableColorInfo) info = Component.empty();

        return MiniMessage.miniMessage().deserialize(color + "<!italic>" + decoration + "This").append(info).append(styleInfo);
    }
}
