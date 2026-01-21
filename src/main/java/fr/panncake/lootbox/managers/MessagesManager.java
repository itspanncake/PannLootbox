package fr.panncake.lootbox.managers;

import com.mojang.brigadier.context.CommandContext;
import fr.panncake.lootbox.PannLootbox;
import fr.panncake.lootbox.config.LanguageConfiguration;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

import java.util.List;

@SuppressWarnings({"UnstableApiUsage", "unused"})
public final class MessagesManager {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final LanguageConfiguration LANG = PannLootbox.getInstance().getLanguage();

    private MessagesManager() {}

    public static void send(Player player, String message) {
        send(player, message, false);
    }
    public static void send(Player player, String message, String def) { send(player, message, def, false); }
    public static void send(Player player, String message, boolean prefix) { send((Audience) player, message, prefix); }
    public static void send(Player player, String message, String def, boolean prefix) { send((Audience) player, message, def, prefix); }

    public static void send(CommandContext<CommandSourceStack> ctx, String message) { send(ctx.getSource().getExecutor(), message, false); }
    public static void send(CommandContext<CommandSourceStack> ctx, String message, String def) { send(ctx.getSource().getExecutor(), message, def, false); }
    public static void send(CommandContext<CommandSourceStack> ctx, String message, boolean prefix) { send(ctx.getSource().getExecutor(), message, prefix); }
    public static void send(CommandContext<CommandSourceStack> ctx, String message, String def, boolean prefix) { send(ctx.getSource().getExecutor(), message, def, prefix); }

    public static void sendLang(Player player, String key) { sendLang(player, key, false); }
    public static void sendLang(Player player, String key, String def) { sendLang(player, key, def, false); }
    public static void sendLang(Player player, String key, boolean prefix) {
        send(player, LANG.get(key), prefix);
    }
    public static void sendLang(Player player, String key, String def, boolean prefix) { send(player, LANG.get(key), def, prefix); }

    public static void sendLang(CommandContext<CommandSourceStack> ctx, String key) { sendLang(ctx, key, false); }
    public static void sendLang(CommandContext<CommandSourceStack> ctx, String key, String def) { sendLang(ctx, key, def, false); }
    public static void sendLang(CommandContext<CommandSourceStack> ctx, String key, boolean prefix) { send(ctx, LANG.get(key), prefix); }
    public static void sendLang(CommandContext<CommandSourceStack> ctx, String key, String def, boolean prefix) { send((Audience) ctx, LANG.get(key), def, prefix); }

    public static void sendList(Player player, List<String> messages) { send(player, messages, false); }
    public static void sendList(Player player, List<String> messages, List<String> def) { send(player, messages, def, false); }
    public static void sendList(Player player, List<String> messages, boolean prefix) { send(player, messages, prefix); }
    public static void sendList(Player player, List<String> messages, List<String> def, boolean prefix) { send(player, messages, def, prefix); }

    public static void sendList(CommandContext<CommandSourceStack> ctx, List<String> messages) { send(ctx.getSource().getExecutor(), messages, false); }
    public static void sendList(CommandContext<CommandSourceStack> ctx, List<String> messages, List<String> def) { send(ctx.getSource().getExecutor(), messages, def, false); }
    public static void sendList(CommandContext<CommandSourceStack> ctx, List<String> messages, boolean prefix) { send(ctx.getSource().getExecutor(), messages, prefix); }
    public static void sendList(CommandContext<CommandSourceStack> ctx, List<String> messages, List<String> def, boolean prefix) { send(ctx.getSource().getExecutor(), messages, def, prefix); }

    public static void sendLangList(Player player, String key) { sendLangList(player, key, false); }
    public static void sendLangList(Player player, String key, List<String> def) { sendLangList(player, key, def, false); }
    public static void sendLangList(Player player, String key, boolean prefix) { send(player, LANG.getList(key), prefix); }
    public static void sendLangList(Player player, String key, List<String> def, boolean prefix) { send(player, LANG.getList(key), def, prefix); }

    public static void sendLangList(CommandContext<CommandSourceStack> ctx, String key) { sendLangList(ctx, key, false); }
    public static void sendLangList(CommandContext<CommandSourceStack> ctx, String key, List<String> def) { sendLangList(ctx, key, false); }
    public static void sendLangList(CommandContext<CommandSourceStack> ctx, String key, boolean prefix) { send(ctx.getSource().getExecutor(), LANG.getList(key), prefix); }
    public static void sendLangList(CommandContext<CommandSourceStack> ctx, String key, List<String> def, boolean prefix) { send(ctx.getSource().getExecutor(), LANG.getList(key), prefix); }

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

    private static void send(Audience audience, String message, boolean prefix) {
        if (audience == null || message == null || message.isBlank()) return;
        if (prefix) message = getPrefix() + message;
        audience.sendMessage(MINI_MESSAGE.deserialize(message));
    }

    private static void send(Audience audience, String message, String def, boolean prefix) {
        if (audience == null) return;
        if (message == null || message.isBlank()) message = def;
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
    private static void send(Audience audience, List<String> messages, List<String> def, boolean prefix) {
        if (audience == null) return;
        if (messages == null || messages.isEmpty()) messages = def;

        messages.stream()
                .filter(m -> m != null && !m.isBlank())
                .map(m -> prefix ? getPrefix() + m : m)
                .map(MINI_MESSAGE::deserialize)
                .forEach(audience::sendMessage);
    }

}
