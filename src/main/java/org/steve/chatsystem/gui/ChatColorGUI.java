package org.steve.chatsystem.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.steve.chatsystem.ChatSystem;
import org.steve.chatsystem.Color;
import org.steve.chatsystem.CustomHead;
import org.steve.chatsystem.commands.ChatColor;
import org.steve.chatsystem.database.DatabaseDefault;
import org.steve.chatsystem.database.DatabaseManager;
import org.steve.chatsystem.database.PlayerData;

import java.util.*;

public class ChatColorGUI {
    public static void openChatColorGUI(Player player) {
        openChatColorGUI(player, player);
    }

    public static void openChatColorGUI(Player player, OfflinePlayer target) {
        String name;
        if (target != player) name = " &8of &#ff0000&l" + target.getName(); else name = "";
        String title = "Chatcolor";
        Inventory inventory = Bukkit.createInventory(player, 9 * 4, Color.coloredNoItalic(title + name));
        PlayerData playerData = DatabaseManager.loadPlayerData(target);
        String chatcolor = playerData.chatColor();

        ItemStack background = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta backgroundMeta = background.getItemMeta();
        if (Bukkit.getBukkitVersion().startsWith("1.21")) backgroundMeta.setHideTooltip(true);
        backgroundMeta.displayName(Color.coloredNoItalic(""));
        background.setItemMeta(backgroundMeta);

        for (int i = 0; i <= 8; i++) {
            inventory.setItem(i, background);
        }
        for (int i = 27; i <= 35; i++) {
            inventory.setItem(i, background);
        }

        ItemStack info = new ItemStack(Material.COMPASS);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.displayName(Color.coloredNoItalic("&e&lCurrent Color"));
        if (target != player) name = "&#ff0000&l" + target.getName() + "&7's"; else name = "&7Your";
        infoMeta.lore(List.of(Color.coloredNoItalic(name + " &7current color:"), Color.coloredNoItalic(""), ChatColor.getColorString(chatcolor, playerData.getStyle())));
        info.setItemMeta(infoMeta);

        inventory.setItem(4, info);

        ItemStack hexColor = new ItemStack(Material.NETHER_STAR);
        ItemMeta hexColorMeta = hexColor.getItemMeta();
        hexColorMeta.displayName(Color.coloredNoItalic("&#A3DEFB&lHex Color"));
        if (Color.isMiniMessageHex(chatcolor) && !playerData.isDefaultChatColor()) {
            hexColorMeta.lore(List.of(Color.coloredNoItalic("&a&l➥ &aSelected")));
        } else {
            hexColorMeta.lore(List.of(Color.coloredNoItalic("&7&l➥ &7Click to set")));
        }
        hexColor.setItemMeta(hexColorMeta);

        inventory.setItem(28, hexColor);

        ItemStack gradient = CustomHead.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjg3ZmQyM2E3ODM2OWJkMzg3NWRhODg5NmYxNTBjNGFmOWYyMzM3NGUwNDhlMzA5MTM5MDBlM2ZkZDc3ODU5YSJ9fX0=");
        ItemMeta gradientMeta = gradient.getItemMeta();
        gradientMeta.displayName(Color.coloredNoItalic("&#FB2F2F&lG&#FC4735&lr&#FC5F3B&la&#FD7741&ld&#FD8E47&li&#FEA64D&le&#FEBE53&ln&#FFD659&lt"));
        if (Color.isMiniMessageGradient(chatcolor) && !playerData.isDefaultChatColor()) {
            gradientMeta.lore(List.of(Color.coloredNoItalic("&a&l➥ &aSelected")));
        } else {
            gradientMeta.lore(List.of(Color.coloredNoItalic("&7&l➥ &7Click to set")));
        }
        gradient.setItemMeta(gradientMeta);

        inventory.setItem(30, gradient);

        ItemStack style = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta styleMeta = style.getItemMeta();
        styleMeta.displayName(Color.coloredNoItalic("&e&lConfigure Style"));
        if (playerData.isDefaultChatColor()) {
            styleMeta.lore(List.of(Color.coloredNoItalic("&c&l➥ &cCan't change when chat color is default")));
        } else {
            styleMeta.lore(List.of(Color.coloredNoItalic("&7&l➥ &7Click to configure")));
        }
        style.setItemMeta(styleMeta);

        inventory.setItem(32, style);

        ItemStack resetColor = new ItemStack(Material.STRUCTURE_VOID);
        ItemMeta resetColorMeta = resetColor.getItemMeta();
        resetColorMeta.displayName(Color.coloredNoItalic("&c&lReset Color"));
        resetColorMeta.lore(List.of(Color.coloredNoItalic("&7Default: ").append(ChatColor.getColorString(DatabaseDefault.getDefaultPlayerdata().chatColor(), DatabaseDefault.getDefaultPlayerdata().getStyle())), Color.coloredNoItalic(""), Color.coloredNoItalic("&7&l➥ &7Click to reset color")));
        resetColor.setItemMeta(resetColorMeta);

        inventory.setItem(34, resetColor);

        List<String> colorHeadsValue = List.of(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzY2YTVjOTg5MjhmYTVkNGI1ZDViOGVmYjQ5MDE1NWI0ZGRhMzk1NmJjYWE5MzcxMTc3ODE0NTMyY2ZjIn19fQ",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWM4ZTBkZGYyNDMyZjQzMzJiODc2OTFiNTk1MmM3Njc5NzYzZWY0ZjI3NWI4NzRlOWJjZWI4ODhlZDViNWI5In19fQ==",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmY5YmI5ZTU2MTI1YzgyMjdiOTRiYmRhOWY2ZTBmODYyOTMxYzIyOTI1NWJhOGYxMjA1ZDEzYzQ0YzFiYjU2MSJ9fX0",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmE1MmQ1NzlhZmUyZmRmN2I4ZWNmYTc0NmNkMDE2MTUwZDk2YmViNzUwMDliYjI3MzNhZGUxNWQ0ODdjNDJhMSJ9fX0=",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTExOTRmZTllZGY1ODNjMGViZTdkYzFkMzQzMDliZWVmYjIyOWJiMTViNmE4YzNjN2IwYzc2ZGUyN2M4YjdiZiJ9fX0=",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2Y5YzMyMTM4Yzk3NjRjNjM5YWViZDgxOWNkOTE5OTJhZWQwMWJmNDQ4ZjBlNzEwYTAzYWI0NDNhYzQ5MGVlOSJ9fX0=",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmQ2YThiNDdkYTkyM2I3ZDEwMTQyNDQ3ZmRiZGNmZDFlOGU4MmViNDg0OTY0MjUyYmIzNmRkYjVmNzNiNTFjMiJ9fX0=",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzFmNTcwNTExMzBlODUwODQ4ZThlMzdlNzIxMTBhMTZmMDlkYmRhYjdkOWQ2ZTMzYTlmZWNmZDM0OGQ1YTExMCJ9fX0=",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWUzOGZhMzEzMWUyZGEwNGE4ZjhkNTA4NzJhODIzMmQ3ZDlkZWEzNDBkMDZjOTA5N2ZmYTNjYzQ4MjA4ZGYxZCJ9fX0=",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTI1YjhlZWQ1YzU2NWJkNDQwZWM0N2M3OWMyMGQ1Y2YzNzAxNjJiMWQ5YjVkZDMxMDBlZDYyODNmZTAxZDZlIn19fQ==",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTg5YjkzZmQ2MTZlZDM2NzBjY2Y2NDdhMGY5MzgwMzk4YzBkNDYxNTYzNGYyZGVmZjQ2YzZlZGJkYzcxMjg4NSJ9fX0=",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjA5MGQwOWUxNzNlZTM0MTM4YzNiMDFiNDhlZTBiZTUzNGJiYjFhY2UwZGRmNWZmOThlNjZmN2IwMjExMzk5NSJ9fX0=",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjNmNzliMjA3ZDYxZTEyMjUyM2I4M2Q2MTUwOGQ5OWNmYTA3OWQ0NWJmMjNkZjJhOWE1MTI3ZjkwNzFkNGIwMCJ9fX0=",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjhkNDA5MzUyNzk3NzFhZGM2MzkzNmVkOWM4NDYzYWJkZjVjNWJhNzhkMmU4NmNiMWVjMTBiNGQxZDIyNWZiIn19fQ==",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGNmMjgzNTE4MGNiZmVjM2IzMTdkNmE0NzQ5MWE3NGFlNzE0MzViYTE2OWE1NzkyNWI5MDk2ZWEyZjljNjFiNiJ9fX0=",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWI4MmU3MmI4ZTQ4MzJlNWExMTRhYjBmYzEyN2M4YWNiODNmMzFmZDRkMjY2ZDA4YjJjYWNjNWI2NDAxYTQwMCJ9fX0="
                );

        List<String> colorHeadsName = List.of(
                "&f&lWhite",
                "&7&lGray",
                "&8&lDark Gray",
                "&0&lBlack",
                "&1&lDark Blue",
                "&9&lBlue",
                "&b&lAqua",
                "&3&lDark Aqua",
                "&2&lDark Green",
                "&a&lGreen",
                "&e&lYellow",
                "&6&lGold",
                "&c&lRed",
                "&4&lDark Red",
                "&d&lLight Purple",
                "&5&lDark Purple"
        );

        List<String> colors = List.of("f", "7", "8", "0", "1", "9", "b", "3", "2", "a", "e", "6", "c", "4", "d", "5");

        for (int i = 0; i < 16; i++) {
            int slot = i + (int) Math.floor((double) i / 9) + 9;
            ItemStack head = CustomHead.getCustomHead(colorHeadsValue.get(i));
            ItemMeta headMeta = head.getItemMeta();
            headMeta.displayName(Color.coloredNoItalic(colorHeadsName.get(i)));
            if (!playerData.isDefaultChatColor() && Objects.equals(chatcolor, Color.normalizeColor(colors.get(i)))) {
                headMeta.lore(List.of(Color.coloredNoItalic("&a&l➥ &aSelected")));
            } else {
                headMeta.lore(List.of(Color.coloredNoItalic("&7&l➥ &7Click to select")));
            }
            head.setItemMeta(headMeta);
            inventory.setItem(slot, head);
        }

        player.openInventory(inventory);
        player.setMetadata("ChatColorGUI", new FixedMetadataValue(ChatSystem.getInstance(), inventory));
        player.setMetadata("ChatColorGUITarget", new FixedMetadataValue(ChatSystem.getInstance(), target.getUniqueId().toString()));
    }

