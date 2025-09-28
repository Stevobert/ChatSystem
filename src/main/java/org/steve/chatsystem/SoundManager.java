package org.steve.chatsystem;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundManager {
    public static Sound getSound(String input) {
        Sound sound;

        sound = Registry.SOUNDS.get(NamespacedKey.minecraft(input.toLowerCase()));

        if (sound == null) {
            sound = Sound.ENTITY_ARROW_HIT_PLAYER;
        }

        return sound;
    }

    public static void playSound(Player player, String sound, float volume, float pitch) {
        playSound(player, SoundManager.getSound(sound), volume, pitch);
    }

    public static void playSound(Player player, Sound sound, float volume, float pitch) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }
}
