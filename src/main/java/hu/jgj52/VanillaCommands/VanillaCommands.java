package hu.jgj52.VanillaCommands;

import hu.jgj52.Sulfur.Utils.Plugin;
import hu.jgj52.VanillaCommands.Commands.*;
import hu.jgj52.VanillaCommands.Listeners.GameModeListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;

public class VanillaCommands implements Plugin {
    @Override
    public void onEnable() {
        new GameModeListener();

        MinecraftServer.getCommandManager().register(new GameModeCommand());
        MinecraftServer.getCommandManager().register(new TeleportCommand());
        MinecraftServer.getCommandManager().register(new GiveCommand());
        MinecraftServer.getCommandManager().register(new ClearCommand());
        MinecraftServer.getCommandManager().register(new DifficultyCommand());
        MinecraftServer.getCommandManager().register(new EffectCommand());

        MinecraftServer.getCommandManager().setUnknownCommandCallback((sender, command) -> {
            sender.sendMessage(Component.translatable("command.unknown.command").color(NamedTextColor.RED));
            sender.sendMessage(Component.text(command).decorate(TextDecoration.UNDERLINED).color(NamedTextColor.RED).append(Component.translatable("command.context.here").color(NamedTextColor.RED).decoration(TextDecoration.UNDERLINED, false)));
        });
    }

    @Override
    public void onDisable() {

    }
}
