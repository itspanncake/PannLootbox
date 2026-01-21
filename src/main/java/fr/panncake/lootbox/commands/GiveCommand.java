package fr.panncake.lootbox.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.panncake.lootbox.PannLootbox;
import fr.panncake.lootbox.managers.LootboxManager;
import fr.panncake.lootbox.managers.MessagesManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
                            for (Object name : LootboxManager.lootbox().getRoot().getKeys()) {
                                builder.suggest(name.toString());
                            }
                            return builder.buildFuture();
                        })
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                String lootboxId = ctx.getArgument("lootbox", String.class);
                                if (LootboxManager.lootbox().contains(lootboxId)) {
                                    ItemStack lootbox = LootboxManager.createLootbox(lootboxId);
                                    if (lootbox != null) {
                                        player.getInventory().addItem(lootbox);
                                    }
                                } else {
                                    MessagesManager.builder()
                                            .to(ctx.getSource().getExecutor())
                                            .toConfig("commands.lootbox.unknownLootbox")
                                            .defaults("<red>The lootbox <white>{LOOTBOX}</white>does not exist!")
                                            .placeholders(Map.of("LOOTBOX", lootboxId))
                                            .prefixed()
                                            .send();
                                }
                            } else {
                                MessagesManager.builder()
                                        .to(ctx.getSource().getExecutor())
                                        .toConfig("commands.noConsole")
                                        .defaults("<red>Only a player can do this!")
                                        .send();
                            }

                            return Command.SINGLE_SUCCESS;
                        })
                );
    }
}
