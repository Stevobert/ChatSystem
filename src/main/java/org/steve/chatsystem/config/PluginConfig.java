package org.steve.chatsystem.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.steve.chatsystem.ChatSystem;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Level;

public class PluginConfig {
    private final static PluginConfig instance = new PluginConfig();

    private File file;
    private static YamlConfiguration config;

    public static float version;
    public static String chatformatPrefix;
    public static String chatformatSuffix;
    public static String chatformatArrow;
    public static boolean chatformatEnableCustom;
    public static String chatformatCustom;
    public static String staffchatPrefix;
    public static String adminchatPrefix;
    public static String soundsPing;
    public static float soundsPingVolume;
    public static float soundsPingPitch;
    public static String soundsClickSuccess;
    public static float soundsClickSuccessVolume;
    public static float soundsClickSuccessPitch;
    public static String soundsClickFail;
    public static float soundsClickFailVolume;
    public static float soundsClickFailPitch;
    public static String defaultChatcolor;
    public static boolean defaultMagic;
    public static boolean defaultBold;
    public static boolean defaultStrikethrough;
    public static boolean defaultUnderlined;
    public static boolean defaultItalic;
    public static boolean enableChatCooldown;
    public static int cooldownTime;
    public static boolean enableNoDuplicateMessages;
    public static boolean enableStyleInfo;
    public static boolean enableColorInfo;
    public static boolean pingEnableCustom;
    public static String pingCustom;
    public static boolean everyonePingEnableCustom;
    public static String everyonePingReplacePhrase;
    public static String everyonePingReplacement;
    public static boolean staffchatEnableCustom;
    public static String staffchatCustom;
    public static boolean adminchatEnableCustom;
    public static String adminchatCustom;
    public static boolean enableChatcolorCommandTargetFeedback;
    public static String messagesConsoleCommandError;
    public static String messagesAdminchatEnable;
    public static String messagesAdminchatDisable;
    public static String messagesStaffchatEnable;
    public static String messagesStaffchatDisable;
    public static String messagesMutechatMute;
    public static String messagesMutechatUnmute;
    public static String messagesClearchatClearchat;
    public static String messagesTogglepingsEnable;
    public static String messagesTogglepingsDisable;
    public static String messagesChatSendErrorsMessageCooldown;
    public static String messagesChatSendErrorsDuplicatedMessage;
    public static String messagesChatSendErrorsChatIsMuted;
    public static String messagesChatcolorCommandUpdateChatcolorSender;
    public static String messagesChatcolorCommandUpdateChatcolorTarget;

    PluginConfig() {
    }

