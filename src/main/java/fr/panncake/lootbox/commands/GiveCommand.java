package fr.panncake.lootbox.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.panncake.lootbox.PannLootbox;
import fr.panncake.lootbox.lootbox.LootboxBuilder;
import fr.panncake.lootbox.managers.MessagesManager;
import fr.panncake.lootbox.utils.PlaceholderUtils;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
public class GiveCommand {

    private static final PannLootbox plugin = PannLootbox.getInstance();

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("give")
                .requires(ctx -> ctx.getSender().hasPermission("pannlootbox.commands.give"))
                .executes(PluginCommands::incorrectUsage)
                .then(Commands.argument("lootbox", StringArgumentType.word())
                        .suggests((context, builder) -> {
                            for (Object name : plugin.getLootboxManager().getLootboxes().keySet()) {
                                builder.suggest(name.toString());
                            }
                            return builder.buildFuture();
                        })
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                String lootboxId = ctx.getArgument("lootbox", String.class);
                                if (plugin.getLootboxManager().getLootboxes().containsKey(lootboxId)) {
                                    player.getInventory().addItem(LootboxBuilder.createItem(plugin.getLootboxManager().getLootboxes().get(lootboxId)));
                                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                                    MessagesManager.builder()
                                            .to(player)
                                            .toConfig("commands.give.given")
                                            .defaults("<green>You have successfully received the <white>{LOOTBOX}</white> lootbox!")
                                            .placeholders(PlaceholderUtils.lootboxPlaceholders(plugin.getLootboxManager().getLootboxes().get(lootboxId)))
                                            .prefixed()
                                            .send();
                                } else {
                                    MessagesManager.builder()
                                            .to(ctx.getSource().getExecutor())
                                            .toConfig("commands.give.unknownLootbox")
                                            .defaults("<red>The lootbox <white>{LOOTBOX}</white>does not exist!")
                                            .placeholders(Map.of("LOOTBOX", lootboxId))
                                            .prefixed()
                                            .send();
                                }
                            } else {
                                MessagesManager.builder()
                                        .to(ctx.getSource().getExecutor())
                                        .toConfig("commands.errors.noConsole")
                                        .defaults("<red>Only a player can do this!")
                                        .send();
                            }

                            return Command.SINGLE_SUCCESS;
                        })
                );
    }
}
