package hu.jgj52.VanillaCommands.Commands;

import dev.sulfurmc.Sulfur.Utils.SulfurCommand;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentResource;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;
import net.minestom.server.command.builder.arguments.relative.ArgumentRelativeVec3;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.sound.SoundEvent;

import java.util.List;

public class PlaysoundCommand extends SulfurCommand {
    public PlaysoundCommand() {
        super("playsound");

        Component here = Component.translatable("command.context.here").color(NamedTextColor.RED).decoration(TextDecoration.UNDERLINED, false);

        setCondition((commandSender, _) -> !(commandSender instanceof Player player) || player.hasPermission("vanilla.command.playsound"));

        setDefaultExecutor((sender, context) -> {
            sender.sendMessage(Component.translatable("command.unknown.command").color(NamedTextColor.RED));
            sender.sendMessage(Component.text(context.getInput()).color(NamedTextColor.GRAY).append(here));
        });

        enum sources {
            ambient,
            block,
            hostile,
            master,
            music,
            neutral,
            player,
            record,
            ui,
            voice,
            weather
        }

        ArgumentResource soundArg = ArgumentType.Resource("sound", "minecraft:sound_event");
        ArgumentEnum<sources> sourceArg = ArgumentType.Enum("source", sources.class);
        ArgumentEntity playerArg = ArgumentType.Entity("player").onlyPlayers(true);
        ArgumentRelativeVec3 posArg = ArgumentType.RelativeVec3("pos");
        ArgumentNumber<Float> volumeArg = ArgumentType.Float("volume").min(0f);
        ArgumentNumber<Float> pitchArg = ArgumentType.Float("pitch").min(0f).max(2f);
        ArgumentNumber<Float> minVolumeArg = ArgumentType.Float("minVolume").min(0f).max(1f);

        addSyntax((sender, context) -> {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(Component.translatable(
                        "permissions.requires.player"
                ).color(NamedTextColor.RED));
                return;
            }

            play(
                    sender,
                    SoundEvent.fromKey(context.get(soundArg)),
                    Sound.Source.MASTER,
                    List.of(player),
                    player.getPosition(),
                    1,
                    1,
                    1
            );
        }, soundArg);

        addSyntax((sender, context) -> {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(Component.translatable(
                        "permissions.requires.player"
                ).color(NamedTextColor.RED));
                return;
            }

            play(
                    sender,
                    SoundEvent.fromKey(context.get(soundArg)),
                    Sound.Source.valueOf(context.get(sourceArg).name().toUpperCase()),
                    List.of(player),
                    player.getPosition(),
                    1,
                    1,
                    1
            );
        }, soundArg, sourceArg);

        addSyntax((sender, context) -> {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(Component.translatable(
                        "permissions.requires.player"
                ).color(NamedTextColor.RED));
                return;
            }

            List<Player> players = context.get(playerArg).find(sender).stream()
                    .filter(e -> e instanceof Player)
                    .map(e -> (Player) e)
                    .toList();

            play(
                    sender,
                    SoundEvent.fromKey(context.get(soundArg)),
                    Sound.Source.valueOf(context.get(sourceArg).name().toUpperCase()),
                    players,
                    player.getPosition(),
                    1,
                    1,
                    1
            );
        }, soundArg, sourceArg, playerArg);

        addSyntax((sender, context) -> {
            List<Player> players = context.get(playerArg).find(sender).stream()
                    .filter(e -> e instanceof Player)
                    .map(e -> (Player) e)
                    .toList();

            play(
                    sender,
                    SoundEvent.fromKey(context.get(soundArg)),
                    Sound.Source.valueOf(context.get(sourceArg).name().toUpperCase()),
                    players,
                    context.get(posArg).fromSender(sender),
                    1,
                    1,
                    1
            );
        }, soundArg, sourceArg, playerArg, posArg);

        addSyntax((sender, context) -> {
            List<Player> players = context.get(playerArg).find(sender).stream()
                    .filter(e -> e instanceof Player)
                    .map(e -> (Player) e)
                    .toList();

            play(
                    sender,
                    SoundEvent.fromKey(context.get(soundArg)),
                    Sound.Source.valueOf(context.get(sourceArg).name().toUpperCase()),
                    players,
                    context.get(posArg).fromSender(sender),
                    context.get(volumeArg),
                    1,
                    1
            );
        }, soundArg, sourceArg, playerArg, posArg, volumeArg);

        addSyntax((sender, context) -> {
            List<Player> players = context.get(playerArg).find(sender).stream()
                    .filter(e -> e instanceof Player)
                    .map(e -> (Player) e)
                    .toList();

            play(
                    sender,
                    SoundEvent.fromKey(context.get(soundArg)),
                    Sound.Source.valueOf(context.get(sourceArg).name().toUpperCase()),
                    players,
                    context.get(posArg).fromSender(sender),
                    context.get(volumeArg),
                    context.get(pitchArg),
                    1
            );
        }, soundArg, sourceArg, playerArg, posArg, volumeArg, pitchArg);

        addSyntax((sender, context) -> {
            List<Player> players = context.get(playerArg).find(sender).stream()
                    .filter(e -> e instanceof Player)
                    .map(e -> (Player) e)
                    .toList();

            play(
                    sender,
                    SoundEvent.fromKey(context.get(soundArg)),
                    Sound.Source.valueOf(context.get(sourceArg).name().toUpperCase()),
                    players,
                    context.get(posArg).fromSender(sender),
                    context.get(volumeArg),
                    context.get(pitchArg),
                    context.get(minVolumeArg)
            );
        }, soundArg, sourceArg, playerArg, posArg, volumeArg, pitchArg, minVolumeArg);
    }

    private void play(CommandSender sender, SoundEvent event, Sound.Source source, List<Player> players, Point point, float volume, float pitch, float minVolume) {
        Sound sound = Sound.sound()
                .type(event)
                .source(source)
                .volume(volume)
                .pitch(pitch)
                .build();

        for (Player player : players) {
            player.playSound(sound, point);
        }

        int size = players.size();
        if (size > 1) {
            sender.sendMessage(Component.translatable(
                    "commands.playsound.success.multiple",
                    Component.text(event.key().asString()),
                    Component.text(size)
            ));
        } else {
            sender.sendMessage(Component.translatable(
                    "commands.playsound.success.single",
                    Component.text(event.key().asString()),
                    Component.text(players.getFirst().getUsername())
            ));
        }
    }
}
