package hu.jgj52.VanillaCommands.Commands;

import dev.sulfurmc.Sulfur.Sulfur;
import dev.sulfurmc.Sulfur.Utils.SulfurCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentLong;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public class TimeCommand extends SulfurCommand {
    public TimeCommand() {
        super("time");

        Component here = Component.translatable("command.context.here").color(NamedTextColor.RED).decoration(TextDecoration.UNDERLINED, false);

        setCondition((commandSender, _) -> !(commandSender instanceof Player player) || player.hasPermission("vanilla.command.time"));

        setDefaultExecutor((sender, context) -> {
            sender.sendMessage(Component.translatable("command.unknown.command").color(NamedTextColor.RED));
            sender.sendMessage(Component.text(context.getInput()).color(NamedTextColor.GRAY).append(here));
        });

        ArgumentLiteral addArg = ArgumentType.Literal("add");
        ArgumentLiteral queryArg = ArgumentType.Literal("query");
        ArgumentLiteral setArg = ArgumentType.Literal("set");
        ArgumentLiteral dayArg = ArgumentType.Literal("day");
        ArgumentLiteral noonArg = ArgumentType.Literal("noon");
        ArgumentLiteral nightArg = ArgumentType.Literal("night");
        ArgumentLiteral midnightArg = ArgumentType.Literal("midnight");
        ArgumentLiteral daytimeArg = ArgumentType.Literal("daytime");
        ArgumentLong timeArg = ArgumentType.Long("time");

        addSyntax((sender, context) -> {
            long add = context.get(timeArg);
            Instance instance;
            if (sender instanceof Entity entity) {
                instance = entity.getInstance();
            } else {
                instance = Sulfur.ic;
            }
            instance.setTime(instance.getTime() + add);
            sender.sendMessage(Component.translatable(
                    "commands.time.set",
                    Component.text(instance.getTime())
            ));
        }, addArg, timeArg);

        addSyntax((sender, _) -> {
            Instance instance;
            if (sender instanceof Entity entity) {
                instance = entity.getInstance();
            } else {
                instance = Sulfur.ic;
            }
            sender.sendMessage(Component.translatable(
                    "commands.time.query",
                    Component.text(instance.getTime() / 24000)
            ));
        }, queryArg, dayArg);

        addSyntax((sender, _) -> {
            Instance instance;
            if (sender instanceof Entity entity) {
                instance = entity.getInstance();
            } else {
                instance = Sulfur.ic;
            }
            sender.sendMessage(Component.translatable(
                    "commands.time.query",
                    Component.text(instance.getTime() % 24000)
            ));
        }, queryArg, daytimeArg);

        addSyntax((sender, _) -> {
            Instance instance;
            if (sender instanceof Entity entity) {
                instance = entity.getInstance();
            } else {
                instance = Sulfur.ic;
            }
            long time = 1000;
            instance.setTime(time);
            sender.sendMessage(Component.translatable(
                    "commands.time.set",
                    Component.text(time)
            ));
        }, setArg, dayArg);

        addSyntax((sender, _) -> {
            Instance instance;
            if (sender instanceof Entity entity) {
                instance = entity.getInstance();
            } else {
                instance = Sulfur.ic;
            }
            long time = 6000;
            instance.setTime(time);
            sender.sendMessage(Component.translatable(
                    "commands.time.set",
                    Component.text(time)
            ));
        }, setArg, noonArg);

        addSyntax((sender, _) -> {
            Instance instance;
            if (sender instanceof Entity entity) {
                instance = entity.getInstance();
            } else {
                instance = Sulfur.ic;
            }
            long time = 13000;
            instance.setTime(time);
            sender.sendMessage(Component.translatable(
                    "commands.time.set",
                    Component.text(time)
            ));
        }, setArg, nightArg);

        addSyntax((sender, _) -> {
            Instance instance;
            if (sender instanceof Entity entity) {
                instance = entity.getInstance();
            } else {
                instance = Sulfur.ic;
            }
            long time = 18000;
            instance.setTime(time);
            sender.sendMessage(Component.translatable(
                    "commands.time.set",
                    Component.text(time)
            ));
        }, setArg, midnightArg);

        addSyntax((sender, context) -> {
            Instance instance;
            if (sender instanceof Entity entity) {
                instance = entity.getInstance();
            } else {
                instance = Sulfur.ic;
            }
            long time = context.get(timeArg);
            instance.setTime(time);
            sender.sendMessage(Component.translatable(
                    "commands.time.set",
                    Component.text(time)
            ));
        }, setArg, timeArg);
    }
}
