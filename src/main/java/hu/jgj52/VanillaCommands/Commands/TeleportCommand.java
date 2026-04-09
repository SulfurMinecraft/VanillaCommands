package hu.jgj52.VanillaCommands.Commands;

import hu.jgj52.SulfurPermissions.Utils.PermissionManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.command.builder.arguments.number.ArgumentFloat;
import net.minestom.server.command.builder.arguments.relative.ArgumentRelativeVec3;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;

import java.util.List;

public class TeleportCommand extends Command {
    public TeleportCommand() {
        super("teleport", "tp");

        Component here = Component.translatable("command.context.here").color(NamedTextColor.RED).decoration(TextDecoration.UNDERLINED, false);

        setCondition((commandSender, _) -> !(commandSender instanceof Player player) || new PermissionManager(player, "vanilla.command").has("teleport"));

        setDefaultExecutor((sender, context) -> {
            sender.sendMessage(Component.translatable("command.unknown.command").color(NamedTextColor.RED));
            sender.sendMessage(Component.text(context.getInput()).color(NamedTextColor.GRAY).append(here));
        });

        ArgumentEntity e1 = ArgumentType.Entity("e1");
        ArgumentEntity e2 = ArgumentType.Entity("e2").singleEntity(true);

        ArgumentRelativeVec3 pos = ArgumentType.RelativeVec3("pos");
        ArgumentFloat yaw = ArgumentType.Float("yaw");
        ArgumentFloat pitch = ArgumentType.Float("pitch");

        addSyntax((sender, context) -> {
            if (sender instanceof Entity entity) {
                Entity to = context.get(e2).findFirstEntity(sender);
                if (to == null) return;
                entity.teleport(to.getPosition());
                sender.sendMessage(Component.translatable(
                        "commands.teleport.success.entity.single",
                        name(entity),
                        name(to)
                ));
            } else {
                sender.sendMessage(Component.translatable("permissions.requires.entity").color(NamedTextColor.RED));
            }
        }, e2);

        addSyntax((sender, context) -> {
            if (sender instanceof Entity entity) {
                Pos position = context.get(pos).from(entity.getPosition()).asPos();
                entity.teleport(position);
                sender.sendMessage(Component.translatable(
                        "commands.teleport.success.location.single",
                        name(entity),
                        Component.text(position.x()),
                        Component.text(position.y()),
                        Component.text(position.z())
                ));
            } else {
                sender.sendMessage(Component.translatable("permissions.requires.entity").color(NamedTextColor.RED));
            }
        }, pos);

        addSyntax((sender, context) -> {
            if (sender instanceof Entity entity) {
                Pos position = context.get(pos).from(entity.getPosition()).asPos();
                float y = context.get(yaw);
                float p = context.get(pitch);
                entity.teleport(position.withYaw(y).withPitch(p));
                sender.sendMessage(Component.translatable(
                        "commands.teleport.success.location.single",
                        name(entity),
                        Component.text(position.x()),
                        Component.text(position.y()),
                        Component.text(position.z())
                ));
            } else {
                sender.sendMessage(Component.translatable("permissions.requires.entity").color(NamedTextColor.RED));
            }
        }, pos, yaw, pitch);

        addSyntax((sender, context) -> {
            Entity to = context.get(e2).findFirstEntity(sender);
            if (to == null) return;
            List<Entity> entities = context.get(e1).find(sender);
            for (Entity entity : entities) {
                entity.teleport(to.getPosition());
            }
            sender.sendMessage(entities.size() > 1
            ? Component.translatable(
                    "commands.teleport.success.entity.multiple",
                    Component.text(entities.size()),
                    name(to)
                )
            : Component.translatable(
                    "commands.teleport.success.entity.single",
                    name(entities.getFirst()),
                    to instanceof Player player
                    ? Component.text(player.getUsername())
                    : Component.translatable(to.getEntityType().registry().translationKey())
                )
            );
        }, e1, e2);

        addSyntax((sender, context) -> {
            Pos position = context.get(pos).from(
                    sender instanceof Entity entity
                            ? entity.getPosition()
                            : new Pos(0, 0, 0)
            ).asPos();
            List<Entity> entities = context.get(e1).find(sender);
            for (Entity entity : entities) {
                entity.teleport(position);
            }
            sender.sendMessage(entities.size() > 1
            ? Component.translatable(
                    "commands.teleport.success.location.multiple",
                    Component.text(entities.size()),
                    Component.text(position.x()),
                    Component.text(position.y()),
                    Component.text(position.z())
                )
            : Component.translatable(
                    "commands.teleport.success.location.single",
                    name(entities.getFirst()),
                    Component.text(position.x()),
                    Component.text(position.y()),
                    Component.text(position.z())
                )
            );
        }, e1, pos);

        addSyntax((sender, context) -> {
            Pos position = context.get(pos).from(
                    sender instanceof Entity entity
                    ? entity.getPosition()
                    : new Pos(0, 0, 0)
            ).asPos();
            float y = context.get(yaw);
            float p = context.get(pitch);
            List<Entity> entities = context.get(e1).find(sender);
            for (Entity entity : entities) {
                entity.teleport(position.withYaw(y).withPitch(p));
            }
            sender.sendMessage(entities.size() > 1
            ? Component.translatable(
                    "commands.teleport.success.location.multiple",
                    Component.text(entities.size()),
                    Component.text(position.x()),
                    Component.text(position.y()),
                    Component.text(position.z())
                )
            : Component.translatable(
                    "commands.teleport.success.location.single",
                    name(entities.getFirst()),
                    Component.text(position.x()),
                    Component.text(position.y()),
                    Component.text(position.z())
                )
            );
        }, e1, pos, yaw, pitch);
    }

    private Component name(Entity entity) {
        return entity instanceof Player player
                ? Component.text(player.getUsername())
                : Component.translatable(entity.getEntityType().registry().translationKey());
    }
}
