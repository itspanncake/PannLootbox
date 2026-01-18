package fr.panncake.lootbox.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.panncake.lootbox.PannLootbox;
import fr.panncake.lootbox.managers.MessagesManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ReloadCommand {

    private static final PannLootbox plugin = PannLootbox.getInstance();

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("reload")
                .requires(ctx -> ctx.getSender().hasPermission("pannlootbox.commands.reload"))
                .executes(ctx -> {
                    MessagesManager.sendLang(ctx, "reload.reloading", true);
                    plugin.logger.info("PannLootbox plugin is now reloading...");

                    PannLootbox.getInstance().getConfigManager().reload();

                    plugin.logger.info("PannLootbox plugin reloaded successfully!");
                    MessagesManager.sendLang(ctx, "reload.reloaded", true);

                    if (ctx.getSource().getExecutor() instanceof Player player) {
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                    }

                    return Command.SINGLE_SUCCESS;
                });
    }
}
