package fr.panncake.lootbox.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import fr.panncake.lootbox.PannLootbox;
import fr.panncake.lootbox.managers.MessagesManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

import java.util.List;

public class PluginCommands {

    private static final PannLootbox plugin = PannLootbox.getInstance();

    public static LiteralCommandNode<CommandSourceStack> register() {
        return Commands.literal("lootbox")
                .executes(ctx -> {
                    MessagesManager.send(ctx, pluginInfo());
                    return Command.SINGLE_SUCCESS;
                })
                .then(ReloadCommand.register())
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
}
