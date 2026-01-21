package fr.panncake.lootbox.managers;

import com.mojang.brigadier.context.CommandContext;
import fr.panncake.lootbox.PannLootbox;
import fr.panncake.lootbox.config.LanguageConfiguration;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

@SuppressWarnings({"UnstableApiUsage", "unused"})
public final class MessagesManager {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final LanguageConfiguration LANG = PannLootbox.getInstance().getLanguage();

    private MessagesManager() {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Audience audience;
        private String message;
        private List<String> messageList;
        private String defaultMessage;
        private List<String> defaultList;
        private boolean usePrefix = false;
        private Map<String, String> placeholders;

        public Builder to(Player player) {
            this.audience = player;
            return this;
        }

        public Builder to(Audience audience) {
            this.audience = audience;
            return this;
        }

        public Builder toMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder toConfig(String path) {
            this.message = LANG.get(path);
            return this;
        }

        public Builder toMessageList(List<String> messages) {
            this.messageList = messages;
            return this;
        }

        public Builder toConfigList(String path) {
            this.messageList = LANG.getList(path);
            return this;
        }

        public Builder defaults(String def) {
            this.defaultMessage = def;
            return this;
        }

        public Builder defaults(List<String> defList) {
            this.defaultList = defList;
            return this;
        }

        public Builder placeholders(Map<String, String> placeholders) {
            this.placeholders = placeholders;
            return this;
        }

        public Builder prefixed() {
            this.usePrefix = true;
            return this;
        }

        public void send() {
            if (audience == null) return;

            if (message != null) {
                sendMessage(audience, message, defaultMessage, placeholders, usePrefix);
            }

            if (messageList != null && !messageList.isEmpty()) {
                sendList(audience, messageList, defaultList, placeholders, usePrefix);
            }
        }

        private void sendMessage(Audience audience, String msg, String def, Map<String, String> placeholders, boolean prefix) {
            msg = (msg == null || msg.isBlank()) ? def : msg;
            if (msg == null || msg.isBlank()) return;

            hasPlaceholders(audience, placeholders, prefix, msg);
        }

        private void sendList(Audience audience, List<String> messages, List<String> def, Map<String, String> placeholders, boolean prefix) {
            if (messages == null || messages.isEmpty()) messages = def;
            if (messages == null || messages.isEmpty()) return;

            for (String msg : messages) {
                if (msg == null || msg.isBlank()) continue;

                hasPlaceholders(audience, placeholders, prefix, msg);
            }
        }

        private void hasPlaceholders(Audience audience, Map<String, String> placeholders, boolean prefix, String msg) {
            if (placeholders != null) {
                for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                    msg = msg.replace("{" + entry.getKey() + "}", entry.getValue());
                }
            }

            if (prefix) msg = MessagesManager.getPrefix() + msg;
            audience.sendMessage(MINI_MESSAGE.deserialize(msg));
        }
    }

    public static String getPrefix() {
        String rawPrefix = PannLootbox.getInstance()
                .getConfiguration()
                .get("plugin-prefix",
                        "<gradient:#54b5ff:#765eff><b>[PannLootbox] <dark_gray><b>»")
                .toString();

        if (!rawPrefix.isEmpty() && !rawPrefix.endsWith(" ")) {
            rawPrefix += " ";
        }

        return rawPrefix + "<reset>";
    }
}
