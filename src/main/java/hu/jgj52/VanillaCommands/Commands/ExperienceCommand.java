package hu.jgj52.VanillaCommands.Commands;

import dev.sulfurmc.Sulfur.Utils.SulfurCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;
import net.minestom.server.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExperienceCommand extends SulfurCommand {
    public ExperienceCommand() {
        super("experience", "xp");

        Component here = Component.translatable("command.context.here").color(NamedTextColor.RED).decoration(TextDecoration.UNDERLINED, false);

        setCondition((commandSender, _) -> !(commandSender instanceof Player player) || player.hasPermission("vanilla.command.experience"));

        setDefaultExecutor((sender, context) -> {
            sender.sendMessage(Component.translatable("command.unknown.command").color(NamedTextColor.RED));
            sender.sendMessage(Component.text(context.getInput()).color(NamedTextColor.GRAY).append(here));
        });

        ArgumentLiteral addArg = ArgumentType.Literal("add");
        ArgumentLiteral queryArg = ArgumentType.Literal("query");
        ArgumentLiteral setArg = ArgumentType.Literal("set");
        ArgumentEntity playerArg = ArgumentType.Entity("player").onlyPlayers(true);
        ArgumentEntity singleArg = ArgumentType.Entity("player").onlyPlayers(true).singleEntity(true);
        ArgumentNumber<Integer> amountArg = ArgumentType.Integer("amount").min(0);
        ArgumentLiteral pointsArg = ArgumentType.Literal("points");
        ArgumentLiteral levelsArg = ArgumentType.Literal("levels");

        addSyntax((sender, context) -> {
            List<Player> players = context.get(playerArg).find(sender).stream()
                    .filter(e -> e instanceof Player)
                    .map(e -> (Player) e)
                    .toList();

            int amount = context.get(amountArg);

            add(players, amount, false);

            int size = players.size();
            if (size > 1) {
                sender.sendMessage(Component.translatable(
                        "commands.experience.add.points.success.multiple",
                        Component.text(amount),
                        Component.text(size)
                ));
            } else {
                sender.sendMessage(Component.translatable(
                        "commands.experience.add.points.success.single",
                        Component.text(amount),
                        Component.text(players.getFirst().getUsername())
                ));
            }
        }, addArg, playerArg, amountArg);

        addSyntax((sender, context) -> {
            List<Player> players = context.get(playerArg).find(sender).stream()
                    .filter(e -> e instanceof Player)
                    .map(e -> (Player) e)
                    .toList();

            int amount = context.get(amountArg);

            add(players, amount, false);

            int size = players.size();
            if (size > 1) {
                sender.sendMessage(Component.translatable(
                        "commands.experience.add.points.success.multiple",
                        Component.text(amount),
                        Component.text(size)
                ));
            } else {
                sender.sendMessage(Component.translatable(
                        "commands.experience.add.points.success.single",
                        Component.text(amount),
                        Component.text(players.getFirst().getUsername())
                ));
            }
        }, addArg, playerArg, amountArg, pointsArg);

        addSyntax((sender, context) -> {
            List<Player> players = context.get(playerArg).find(sender).stream()
                    .filter(e -> e instanceof Player)
                    .map(e -> (Player) e)
                    .toList();

            int amount = context.get(amountArg);

            add(players, amount, true);

            int size = players.size();
            if (size > 1) {
                sender.sendMessage(Component.translatable(
                        "commands.experience.add.levels.success.multiple",
                        Component.text(amount),
                        Component.text(size)
                ));
            } else {
                sender.sendMessage(Component.translatable(
                        "commands.experience.add.levels.success.single",
                        Component.text(amount),
                        Component.text(players.getFirst().getUsername())
                ));
            }
        }, addArg, playerArg, amountArg, levelsArg);

        addSyntax((sender, context) -> {
            Player player = context.get(singleArg).findFirstPlayer(sender);
            if (player == null) return;

            sender.sendMessage(Component.translatable(
                    "commands.experience.query.points",
                    Component.text(player.getUsername()),
                    Component.text(player.getExp())
            ));
        }, queryArg, singleArg, pointsArg);

        addSyntax((sender, context) -> {
            Player player = context.get(singleArg).findFirstPlayer(sender);
            if (player == null) return;

            sender.sendMessage(Component.translatable(
                    "commands.experience.query.levels",
                    Component.text(player.getUsername()),
                    Component.text(player.getLevel())
            ));
        }, queryArg, singleArg, levelsArg);

        addSyntax((sender, context) -> {
            List<Player> players = new ArrayList<>(context.get(playerArg).find(sender).stream()
                    .filter(e -> e instanceof Player)
                    .map(e -> (Player) e)
                    .toList());

            int amount = context.get(amountArg);

            set(players, amount, false);

            if (players.isEmpty()) {
                sender.sendMessage(Component.translatable(
                        "commands.experience.set.points.invalid"
                ).color(NamedTextColor.RED));
                return;
            }

            int size = players.size();
            if (size > 1) {
                sender.sendMessage(Component.translatable(
                        "commands.experience.set.points.success.multiple",
                        Component.text(amount),
                        Component.text(size)
                ));
            } else {
                sender.sendMessage(Component.translatable(
                        "commands.experience.set.points.success.single",
                        Component.text(amount),
                        Component.text(players.getFirst().getUsername())
                ));
            }
        }, setArg, playerArg, amountArg);

        addSyntax((sender, context) -> {
            List<Player> players = new ArrayList<>(context.get(playerArg).find(sender).stream()
                    .filter(e -> e instanceof Player)
                    .map(e -> (Player) e)
                    .toList());

            int amount = context.get(amountArg);

            set(players, amount, false);

            if (players.isEmpty()) {
                sender.sendMessage(Component.translatable(
                        "commands.experience.set.points.invalid"
                ).color(NamedTextColor.RED));
                return;
            }

            int size = players.size();
            if (size > 1) {
                sender.sendMessage(Component.translatable(
                        "commands.experience.set.points.success.multiple",
                        Component.text(amount),
                        Component.text(size)
                ));
            } else {
                sender.sendMessage(Component.translatable(
                        "commands.experience.set.points.success.single",
                        Component.text(amount),
                        Component.text(players.getFirst().getUsername())
                ));
            }
        }, setArg, playerArg, amountArg, pointsArg);

        addSyntax((sender, context) -> {
            List<Player> players = context.get(playerArg).find(sender).stream()
                    .filter(e -> e instanceof Player)
                    .map(e -> (Player) e)
                    .toList();

            int amount = context.get(amountArg);

            set(players, amount, true);

            int size = players.size();
            if (size > 1) {
                sender.sendMessage(Component.translatable(
                        "commands.experience.set.levels.success.multiple",
                        Component.text(amount),
                        Component.text(size)
                ));
            } else {
                sender.sendMessage(Component.translatable(
                        "commands.experience.set.levels.success.single",
                        Component.text(amount),
                        Component.text(players.getFirst().getUsername())
                ));
            }
        }, setArg, playerArg, amountArg, levelsArg);
    }

    private void add(List<Player> players, int amount, boolean isLevel) {
        if (isLevel) {
            for (Player player : players) {
                int level = player.getLevel();
                player.setLevel(level + amount);
            }
        } else {
            for (Player player : players) {
                int level = player.getLevel();
                float progress = player.getExp();
                int pointsForLevel = pointsForLevel(level);
                int currentPoints = totalPointsForLevel(level) + Math.round(progress * pointsForLevel);
                int newTotal = currentPoints + amount;

                int newLevel = 0;
                while (totalPointsForLevel(newLevel + 1) <= newTotal) {
                    newLevel++;
                }
                int newProgress = newTotal - totalPointsForLevel(newLevel);

                player.setLevel(newLevel);
                player.setExp(newProgress / (float) pointsForLevel(newLevel));
            }
        }
    }

    private void set(List<Player> players, int amount, boolean isLevel) {
        if (isLevel) {
            for (Player player : players) {
                player.setLevel(amount);
            }
        } else {
            Iterator<Player> iterator = players.iterator();
            while (iterator.hasNext()) {
                Player player = iterator.next();
                if (amount >= pointsForLevel(player.getLevel())) {
                    iterator.remove();
                    continue;
                }
                player.setExp((float) amount / pointsForLevel(player.getLevel()));
            }
        }
    }

    private int pointsForLevel(int level) {
        if (level <= 15) return 2 * level + 7;
        if (level <= 30) return 5 * level - 38;
        return 9 * level - 158;
    }

    private int totalPointsForLevel(int level) {
        if (level <= 16) return level * level + 6 * level;
        if (level <= 31) return (int)(2.5 * level * level - 40.5 * level + 360);
        return (int)(4.5 * level * level - 162.5 * level + 2220);
    }
}
