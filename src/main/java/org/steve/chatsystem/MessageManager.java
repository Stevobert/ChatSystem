package org.steve.chatsystem;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;

public class MessageManager {
    public static void send(CommandSender sender, String message) {
        send(sender, message, "&f");
    }

    public static void send(CommandSender sender, Component component) {
        sender.sendMessage(Color.colored("&f").append(component));
    }

    public static void send(CommandSender sender, String message, String prefix) {
        sender.sendMessage(Color.colored(prefix + message));
    }

    public static void broadcastMessage(Component message) {
        broadcastMessage(message, true);
    }

    public static void broadcastMessage(Component message, Boolean console) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            send(p, message);
        }
        if (console) {
            send(Bukkit.getConsoleSender(), message);
        }
    }

    public static void sendTitle(Player player, String title, String subtitle) {
        Component compTitle = Color.colored(title);
        Component compSubtitle = Color.colored(subtitle);
        Title fullTitle = Title.title(
                compTitle,
                compSubtitle,
                Title.Times.times(Duration.ofMillis(100), Duration.ofSeconds(2), Duration.ofMillis(500))
        );
        player.showTitle(fullTitle);
    }
}
