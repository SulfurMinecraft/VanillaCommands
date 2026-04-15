package hu.jgj52.VanillaCommands.Commands;

import dev.sulfurmc.Sulfur.Sulfur;
import dev.sulfurmc.Sulfur.Utils.SulfurCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.Weather;

public class WeatherCommand extends SulfurCommand {
    public WeatherCommand() {
        super("weather");

        Component here = Component.translatable("command.context.here").color(NamedTextColor.RED).decoration(TextDecoration.UNDERLINED, false);

        setCondition((commandSender, _) -> !(commandSender instanceof Player player) || player.hasPermission("vanilla.command.weather"));

        setDefaultExecutor((sender, context) -> {
            sender.sendMessage(Component.translatable("command.unknown.command").color(NamedTextColor.RED));
            sender.sendMessage(Component.text(context.getInput()).color(NamedTextColor.GRAY).append(here));
        });

        ArgumentLiteral clearArg = ArgumentType.Literal("clear");
        ArgumentLiteral rainArg = ArgumentType.Literal("rain");
        ArgumentLiteral thunderArg = ArgumentType.Literal("thunder");

        addSyntax((sender, _) -> {
            Instance instance;
            if (sender instanceof Entity entity) {
                instance = entity.getInstance();
            } else {
                instance = Sulfur.ic;
            }
            instance.setWeather(Weather.CLEAR);
            sender.sendMessage(Component.translatable(
                    "commands.weather.set.clear"
            ));
        }, clearArg);

        addSyntax((sender, _) -> {
            Instance instance;
            if (sender instanceof Entity entity) {
                instance = entity.getInstance();
            } else {
                instance = Sulfur.ic;
            }
            instance.setWeather(Weather.RAIN);
            sender.sendMessage(Component.translatable(
                    "commands.weather.set.rain"
            ));
        }, rainArg);

        addSyntax((sender, _) -> {
            Instance instance;
            if (sender instanceof Entity entity) {
                instance = entity.getInstance();
            } else {
                instance = Sulfur.ic;
            }
            instance.setWeather(Weather.THUNDER);
            sender.sendMessage(Component.translatable(
                    "commands.weather.set.thunder"
            ));
        }, thunderArg);
    }
}
