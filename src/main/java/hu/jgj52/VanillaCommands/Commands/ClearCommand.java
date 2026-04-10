package hu.jgj52.VanillaCommands.Commands;

import hu.jgj52.Sulfur.Permissions.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentItemStack;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.List;

public class ClearCommand extends Command {
    public ClearCommand() {
        super("clear");

        Component here = Component.translatable("command.context.here").color(NamedTextColor.RED).decoration(TextDecoration.UNDERLINED, false);

        setCondition((commandSender, _) -> !(commandSender instanceof Player player) || new User(player).has("vanilla.command.clear"));

        setDefaultExecutor((sender, context) -> {
            sender.sendMessage(Component.translatable("command.unknown.command").color(NamedTextColor.RED));
            sender.sendMessage(Component.text(context.getInput()).color(NamedTextColor.GRAY).append(here));
        });

        ArgumentEntity playerArg = ArgumentType.Entity("players").onlyPlayers(true);
        ArgumentItemStack itemArg = ArgumentType.ItemStack("item");
        ArgumentInteger maxAmount = ArgumentType.Integer("maxAmount");

        addSyntax((sender, _) -> {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(Component.translatable("permissions.requires.player"));
                return;
            }

            int count = 0;
            for (ItemStack itemStack : player.getInventory().getItemStacks()) {
                count += itemStack.amount();
            }
            player.getInventory().clear();
            if (count == 0) {
                sender.sendMessage(Component.translatable(
                        "clear.failed.single",
                        Component.text(player.getUsername())
                ).color(NamedTextColor.RED));
            } else {
                sender.sendMessage(Component.translatable(
                        "commands.clear.success.single",
                        Component.text(count),
                        Component.text(player.getUsername())
                    ));
            }
        });

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
            if (size > 1) {
                int count = 0;

                for (Player player : players) {
                    for (ItemStack itemStack : player.getInventory().getItemStacks()) {
                        count += itemStack.amount();
                    }
                    player.getInventory().clear();
                }

                if (count == 0) {
                    sender.sendMessage(Component.translatable(
                            "clear.failed.multiple",
                            Component.text(size)
                    ).color(NamedTextColor.RED));
                } else {
                    sender.sendMessage(Component.translatable(
                            "commands.clear.success.multiple",
                            Component.text(count),
                            Component.text(size)
                    ));
                }
            } else {
                Player player = players.getFirst();

                int count = 0;
                for (ItemStack itemStack : player.getInventory().getItemStacks()) {
                    count += itemStack.amount();
                }
                player.getInventory().clear();
                if (count == 0) {
                    sender.sendMessage(Component.translatable(
                            "clear.failed.single",
                            Component.text(player.getUsername())
                    ).color(NamedTextColor.RED));
                } else {
                    sender.sendMessage(Component.translatable(
                            "commands.clear.success.single",
                            Component.text(count),
                            Component.text(player.getUsername())
                    ));
                }
            }
        }, playerArg);

        addSyntax((sender, context) -> {
            List<Player> players = context.get(playerArg).find(sender).stream()
                    .filter(Player.class::isInstance)
                    .map(Player.class::cast)
                    .toList();
            Material material = context.get(itemArg).material();
            if (players.isEmpty()) {
                sender.sendMessage(Component.translatable("argument.entity.notfound.player").color(NamedTextColor.RED));
                return;
            }
            int size = players.size();
            if (size > 1) {
                int count = 0;

                for (Player player : players) {
                    PlayerInventory inv = player.getInventory();
                    for (int i = 0; i < inv.getItemStacks().length; i++) {
                        ItemStack itemStack = inv.getItemStack(i);
                        if (itemStack.material() == material) {
                            player.getInventory().setItemStack(i, ItemStack.AIR);
                            count += itemStack.amount();
                        }
                    }
                }

                if (count == 0) {
                    sender.sendMessage(Component.translatable(
                            "clear.failed.multiple",
                            Component.text(size)
                    ).color(NamedTextColor.RED));
                } else {
                    sender.sendMessage(Component.translatable(
                            "commands.clear.success.multiple",
                            Component.text(count),
                            Component.text(size)
                    ));
                }
            } else {
                Player player = players.getFirst();

                int count = 0;
                PlayerInventory inv = player.getInventory();
                for (int i = 0; i < inv.getItemStacks().length; i++) {
                    ItemStack itemStack = inv.getItemStack(i);
                    if (itemStack.material() == material) {
                        player.getInventory().setItemStack(i, ItemStack.AIR);
                        count += itemStack.amount();
                    }
                }
                if (count == 0) {
                    sender.sendMessage(Component.translatable(
                            "clear.failed.single",
                            Component.text(player.getUsername())
                    ).color(NamedTextColor.RED));
                } else {
                    sender.sendMessage(Component.translatable(
                            "commands.clear.success.single",
                            Component.text(count),
                            Component.text(player.getUsername())
                    ));
                }
            }
        }, playerArg, itemArg);

        addSyntax((sender, context) -> {
            List<Player> players = context.get(playerArg).find(sender).stream()
                    .filter(Player.class::isInstance)
                    .map(Player.class::cast)
                    .toList();
            Material material = context.get(itemArg).material();
            int max = context.get(maxAmount);
            if (players.isEmpty()) {
                sender.sendMessage(Component.translatable("argument.entity.notfound.player").color(NamedTextColor.RED));
                return;
            }
            int size = players.size();
            if (size > 1) {
                int count = 0;

                for (Player player : players) {
                    int c = 0;
                    PlayerInventory inv = player.getInventory();
                    for (int i = 0; i < inv.getItemStacks().length; i++) {
                        ItemStack itemStack = inv.getItemStack(i);
                        if (itemStack.material() == material) {
                            if (max == 0) {
                                c += itemStack.amount();
                                continue;
                            }
                            if (itemStack.amount() + c > max) {
                                int remain = max - c;
                                player.getInventory().setItemStack(
                                        i,
                                        itemStack.withAmount(itemStack.amount() - remain)
                                );
                                c += remain;
                                break;
                            } else {
                                player.getInventory().setItemStack(i, ItemStack.AIR);
                                c += itemStack.amount();
                            }
                            if (c >= max) break;
                        }
                    }
                    count += c;
                }

                if (max == 0) {
                    sender.sendMessage(Component.translatable(
                            "commands.clear.test.multiple",
                            Component.text(count),
                            Component.text(size)
                    ));
                } else {
                    if (count == 0) {
                        sender.sendMessage(Component.translatable(
                                "clear.failed.multiple",
                                Component.text(size)
                        ).color(NamedTextColor.RED));
                    } else {
                        sender.sendMessage(Component.translatable(
                                "commands.clear.success.multiple",
                                Component.text(count),
                                Component.text(size)
                        ));
                    }
                }
            } else {
                Player player = players.getFirst();

                int count = 0;
                PlayerInventory inv = player.getInventory();
                for (int i = 0; i < inv.getItemStacks().length; i++) {
                    ItemStack itemStack = inv.getItemStack(i);
                    if (itemStack.material() == material) {
                        if (max == 0) {
                            count += itemStack.amount();
                            continue;
                        }
                        if (itemStack.amount() + count > max) {
                            int remain = max - count;
                            player.getInventory().setItemStack(
                                    i,
                                    itemStack.withAmount(itemStack.amount() - remain)
                            );
                            count += remain;
                            break;
                        } else {
                            player.getInventory().setItemStack(i, ItemStack.AIR);
                            count += itemStack.amount();
                        }
                        if (count >= max) break;
                    }
                }
                if (max == 0) {
                    sender.sendMessage(Component.translatable(
                            "commands.clear.test.single",
                            Component.text(count),
                            Component.text(player.getUsername())
                    ));
                } else {
                    if (count == 0) {
                        sender.sendMessage(Component.translatable(
                                "clear.failed.single",
                                Component.text(player.getUsername())
                        ).color(NamedTextColor.RED));
                    } else {
                        sender.sendMessage(Component.translatable(
                                "commands.clear.success.single",
                                Component.text(count),
                                Component.text(player.getUsername())
                        ));
                    }
                }
            }
        }, playerArg, itemArg, maxAmount);
    }
}

