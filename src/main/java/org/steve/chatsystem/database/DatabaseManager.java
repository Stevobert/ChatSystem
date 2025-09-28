package org.steve.chatsystem.database;

import org.bukkit.OfflinePlayer;
import org.steve.chatsystem.ChatSystem;

import java.sql.*;
import java.util.logging.Level;

public class DatabaseManager {
    public static void savePlayerData(OfflinePlayer player, String chatColor, boolean magic, boolean bold, boolean strikethrough, boolean underlined, boolean italic, boolean disablepings, boolean staffchat, boolean adminchat) {
        String uuid = player.getUniqueId().toString();
        try (Connection connection = DriverManager.getConnection(Database.DATABASE_URL)) {
            String insertQuery = "INSERT OR REPLACE INTO player_data (uuid, chatcolor, magic, bold, strikethrough, underlined, italic, disablepings, staffchat, adminchat) VALUES (?,?,?,?,?,?,?,?,?,?);";
            try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
                statement.setString(1, uuid);
                statement.setString(2, chatColor);
                statement.setBoolean(3, magic);
                statement.setBoolean(4, bold);
                statement.setBoolean(5, strikethrough);
                statement.setBoolean(6, underlined);
                statement.setBoolean(7, italic);
                statement.setBoolean(8, disablepings);
                statement.setBoolean(9, staffchat);
                statement.setBoolean(10, adminchat);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            ChatSystem.getInstance().getLogger().log(Level.SEVERE, "Failed to save data for " + player.getName() + " (UUID: " + uuid + ")", e);
        }
    }

    public static void setChatColor(OfflinePlayer player, String chatColor) {
        PlayerData playerData = loadPlayerData(player);
        savePlayerData(player, chatColor, playerData.magic(), playerData.bold(), playerData.strikethrough(), playerData.underlined(), playerData.italic(), playerData.disablepings(), playerData.staffchat(), playerData.adminchat());
    }

    public static void setDisablePings(OfflinePlayer player, boolean disablepings) {
        PlayerData playerData = loadPlayerData(player);
        savePlayerData(player, playerData.isDefaultChatColor() ? DatabaseDefault.getDefaultChatcolor() : playerData.chatColor(), playerData.magic(), playerData.bold(), playerData.strikethrough(), playerData.underlined(), playerData.italic(), disablepings, playerData.staffchat(), playerData.adminchat());
    }

    public static void setStaffchat(OfflinePlayer player, boolean staffchat) {
        PlayerData playerData = loadPlayerData(player);
        savePlayerData(player, playerData.isDefaultChatColor() ? DatabaseDefault.getDefaultChatcolor() : playerData.chatColor(), playerData.magic(), playerData.bold(), playerData.strikethrough(), playerData.underlined(), playerData.italic(), playerData.disablepings(), staffchat, playerData.adminchat());
    }

    public static void setAdminchat(OfflinePlayer player, boolean adminchat) {
        PlayerData playerData = loadPlayerData(player);
        savePlayerData(player, playerData.isDefaultChatColor() ? DatabaseDefault.getDefaultChatcolor() : playerData.chatColor(), playerData.magic(), playerData.bold(), playerData.strikethrough(), playerData.underlined(), playerData.italic(), playerData.disablepings(), playerData.staffchat(), adminchat);
    }

    public static void setMagic(OfflinePlayer player, boolean magic) {
        PlayerData playerData = loadPlayerData(player);
        savePlayerData(player, playerData.isDefaultChatColor() ? DatabaseDefault.getDefaultChatcolor() : playerData.chatColor(), magic, playerData.bold(), playerData.strikethrough(), playerData.underlined(), playerData.italic(), playerData.disablepings(), playerData.staffchat(), playerData.adminchat());
    }

    public static void setBold(OfflinePlayer player, boolean bold) {
        PlayerData playerData = loadPlayerData(player);
        savePlayerData(player, playerData.isDefaultChatColor() ? DatabaseDefault.getDefaultChatcolor() : playerData.chatColor(), playerData.magic(), bold, playerData.strikethrough(), playerData.underlined(), playerData.italic(), playerData.disablepings(), playerData.staffchat(), playerData.adminchat());
    }

    public static void setStrikethrough(OfflinePlayer player, boolean strikethrough) {
        PlayerData playerData = loadPlayerData(player);
        savePlayerData(player, playerData.isDefaultChatColor() ? DatabaseDefault.getDefaultChatcolor() : playerData.chatColor(), playerData.magic(), playerData.bold(), strikethrough, playerData.underlined(), playerData.italic(), playerData.disablepings(), playerData.staffchat(), playerData.adminchat());
    }

    public static void setUnderlined(OfflinePlayer player, boolean underlined) {
        PlayerData playerData = loadPlayerData(player);
        savePlayerData(player, playerData.isDefaultChatColor() ? DatabaseDefault.getDefaultChatcolor() : playerData.chatColor(), playerData.magic(), playerData.bold(), playerData.strikethrough(), underlined, playerData.italic(), playerData.disablepings(), playerData.staffchat(), playerData.adminchat());
    }

    public static void setItalic(OfflinePlayer player, boolean italic) {
        PlayerData playerData = loadPlayerData(player);
        savePlayerData(player, playerData.isDefaultChatColor() ? DatabaseDefault.getDefaultChatcolor() : playerData.chatColor(), playerData.magic(), playerData.bold(), playerData.strikethrough(), playerData.underlined(), italic, playerData.disablepings(), playerData.staffchat(), playerData.adminchat());
    }

    public static PlayerData loadPlayerData(OfflinePlayer player) {
        String uuid = player.getUniqueId().toString();
        try (Connection connection = DriverManager.getConnection(Database.DATABASE_URL)) {
            String selectQuery = "SELECT * FROM player_data WHERE uuid = ?;";
            try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {
                statement.setString(1, uuid);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    String chatColor = resultSet.getString("chatcolor");
                    boolean magic = resultSet.getBoolean("magic");
                    boolean bold = resultSet.getBoolean("bold");
                    boolean strikethrough = resultSet.getBoolean("strikethrough");
                    boolean underlined = resultSet.getBoolean("underlined");
                    boolean italic = resultSet.getBoolean("italic");
                    boolean disablepings = resultSet.getBoolean("disablepings");
                    boolean staffchat = resultSet.getBoolean("staffchat");
                    boolean adminchat = resultSet.getBoolean("adminchat");
                    return new PlayerData(chatColor, magic, bold, strikethrough, underlined, italic, disablepings, staffchat, adminchat);
                }
            }
        } catch (SQLException e) {
            ChatSystem.getInstance().getLogger().log(Level.SEVERE, "Failed to load data for" + player.getName() + "(UUID: " + uuid + "), using default data instead", e);
        }
        return DatabaseDefault.getDefaultPlayerdata();
    }
}
