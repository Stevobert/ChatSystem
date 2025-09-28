package org.steve.chatsystem;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.steve.chatsystem.commands.*;
import org.steve.chatsystem.config.PluginConfig;
import org.steve.chatsystem.database.Database;
import org.steve.chatsystem.listener.ChatColorGUIListener;
import org.steve.chatsystem.listener.ChatListener;
import org.steve.chatsystem.listener.JoinListener;

import java.util.Objects;
import java.util.logging.Level;

public final class ChatSystem extends JavaPlugin {
    private static ChatSystem instance;

    @Override
    public void onEnable() {
        getLogger().info("ChatSystem started");
        instance = this;

        // Config
        boolean validateConfig = PluginConfig.getInstance().load();
        if (!validateConfig) {
            ChatSystem.getInstance().getLogger().log(Level.SEVERE, "The config is invalid! Using default config instead.");
        }

        // Database
        Database.initializeDatabase();

        // Listeners
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new ChatColorGUIListener(), this);

        // Commands
        command("chatsystem").setExecutor(new ChatSystemCommand());
        command("chatsystem").setExecutor(new ChatSystemCommand());
        command("staffchat").setExecutor(new Staffchat());
        command("adminchat").setExecutor(new Adminchat());
        command("clearchat").setExecutor(new ClearChat());
        command("mutechat").setExecutor(new MuteChat());
        command("togglepings").setExecutor(new Togglepings());
        command("chatcolor").setExecutor(new ChatColor());
    }

    @Override
    public void onDisable() {
        getLogger().info("ChatSystem stopped");
    }

    private PluginCommand command(String name) {
        return Objects.requireNonNull(getCommand(name), "Command " + name + " not found in plugin.yml!");
    }

    public static ChatSystem getInstance() {
        return instance;
    }
}
