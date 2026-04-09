package hu.jgj52.VanillaCommands.Commands;

import hu.jgj52.SulfurPermissions.Utils.PermissionManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentItemStack;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GiveCommand extends Command {
    public GiveCommand() {
        super("give");

        Component here = Component.translatable("command.context.here").color(NamedTextColor.RED).decoration(TextDecoration.UNDERLINED, false);

        setCondition((commandSender, _) -> !(commandSender instanceof Player player) || new PermissionManager(player, "vanilla.command").has("give"));

        setDefaultExecutor((sender, context) -> {
            sender.sendMessage(Component.translatable("command.unknown.command").color(NamedTextColor.RED));
            sender.sendMessage(Component.text(context.getInput()).color(NamedTextColor.GRAY).append(here));
        });

        ArgumentEntity playerArg = ArgumentType.Entity("players").onlyPlayers(true);
        ArgumentItemStack itemArg = ArgumentType.ItemStack("item");
        ArgumentInteger amountArg = ArgumentType.Integer("amount");

        addSyntax((sender, context) -> {
            List<Player> players = context.get(playerArg).find(sender).stream()
                    .filter(Player.class::isInstance)
                    .map(Player.class::cast)
                    .toList();
            if (players.isEmpty()) {
                sender.sendMessage(Component.translatable("argument.entity.notfound.player").color(NamedTextColor.RED));
                return;
            }
            int size = players.size();
            ItemStack item = context.get(itemArg);

            if (size > 1) {
                for (Player player : players) {
                    player.getInventory().addItemStack(item);
                }
                sender.sendMessage(Component.translatable(
                        "commands.give.success.multiple",
                        Component.text(item.amount()),
                        Component.text("[").append(Component.translatable(item.material().registry().translationKey())).append(Component.text("]")),
                        Component.text(size)
                ));
            } else {
                Player player = players.getFirst();

                player.getInventory().addItemStack(item);
                sender.sendMessage(Component.translatable(
                        "commands.give.success.single",
                        Component.text(item.amount()),
                        Component.text("[").append(Component.translatable(item.material().registry().translationKey())).append(Component.text("]")),
                        Component.text(player.getUsername())
                ));
            }
        }, playerArg, itemArg);

        addSyntax((sender, context) -> {
            List<Player> players = context.get(playerArg).find(sender).stream()
                    .filter(Player.class::isInstance)
                    .map(Player.class::cast)
                    .toList();
            if (players.isEmpty()) {
                sender.sendMessage(Component.translatable("argument.entity.notfound.player").color(NamedTextColor.RED));
                return;
            }
            int size = players.size();
            ItemStack item = context.get(itemArg);
            int amount = context.get(amountArg);
            int maxAmount = item.maxStackSize() * 100;

            if (amount > maxAmount) {
                sender.sendMessage(Component.translatable(
                        "commands.give.failed.toomanyitems",
                        Component.text(maxAmount),
                        Component.text("[").append(Component.translatable(item.material().registry().translationKey())).append(Component.text("]")).color(NamedTextColor.WHITE)
                ).color(NamedTextColor.RED));
                return;
            }

            int max = item.maxStackSize();
            List<ItemStack> items = new ArrayList<>();
            for (int i = 0; i < amount / max; i++) {
                items.add(item.withAmount(max));
            }
            int leftover = amount % max;
            if (leftover > 0) {
                items.add(item.withAmount(leftover));
            }

            if (size > 1) {
                for (Player player : players) {
                    for (ItemStack is : items) {
                        player.getInventory().addItemStack(is);
                    }
                }
                sender.sendMessage(Component.translatable(
                        "commands.give.success.multiple",
                        Component.text(amount),
                        Component.text("[").append(Component.translatable(item.material().registry().translationKey())).append(Component.text("]")),
                        Component.text(size)
                ));
            } else {
                Player player = players.getFirst();

                for (ItemStack is : items) {
                    player.getInventory().addItemStack(is);
                }
                sender.sendMessage(Component.translatable(
                        "commands.give.success.single",
                        Component.text(amount),
                        Component.text("[").append(Component.translatable(item.material().registry().translationKey())).append(Component.text("]")),
                        Component.text(player.getUsername())
                ));
            }
        }, playerArg, itemArg, amountArg);
    }
}
