package fr.panncake.lootbox.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import fr.panncake.lootbox.PannLootbox;
import fr.panncake.lootbox.managers.MessagesManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class PluginCommands {

    private static final PannLootbox plugin = PannLootbox.getInstance();

    public static LiteralCommandNode<CommandSourceStack> register() {
        return Commands.literal("lootbox")
                .executes(ctx -> {
                    MessagesManager.builder()
                            .to(ctx.getSource().getExecutor())
                            .toMessageList(pluginInfo())
                            .send();
                    return Command.SINGLE_SUCCESS;
                })
                .then(ReloadCommand.register())
                .then(GiveCommand.register())
                .build();
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

    public static int incorrectUsage(final CommandContext<CommandSourceStack> ctx) {
        MessagesManager.builder()
                .to(ctx.getSource().getExecutor())
                .toConfig("commands.errors.incorrectUsage")
                .defaults("<red>Incorrect command usage! Please try with <white>/lootbox help</white>!")
                .send();
        return Command.SINGLE_SUCCESS;
    }
}
