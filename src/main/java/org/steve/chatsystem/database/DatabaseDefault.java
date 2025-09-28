package org.steve.chatsystem.database;

import org.bukkit.OfflinePlayer;
import org.steve.chatsystem.ChatSystem;

import java.sql.*;
import java.util.logging.Level;

public class DatabaseDefault {
    private static final String DEFAULT_CHATCOLOR = "default";
    private static final boolean DEFAULT_MAGIC = false;
    private static final boolean DEFAULT_BOLD = false;
    private static final boolean DEFAULT_STRIKETHROUGH = false;
    private static final boolean DEFAULT_UNDERLINED = false;
    private static final boolean DEFAULT_ITALIC = false;
    private static final boolean DEFAULT_DISABLEPINGS = false;
    private static final boolean DEFAULT_STAFFCHAT = false;
    private static final boolean DEFAULT_ADMINCHAT = false;

    public static void checkAndInsertDefaultData(OfflinePlayer player) {
        try (Connection connection = DriverManager.getConnection(Database.DATABASE_URL)) {
            String selectQuery = "SELECT * FROM player_data WHERE uuid = ?;";
            try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {
                statement.setString(1, player.getUniqueId().toString());
                ResultSet resultSet = statement.executeQuery();

                if (!resultSet.next()) {
                    DatabaseManager.savePlayerData(player, DEFAULT_CHATCOLOR, DEFAULT_MAGIC, DEFAULT_BOLD, DEFAULT_STRIKETHROUGH, DEFAULT_UNDERLINED, DEFAULT_ITALIC, DEFAULT_DISABLEPINGS, DEFAULT_STAFFCHAT, DEFAULT_ADMINCHAT);
                }
            }
        } catch (SQLException e) {
            ChatSystem.getInstance().getLogger().log(Level.SEVERE, "Failed to load default data for" + player.getName() + "(UUID: " + player.getUniqueId() + ")", e);
        }
    }

    public static String getDefaultChatcolor() { return DEFAULT_CHATCOLOR; }

    public static PlayerData getDefaultPlayerdata() {
        return new PlayerData(DEFAULT_CHATCOLOR, DEFAULT_MAGIC, DEFAULT_BOLD, DEFAULT_STRIKETHROUGH, DEFAULT_UNDERLINED, DEFAULT_ITALIC, DEFAULT_DISABLEPINGS, DEFAULT_STAFFCHAT, DEFAULT_ADMINCHAT);
    }
}
