package org.steve.chatsystem.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.steve.chatsystem.ChatSystem;
import org.steve.chatsystem.database.DatabaseDefault;

public class JoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        DatabaseDefault.checkAndInsertDefaultData(player);
        player.removeMetadata("duplicated", ChatSystem.getInstance());
        player.removeMetadata("cooldown", ChatSystem.getInstance());
    }
}
