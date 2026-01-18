package fr.panncake.lootbox.managers;

import com.mojang.brigadier.context.CommandContext;
import fr.panncake.lootbox.PannLootbox;
import fr.panncake.lootbox.config.LangProvider;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

import java.util.List;

public final class MessagesManager {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final LangProvider LANG = new LangProvider(
            PannLootbox.getInstance().getConfigManager().getLang()
    );

    private MessagesManager() {}

    /* =====================
       Single messages
       ===================== */

    public static void send(Player player, String message) {
        send(player, message, false);
    }

    public static void send(CommandContext<CommandSourceStack> ctx, String message) {
        send(ctx.getSource().getExecutor(), message, false);
    }

    public static void send(Player player, String message, boolean prefix) {
        send((Audience) player, message, prefix);
    }

    public static void send(CommandContext<CommandSourceStack> ctx, String message, boolean prefix) {
        send(ctx.getSource().getExecutor(), message, prefix);
    }

    public static void sendLang(Player player, String key) {
        sendLang(player, key, false);
    }

    public static void sendLang(CommandContext<CommandSourceStack> ctx, String key) {
        sendLang(ctx, key, false);
    }

    public static void sendLang(Player player, String key, boolean prefix) {
        send(player, LANG.get(key), prefix);
    }

    public static void sendLang(CommandContext<CommandSourceStack> ctx, String key, boolean prefix) {
        send(ctx, LANG.get(key), prefix);
    }

    /* =====================
       List messages
       ===================== */

    public static void send(Player player, List<String> messages) {
        send(player, messages, false);
    }

    public static void send(CommandContext<CommandSourceStack> ctx, List<String> messages) {
        send(ctx.getSource().getExecutor(), messages, false);
    }

    public static void send(Player player, List<String> messages, boolean prefix) {
        send((Audience) player, messages, prefix);
    }

    public static void send(CommandContext<CommandSourceStack> ctx, List<String> messages, boolean prefix) {
        send(ctx.getSource().getExecutor(), messages, prefix);
    }

    public static void sendLangList(Player player, String key) {
        sendLangList(player, key, false);
    }

    public static void sendLangList(CommandContext<CommandSourceStack> ctx, String key) {
        sendLangList(ctx, key, false);
    }

    public static void sendLangList(Player player, String key, boolean prefix) {
        send(player, LANG.getList(key), prefix);
    }

    public static void sendLangList(CommandContext<CommandSourceStack> ctx, String key, boolean prefix) {
        send(ctx.getSource().getExecutor(), LANG.getList(key), prefix);
    }

    /* =====================
       Internal helpers
       ===================== */

    public static String getPrefix() {
        String rawPrefix = PannLootbox.getInstance()
                .getConfigManager()
                .getConfig()
                .getString("plugin-prefix", "");

        if (!rawPrefix.isEmpty() && !rawPrefix.endsWith(" ")) {
            rawPrefix += " ";
        }

        return rawPrefix + "<reset>";
    }

    private static void send(Audience audience, String message, boolean prefix) {
        if (audience == null || message == null || message.isBlank()) return;
        if (prefix) message = getPrefix() + message;
        audience.sendMessage(MINI_MESSAGE.deserialize(message));
    }

    private static void send(Audience audience, List<String> messages, boolean prefix) {
        if (audience == null || messages == null || messages.isEmpty()) return;

        messages.stream()
                .filter(m -> m != null && !m.isBlank())
                .map(m -> prefix ? getPrefix() + m : m)
                .map(MINI_MESSAGE::deserialize)
                .forEach(audience::sendMessage);
    }
}