    public boolean load() {
        file = new File(ChatSystem.getInstance().getDataFolder(), "config.yml");

        if (!file.exists()) {
            ChatSystem.getInstance().saveResource("config.yml", false);
        }

        config = new YamlConfiguration();
        config.options().parseComments(true);

        try {
            config.load(file);
        } catch (Exception e) {
            ChatSystem.getInstance().getLogger().log(Level.SEVERE, "Failed to load config", e);
        }

        boolean valid = validate();

        if (!valid) {
            InputStream defaultConfig = ChatSystem.getInstance().getResource("config.yml");
            if (defaultConfig == null) {
                ChatSystem.getInstance().getLogger().log(Level.SEVERE, "Failed to load default config from jar! Your jar file is missing the default config!");
                return false;
            }
            try {
                config.load(new InputStreamReader(defaultConfig));
            } catch (Exception e) {
                ChatSystem.getInstance().getLogger().log(Level.SEVERE, "Failed to default load config", e);
            }
        }

        version = (float) config.getDouble("version");
        chatformatPrefix = config.getString("chat-format.prefix");
        chatformatSuffix = config.getString("chat-format.suffix");
        chatformatArrow = config.getString("chat-format.arrow");
        chatformatEnableCustom = config.getBoolean("chat-format.enable-custom");
        chatformatCustom = config.getString("chat-format.custom");
        staffchatPrefix = config.getString("staff-chat.prefix");
        adminchatPrefix = config.getString("admin-chat.prefix");
        soundsPing = config.getString("sounds.ping.sound");
        soundsPingVolume = (float) config.getDouble("sounds.ping.volume");
        soundsPingPitch = (float) config.getDouble("sounds.ping.pitch");
        soundsClickSuccess = config.getString("sounds.click-success.sound");
        soundsClickSuccessVolume = (float) config.getDouble("sounds.click-success.volume");
        soundsClickSuccessPitch = (float) config.getDouble("sounds.click-success.pitch");
        soundsClickFail = config.getString("sounds.click-fail.sound");
        soundsClickFailVolume = (float) config.getDouble("sounds.click-fail.volume");
        soundsClickFailPitch = (float) config.getDouble("sounds.click-fail.pitch");
        defaultChatcolor = config.getString("default.default-color");
        defaultMagic = config.getBoolean("default.default-magic");
        defaultBold = config.getBoolean("default.default-bold");
        defaultStrikethrough = config.getBoolean("default.default-strikethrough");
        defaultUnderlined = config.getBoolean("default.default-underlined");
        defaultItalic = config.getBoolean("default.default-italic");
        enableChatCooldown = config.getBoolean("options.cooldown.enable-chat-cooldown");
        cooldownTime = config.getInt("options.cooldown.cooldown-time");
        enableNoDuplicateMessages = config.getBoolean("options.enable-no-duplicate-messages");
        enableStyleInfo = config.getBoolean("preview.enable-style");
        enableColorInfo = config.getBoolean("preview.enable-color");
        pingEnableCustom = config.getBoolean("ping.normal.enable-custom");
        pingCustom = config.getString("ping.normal.custom");
        everyonePingEnableCustom = config.getBoolean("ping.everyone.enable-custom");
        everyonePingReplacePhrase = config.getString("ping.everyone.replace-phrase");
        everyonePingReplacement = config.getString("ping.everyone.replacement");
        staffchatEnableCustom = config.getBoolean("staff-chat.enable-custom");
        staffchatCustom = config.getString("staff-chat.custom");
        adminchatEnableCustom = config.getBoolean("admin-chat.enable-custom");
        adminchatCustom = config.getString("admin-chat.custom");
        enableChatcolorCommandTargetFeedback = config.getBoolean("options.enable-chatcolor-command-target-feedback");
        messagesConsoleCommandError = config.getString("messages.console-command-error");
        messagesAdminchatEnable = config.getString("messages.admin-chat.enable");
        messagesAdminchatDisable = config.getString("messages.admin-chat.disable");
        messagesStaffchatEnable = config.getString("messages.staff-chat.enable");
        messagesStaffchatDisable = config.getString("messages.staff-chat.disable");
        messagesMutechatMute = config.getString("messages.mute-chat.mute");
        messagesMutechatUnmute = config.getString("messages.mute-chat.unmute");
        messagesClearchatClearchat = config.getString("messages.clear-chat.clear-chat");
        messagesTogglepingsEnable = config.getString("messages.toggle-pings.enable");
        messagesTogglepingsDisable = config.getString("messages.toggle-pings.disable");
        messagesChatSendErrorsMessageCooldown = config.getString("messages.chat-send-errors.message-cooldown");
        messagesChatSendErrorsDuplicatedMessage = config.getString("messages.chat-send-errors.duplicated-message");
        messagesChatSendErrorsChatIsMuted = config.getString("messages.chat-send-errors.chat-is-muted");
        messagesChatcolorCommandUpdateChatcolorSender = config.getString("messages.chatcolor-command.update-chatcolor-sender");
        messagesChatcolorCommandUpdateChatcolorTarget = config.getString("messages.chatcolor-command.update-chatcolor-target");
        return valid;
    }

