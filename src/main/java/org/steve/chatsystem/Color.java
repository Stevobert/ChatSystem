package org.steve.chatsystem;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Color {
    public static Component coloredNoItalic(String string) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(string).decoration(TextDecoration.ITALIC, false);
    }

    public static Component legacyColored(String string) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(string);
    }

    public static Component coloredMessage(String input, List<String> placeholders, List<String> replacements) {
        if (placeholders.size() != replacements.size() || placeholders.isEmpty()) return colored(input);
        input = MiniMessage.miniMessage().serialize(colored(input));
        for (int i = 0; i < placeholders.size(); i++) {
            input = input.replace(placeholders.get(i), replacements.get(i));
        }
        return colored(input);
    }

    public static Component colored(String string) {
        Component legacy = Color.legacyColored(string);
        String minimessage = MiniMessage.miniMessage().serialize(legacy).replace("\\", "");
        return MiniMessage.miniMessage().deserialize(minimessage);
    }

    public static boolean isHex(String s) { return s.matches("[0-9a-fA-F]{6}"); }
    public static boolean isHex2(String s) { return s.matches("#[0-9a-fA-F]{6}"); }
    public static boolean isColorCode(String s) { return s.matches("[0-9a-fA-F]"); }

    public static boolean isMiniMessageHex(String s) { return s.matches("<#[0-9a-fA-F]{6}>"); }
    public static boolean isMiniMessageGradient(String s) { return s.matches("<gradient:#[0-9a-fA-F]{6}:#[0-9a-fA-F]{6}>"); }

    public static String cleanMiniMessageHex(String s) {
        return s.replaceAll("[<>#]", "");
    }
    public static String[] cleanMiniMessageGradient(String s) {
        s = s.replace("<gradient:", "").replaceAll("[<>#]", "");
        return s.split(":");
    }

    public static String getColorName(String color) {
        HashMap<String, String> map = new HashMap<>();
        map.put("a", "<green>");
        map.put("b", "<aqua>");
        map.put("c", "<red>");
        map.put("d", "<light_purple>");
        map.put("e", "<yellow>");
        map.put("f", "<white>");
        map.put("0", "<black>");
        map.put("1", "<dark_blue>");
        map.put("2", "<dark_green>");
        map.put("3", "<dark_aqua>");
        map.put("4", "<dark_red>");
        map.put("5", "<dark_purple>");
        map.put("6", "<gold>");
        map.put("7", "<gray>");
        map.put("8", "<dark_gray>");
        map.put("9", "<blue>");
        return map.getOrDefault(color, "default");
    }

    public static String getColorValue(String color) {
        HashMap<String, String> map = new HashMap<>();
        map.put("<green>", "a");
        map.put("<aqua>", "b");
        map.put("<red>", "c");
        map.put("<light_purple>", "d");
        map.put("<yellow>", "e");
        map.put("<white>", "f");
        map.put("<black>", "0");
        map.put("<dark_blue>", "1");
        map.put("<dark_green>", "2");
        map.put("<dark_aqua>", "3");
        map.put("<dark_red>", "4");
        map.put("<dark_purple>", "5");
        map.put("<gold>", "6");
        map.put("<gray>", "7");
        map.put("<dark_gray>", "8");
        map.put("<blue>", "9");
        return map.getOrDefault(color, "x");
    }

    public static List<String> getStyleCodes(String string) {
        List<String> output = new ArrayList<>();
        if (string.contains("<obfuscated>")) output.add("k");
        if (string.contains("<bold>")) output.add("l");
        if (string.contains("<strikethrough>")) output.add("m");
        if (string.contains("<underlined>")) output.add("n");
        if (string.contains("<italic>")) output.add("o");
        return output;
    }

    public static String normalizeColor(String color1, String color2) {
        if (isHex(color1) && isHex(color2)) return "<gradient:#" + color1 + ":#" + color2 + ">";
        return "default";
    }

    public static String normalizeColor(String color) {
        if (isMiniMessageHex(color) || isMiniMessageGradient(color)) return color;
        String output;
        if (isHex2(normalizeHex(color))) {
            output = "<#" + color + ">";
        } else {
            output = getColorName(color);
        }
        return output;
    }

    public static String normalizeHex(String hex) {
        if (!hex.matches("#?[0-9a-fA-F]{6}")) return hex;
        return "#" + hex.replace("#", "").toLowerCase();
    }
}
