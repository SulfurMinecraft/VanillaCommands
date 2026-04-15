package hu.jgj52.VanillaCommands.Commands;

import dev.sulfurmc.Sulfur.Sulfur;
import dev.sulfurmc.Sulfur.Utils.SulfurCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.registry.ArgumentEntityType;
import net.minestom.server.command.builder.arguments.relative.ArgumentRelativeVec3;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public class SummonCommand extends SulfurCommand {
    public SummonCommand() {
        super("summon");

        Component here = Component.translatable("command.context.here").color(NamedTextColor.RED).decoration(TextDecoration.UNDERLINED, false);

        setCondition((commandSender, _) -> !(commandSender instanceof Player player) || player.hasPermission("vanilla.command.summon"));

        setDefaultExecutor((sender, context) -> {
            sender.sendMessage(Component.translatable("command.unknown.command").color(NamedTextColor.RED));
            sender.sendMessage(Component.text(context.getInput()).color(NamedTextColor.GRAY).append(here));
        });

        ArgumentEntityType entityArg = ArgumentType.EntityType("entity");
        ArgumentRelativeVec3 posArg = ArgumentType.RelativeVec3("pos");

        addSyntax((sender, context) -> {
            Entity entity = new Entity(context.get(entityArg));

            if (sender instanceof Entity e) {
                Instance instance = e.getInstance();
                Pos pos = e.getPosition().withYaw(0).withPitch(0);
                entity.setInstance(instance);
                entity.teleport(pos);
                sender.sendMessage(Component.translatable(
                        "commands.summon.success",
                        Component.translatable(entity.getEntityType().registry().translationKey())
                ));
            } else {
                sender.sendMessage(Component.translatable(
                        "permissions.requires.entity"
                ).color(NamedTextColor.RED));
            }
        }, entityArg);

        addSyntax((sender, context) -> {
            Entity entity = new Entity(context.get(entityArg));
            Pos pos;

            Instance instance;
            if (sender instanceof Entity e) {
                instance = e.getInstance();
                pos = context.get(posArg).from(e).asPos();
            } else {
                instance = Sulfur.ic;
                 pos = context.get(posArg).vec().asPos();
            }

            entity.setInstance(instance);
            entity.teleport(pos);
            sender.sendMessage(Component.translatable(
                    "commands.summon.success",
                    Component.translatable(entity.getEntityType().registry().translationKey())
            ));
        }, entityArg, posArg);
    }
}
