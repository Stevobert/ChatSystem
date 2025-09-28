package org.steve.chatsystem.database;

import org.steve.chatsystem.Color;
import org.steve.chatsystem.config.PluginConfig;

import java.util.Map;

public record PlayerData(String chatColor, boolean magic, boolean bold, boolean strikethrough, boolean underlined, boolean italic, boolean disablepings, boolean staffchat, boolean adminchat) {

    @Override
    public String chatColor() {
        if (chatColor.equalsIgnoreCase("default")) {
            String d = PluginConfig.defaultChatcolor;
            if (Color.isMiniMessageGradient(d)) return d;
            return Color.normalizeColor(d);
        }
        return chatColor;
    }

    public boolean isDefaultChatColor() {
        return chatColor.equalsIgnoreCase("default");
    }

    public String getStyle() {
        boolean isDefault = this.isDefaultChatColor();
        StringBuilder output = new StringBuilder();

        Map<String, Boolean> playerStyles = Map.of(
                "<obfuscated>", magic(),
                "<bold>", bold(),
                "<strikethrough>", strikethrough(),
                "<underlined>", underlined(),
                "<italic>", italic()
        );

        Map<String, Boolean> defaultStyles = Map.of(
                "<obfuscated>", PluginConfig.defaultMagic,
                "<bold>", PluginConfig.defaultBold,
                "<strikethrough>", PluginConfig.defaultStrikethrough,
                "<underlined>", PluginConfig.defaultUnderlined,
                "<italic>", PluginConfig.defaultItalic
        );

        for (String style : playerStyles.keySet()) {
            if (isDefault) {
                if (defaultStyles.get(style)) output.append(style);
            } else {
                if (playerStyles.get(style)) output.append(style);
            }
        }

        return output.toString();
    }
}
