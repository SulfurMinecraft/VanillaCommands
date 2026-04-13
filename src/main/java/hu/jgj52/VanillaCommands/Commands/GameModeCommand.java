package hu.jgj52.VanillaCommands.Commands;

import dev.sulfurmc.Sulfur.Permissions.User;
import dev.sulfurmc.Sulfur.Utils.SulfurCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.entity.EntityFinder;

public class GameModeCommand extends SulfurCommand {
    public GameModeCommand() {
        super("gamemode");

        Component here = Component.translatable("command.context.here").color(NamedTextColor.RED).decoration(TextDecoration.UNDERLINED, false);

        enum gamemodes {
            adventure,
            creative,
            spectator,
            survival
        }

        setCondition((commandSender, _) -> !(commandSender instanceof Player player) || new User(player).has("vanilla.command.gamemode"));

        setDefaultExecutor((sender, context) -> {
                sender.sendMessage(Component.translatable("command.unknown.command").color(NamedTextColor.RED));
                sender.sendMessage(Component.text(context.getInput()).color(NamedTextColor.GRAY).append(here));
        });

        ArgumentEnum<gamemodes> gamemode = ArgumentType.Enum("gamemode", gamemodes.class);
        addSyntax((sender, context) -> {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(Component.translatable("permissions.requires.player"));
                return;
            }

            gamemodes gm = context.get(gamemode);
            GameMode gameMode = GameMode.valueOf(gm.name().toUpperCase());

            player.setGameMode(gameMode);
            player.sendMessage(Component.translatable("commands.gamemode.success.self", Component.translatable("gameMode." + gm.name())));
        }, gamemode);

        ArgumentEntity target = ArgumentType.Entity("target").onlyPlayers(true);
        addSyntax((sender, context) -> {
            gamemodes gm = context.get(gamemode);
            GameMode gameMode = GameMode.valueOf(gm.name().toUpperCase());
            EntityFinder finder = context.get(target);

            finder.find(sender).forEach(entity -> {
                if (entity instanceof Player player) {
                    player.setGameMode(gameMode);
                    if (sender instanceof Player s && s.getUuid().equals(player.getUuid())) {
                        player.sendMessage(Component.translatable("commands.gamemode.success.self", Component.translatable("gameMode." + gm.name())));
                    } else {
                        player.sendMessage(Component.translatable("gameMode.changed", Component.text(gm.name())));
                        sender.sendMessage(Component.translatable("commands.gamemode.success.other", Component.text(player.getUsername()), Component.translatable("gameMode." + gm.name())));
                    }
                }
            });
        }, gamemode, target);
    }
}