    public static boolean validate() {
        boolean valid = true;

        HashMap<String, Class<?>> map = new HashMap<>();
        map.put("version", Double.class);
        map.put("chat-format.prefix", String.class);
        map.put("chat-format.suffix", String.class);
        map.put("chat-format.arrow", String.class);
        map.put("chat-format.enable-custom", Boolean.class);
        map.put("chat-format.custom", String.class);
        map.put("staff-chat.prefix", String.class);
        map.put("admin-chat.prefix", String.class);
        map.put("sounds.ping.sound", String.class);
        map.put("sounds.ping.volume", Double.class);
        map.put("sounds.ping.pitch", Double.class);
        map.put("sounds.click-success.sound", String.class);
        map.put("sounds.click-success.volume", Double.class);
        map.put("sounds.click-success.pitch", Double.class);
        map.put("sounds.click-fail.sound", String.class);
        map.put("sounds.click-fail.volume", Double.class);
        map.put("sounds.click-fail.pitch", Double.class);
        map.put("default.default-color", String.class);
        map.put("default.default-magic", Boolean.class);
        map.put("default.default-bold", Boolean.class);
        map.put("default.default-strikethrough", Boolean.class);
        map.put("default.default-underlined", Boolean.class);
        map.put("default.default-italic", Boolean.class);
        map.put("options.cooldown.enable-chat-cooldown", Boolean.class);
        map.put("options.cooldown.cooldown-time", Integer.class);
        map.put("options.enable-no-duplicate-messages", Boolean.class);
        map.put("preview.enable-style", Boolean.class);
        map.put("preview.enable-color", Boolean.class);
        map.put("ping.normal.enable-custom", Boolean.class);
        map.put("ping.normal.custom", String.class);
        map.put("ping.everyone.enable-custom", Boolean.class);
        map.put("ping.everyone.replace-phrase", String.class);
        map.put("ping.everyone.replacement", String.class);
        map.put("staff-chat.enable-custom", Boolean.class);
        map.put("staff-chat.custom", String.class);
        map.put("admin-chat.enable-custom", Boolean.class);
        map.put("admin-chat.custom", String.class);
        map.put("options.enable-chatcolor-command-target-feedback", Boolean.class);
        map.put("messages.console-command-error", String.class);
        map.put("messages.admin-chat.enable", String.class);
        map.put("messages.admin-chat.disable", String.class);
        map.put("messages.staff-chat.enable", String.class);
        map.put("messages.staff-chat.disable", String.class);
        map.put("messages.mute-chat.mute", String.class);
        map.put("messages.mute-chat.unmute", String.class);
        map.put("messages.clear-chat.clear-chat", String.class);
        map.put("messages.toggle-pings.enable", String.class);
        map.put("messages.toggle-pings.disable", String.class);
        map.put("messages.chat-send-errors.message-cooldown", String.class);
        map.put("messages.chat-send-errors.duplicated-message", String.class);
        map.put("messages.chat-send-errors.chat-is-muted", String.class);
        map.put("messages.chatcolor-command.update-chatcolor-sender", String.class);
        map.put("messages.chatcolor-command.update-chatcolor-target", String.class);

        for (String path : map.keySet()) {
            Class<?> expected = map.get(path);
            boolean validEntry = false;
            if (expected.equals(String.class)) {
                validEntry = config.isString(path);
            } else if (expected.equals(Boolean.class)) {
                validEntry = config.isBoolean(path);
            } else if (expected.equals(Double.class)) {
                validEntry = config.isDouble(path);
            } else if (expected.equals(Integer.class)) {
                validEntry = config.isInt(path);
            }

            if (!validEntry) {
                ChatSystem.getInstance().getLogger().log(Level.SEVERE, "Failed to load config at: " + path);
                valid = false;
            }
        }

        return valid;
    }

/*
 Code to live edit the config
    public void save() {
        try {
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void set(String path, Object value) {
        config.set(path, value);
        save();
    }

    public void setChatformatPrefix(String prefix) {
        this.chatformatPrefix = prefix;
        set("chatcolor.prefix", prefix);
    }
*/

    public static PluginConfig getInstance() {
        return instance;
    }
}
