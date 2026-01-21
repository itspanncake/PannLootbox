package fr.panncake.lootbox.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.panncake.lootbox.PannLootbox;
import fr.panncake.lootbox.managers.MessagesManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.io.IOException;

@SuppressWarnings("UnstableApiUsage")
public class ReloadCommand {

    private static final PannLootbox plugin = PannLootbox.getInstance();

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("reload")
                .requires(ctx -> ctx.getSender().hasPermission("pannlootbox.commands.reload"))
                .executes(ctx -> {
                    MessagesManager.builder()
                            .to(ctx.getSource().getExecutor())
                            .toConfig("commands.reload.reloading")
                            .defaults("<gray>Reloading plugin configurations...")
                            .prefixed()
                            .send();
                    plugin.getPluginLogger().info("PannLootbox plugin is now reloading...");

                    try {
                        PannLootbox.getInstance().getConfiguration().getYaml().reload();
                        plugin.getLanguageManager().reload();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    plugin.getLootboxManager().reload();

                    plugin.getPluginLogger().info("PannLootbox plugin reloaded successfully!");
                    MessagesManager.builder()
                            .to(ctx.getSource().getExecutor())
                            .toConfig("commands.reload.reloaded")
                            .defaults("<green>Plugin reloaded successfully!")
                            .prefixed()
                            .send();

                    if (ctx.getSource().getExecutor() instanceof Player player) {
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                    }

                    return Command.SINGLE_SUCCESS;
                });
    }
}
