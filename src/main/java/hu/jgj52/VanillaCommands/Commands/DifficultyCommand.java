package hu.jgj52.VanillaCommands.Commands;

import dev.sulfurmc.Sulfur.Permissions.User;
import dev.sulfurmc.Sulfur.Utils.SulfurCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import net.minestom.server.world.Difficulty;

public class DifficultyCommand extends SulfurCommand {
    public DifficultyCommand() {
        super("difficulty");

        Component here = Component.translatable("command.context.here").color(NamedTextColor.RED).decoration(TextDecoration.UNDERLINED, false);

        setCondition((commandSender, _) -> !(commandSender instanceof Player player) || player.hasPermission("vanilla.command.difficulty"));

        setDefaultExecutor((sender, context) -> {
            sender.sendMessage(Component.translatable("command.unknown.command").color(NamedTextColor.RED));
            sender.sendMessage(Component.text(context.getInput()).color(NamedTextColor.GRAY).append(here));
        });

        enum difficulties {
            easy,
            hard,
            normal,
            peaceful
        }
        ArgumentEnum<difficulties> arg = ArgumentType.Enum("difficulty", difficulties.class);

        addSyntax((sender, _) -> {
            String diff = MinecraftServer.getDifficulty().name();
            sender.sendMessage(Component.translatable(
                    "commands.difficulty.query",
                    Component.translatable("options.difficulty." + diff.toLowerCase())
            ));
        });

        addSyntax((sender, context) -> {
            difficulties diff = context.get(arg);
            Difficulty difficulty = Difficulty.valueOf(diff.name().toUpperCase());
            Difficulty before = MinecraftServer.getDifficulty();
            if (difficulty == before) {
                sender.sendMessage(Component.translatable(
                        "commands.difficulty.failure",
                        Component.translatable("options.difficulty." + diff.name())
                ).color(NamedTextColor.RED));
                return;
            }
            MinecraftServer.setDifficulty(difficulty);
            sender.sendMessage(Component.translatable(
                    "commands.difficulty.success",
                    Component.translatable("options.difficulty." + diff.name())
            ));
        }, arg);
    }
}
