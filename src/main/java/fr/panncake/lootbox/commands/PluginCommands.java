package fr.panncake.lootbox.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import fr.panncake.lootbox.PannLootbox;
import fr.panncake.lootbox.managers.MessagesManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

public class PluginCommands {

    private static final PannLootbox plugin = PannLootbox.getInstance();

    public static LiteralCommandNode<CommandSourceStack> register() {
        return Commands.literal("lootbox")
                .executes(ctx -> {
                    MessagesManager.send(ctx, pluginInfo());

                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.literal("reload")
                        .requires(ctx -> ctx.getSender().hasPermission("pannlootbox.commands.reload"))
                        .executes(PluginCommands::executeReload)
                )
                .build();
    }

    private static int executeReload(final CommandContext<CommandSourceStack> ctx) {
        MessagesManager.sendLang(ctx, "reload.reloading", true);
        plugin.getConfigManager().reload();
        MessagesManager.sendLang(ctx, "reload.reloaded", true);

        if (ctx.getSource().getExecutor() instanceof Player player) {
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        }

        return Command.SINGLE_SUCCESS;
    }

    private static List<String> pluginInfo() {
        return List.of(
                "<gold>===== <green>PannLootbox Info <gold>=====",
                "<yellow>Name:</yellow> <white>" + plugin.getName(),
                "<yellow>Version:</yellow> <white>" + plugin.getPluginMeta().getVersion(),
                "<yellow>Author:</yellow> <white>" + String.join(", ", plugin.getPluginMeta().getAuthors()),
                "<yellow>Running on:</yellow> <white>" + plugin.getServer().getVersion(),
                "<gold>==============================="
        );
    }
}
