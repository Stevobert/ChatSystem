package org.steve.chatsystem.database;

import org.steve.chatsystem.ChatSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public class Database {
    public static final String DATABASE_URL = "jdbc:sqlite:plugins/chatsystem/database.db";

    public static void initializeDatabase() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            if (connection != null) {
                Statement statement = connection.createStatement();
                String createTableQuery = "CREATE TABLE IF NOT EXISTS player_data (" +
                        "uuid TEXT PRIMARY KEY," +
                        "chatcolor TEXT," +
                        "magic BOOLEAN," +
                        "bold BOOLEAN," +
                        "strikethrough BOOLEAN," +
                        "underlined BOOLEAN," +
                        "italic BOOLEAN," +
                        "disablepings BOOLEAN," +
                        "staffchat BOOLEAN," +
                        "adminchat BOOLEAN" +
                        ");";
                statement.executeUpdate(createTableQuery);
            }
        } catch (SQLException e) {
            ChatSystem.getInstance().getLogger().log(Level.SEVERE, "Failed to initialize Database", e);
        }
    }
}