    public static void openStyleGUI(Player player, OfflinePlayer target) {
        String name;
        if (target != player) name = " &8of &#ff0000&l" + target.getName(); else name = "";
        String title = "Style";
        Inventory inventory = Bukkit.createInventory(player, 9 * 3, Color.coloredNoItalic(title + name));
        PlayerData playerData = DatabaseManager.loadPlayerData(target);

        HashMap<String, Boolean> statusMap = new HashMap<>();
        statusMap.put("Magic", playerData.magic());
        statusMap.put("Bold", playerData.bold());
        statusMap.put("Strikethrough", playerData.strikethrough());
        statusMap.put("Underlined", playerData.underlined());
        statusMap.put("Italic", playerData.italic());

        HashMap<String, String> valueMap = new HashMap<>();
        valueMap.put("Magic", "&k");
        valueMap.put("Bold", "&l");
        valueMap.put("Strikethrough", "&m");
        valueMap.put("Underlined", "&n");
        valueMap.put("Italic", "&o");

        ArrayList<String> names = new ArrayList<>(Arrays.asList("Magic", "Bold", "Strikethrough", "Underlined", "Italic"));

        ItemStack background = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta backgroundMeta = background.getItemMeta();
        if (Bukkit.getBukkitVersion().startsWith("1.21")) backgroundMeta.setHideTooltip(true);
        backgroundMeta.displayName(Color.coloredNoItalic(""));
        background.setItemMeta(backgroundMeta);

        for (int i = 0; i <= 9; i++) {
            inventory.setItem(i, background);
        }
        for (int i = 17; i <= 26; i++) {
            inventory.setItem(i, background);
        }

        int i = 0;
        for (String s : names) {
            i++;
            if (statusMap.get(s)) {
                ItemStack item = new ItemStack(Material.LIME_DYE);
                ItemMeta meta = item.getItemMeta();
                meta.displayName(Color.coloredNoItalic("&a&l" + s + " &7(" + valueMap.get(s) + "text&7)"));
                meta.lore(List.of(Color.coloredNoItalic("&a☑ Enabled"), Color.coloredNoItalic(""), Color.coloredNoItalic("&7&l➥ &7Click to disable")));
                item.setItemMeta(meta);
                inventory.setItem(10 + i, item);
            } else {
                ItemStack item = new ItemStack(Material.RED_DYE);
                ItemMeta meta = item.getItemMeta();
                meta.displayName(Color.coloredNoItalic("&c&l" + s + " &7(" + valueMap.get(s) + "text&7)"));
                meta.lore(List.of(Color.coloredNoItalic("&c☒ Disabled"), Color.coloredNoItalic(""), Color.coloredNoItalic("&7&l➥ &7Click to enable")));
                item.setItemMeta(meta);
                inventory.setItem(10 + i, item);
            }
        }

        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Color.coloredNoItalic("&bBack"));
        meta.lore(List.of(Color.coloredNoItalic("&7&l➥ &7Click to go back")));
        item.setItemMeta(meta);
        inventory.setItem(22, item);

        player.openInventory(inventory);
        player.setMetadata("StyleGUI", new FixedMetadataValue(ChatSystem.getInstance(), inventory));
        player.setMetadata("StyleGUITarget", new FixedMetadataValue(ChatSystem.getInstance(), target.getUniqueId().toString()));
    }
}
