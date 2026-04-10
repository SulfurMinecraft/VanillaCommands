package hu.jgj52.VanillaCommands.Commands;

import hu.jgj52.Sulfur.Permissions.User;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandExecutor;
import net.minestom.server.command.builder.arguments.ArgumentBoolean;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentResourceLocation;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.potion.PotionEffect;
import net.minestom.server.potion.TimedPotion;

import java.util.*;

@SuppressWarnings("ConstantConditions")
public class EffectCommand extends Command {
    public EffectCommand() {
        super("effect");

        Component here = Component.translatable("command.context.here").color(NamedTextColor.RED).decoration(TextDecoration.UNDERLINED, false);

        setCondition((commandSender, _) -> !(commandSender instanceof Player player) || new User(player).has("vanilla.command.effect"));

        setDefaultExecutor((sender, context) -> {
            sender.sendMessage(Component.translatable("command.unknown.command").color(NamedTextColor.RED));
            sender.sendMessage(Component.text(context.getInput()).color(NamedTextColor.GRAY).append(here));
        });

        enum whatodo {
            clear,
            give
        }

        ArgumentLiteral clearArg = ArgumentType.Literal("clear");
        ArgumentLiteral giveArg = ArgumentType.Literal("give");
        ArgumentEntity entityArg = ArgumentType.Entity("targets");
        ArgumentResourceLocation effectArg = ArgumentType.ResourceLocation("effect");
        ArgumentLiteral infiniteArg = ArgumentType.Literal("infinite");
        ArgumentInteger secondsArg = ArgumentType.Integer("seconds");
        ArgumentInteger amplifierArg = ArgumentType.Integer("amplifier");
        ArgumentBoolean hideArg = ArgumentType.Boolean("hideParticles");

        List<SuggestionEntry> suggest = PotionEffect.values().stream()
                        .map(effect -> effect.key().asString())
                        .sorted()
                        .map(SuggestionEntry::new)
                        .toList();

        effectArg.setSuggestionCallback((_, context, suggestion) -> {
            Key searchK = context.get(effectArg);
            if (searchK == null) {
                suggest.forEach(suggestion::addEntry);
                return;
            }
            String search = searchK.asString();
            suggest.forEach(s -> {
                if (search.isBlank()) {
                    suggestion.addEntry(s);
                    return;
                }
                if (s.getEntry().contains(search)) {
                    suggestion.addEntry(s);
                }
            });
        });

        addSyntax((sender, _) -> {
            if (!(sender instanceof Entity entity)) {
                sender.sendMessage(Component.translatable("permissions.requires.entity").color(NamedTextColor.RED));
                return;
            }
            boolean empty = entity.getActiveEffects().isEmpty();
            if (empty) {
                sender.sendMessage(Component.translatable(
                        "commands.effect.clear.everything.failed"
                ).color(NamedTextColor.RED));
            } else {
                entity.clearEffects();
                sender.sendMessage(Component.translatable(
                        "commands.effect.clear.everything.success.single",
                        entity instanceof Player player
                        ? Component.text(player.getUsername())
                        : Component.translatable(entity.getEntityType().registry().translationKey())
                ));
            }
        }, clearArg);

        addSyntax((sender, context) -> {
            boolean empty = true;
            List<Entity> entities = context.get(entityArg).find(sender);
            if (entities.isEmpty()) {
                sender.sendMessage(Component.translatable(
                        "argument.entity.notfound.entity"
                ).color(NamedTextColor.RED));
                return;
            }
            int size = entities.size();
            if (size > 1) {
                for (Entity entity : entities) {
                    boolean e = entity.getActiveEffects().isEmpty();
                    if (!e) {
                        entity.clearEffects();
                    }
                    if (empty) {
                        empty = e;
                    }
                }

                if (empty) {
                    sender.sendMessage(Component.translatable(
                            "commands.effect.clear.everything.failed"
                    ).color(NamedTextColor.RED));
                } else {
                    sender.sendMessage(Component.translatable(
                            "commands.effect.clear.everything.success.multiple",
                            Component.text(size)
                    ));
                }
            } else {
                Entity entity = entities.getFirst();

                empty = entity.getActiveEffects().isEmpty();
                entity.clearEffects();

                if (empty) {
                    sender.sendMessage(Component.translatable(
                            "commands.effect.clear.everything.failed"
                    ).color(NamedTextColor.RED));
                } else {
                    entity.clearEffects();
                    sender.sendMessage(Component.translatable(
                            "commands.effect.clear.everything.success.single",
                            entity instanceof Player player
                                    ? Component.text(player.getUsername())
                                    : Component.translatable(entity.getEntityType().registry().translationKey())
                    ));
                }
            }
        }, clearArg, entityArg);

        addSyntax((sender, context) -> {
            boolean empty = true;
            List<Entity> entities = context.get(entityArg).find(sender);
            PotionEffect effect = PotionEffect.fromKey(context.get(effectArg));
            if (effect == null) {
                CommandExecutor defaultE = getDefaultExecutor();
                if (defaultE == null) return;
                defaultE.apply(sender, context);
                return;
            }
            if (entities.isEmpty()) {
                sender.sendMessage(Component.translatable(
                        "argument.entity.notfound.entity"
                ).color(NamedTextColor.RED));
                return;
            }
            int size = entities.size();
            if (size > 1) {
                for (Entity entity : entities) {
                    boolean e = true;
                    for (TimedPotion potion : entity.getActiveEffects()) {
                        if (effect.id() == potion.potion().effect().id()) {
                            entity.removeEffect(potion.potion().effect());
                            if (e) {
                                e = false;
                            }
                        }
                    }
                    if (empty) {
                        empty = e;
                    }
                }

                if (empty) {
                    sender.sendMessage(Component.translatable(
                            "commands.effect.clear.specific.failed"
                    ).color(NamedTextColor.RED));
                } else {
                    sender.sendMessage(Component.translatable(
                            "commands.effect.clear.specific.success.multiple",
                            Component.translatable(effect.registry().translationKey()),
                            Component.text(size)
                    ));
                }
            } else {
                Entity entity = entities.getFirst();

                for (TimedPotion potion : entity.getActiveEffects()) {
                    if (effect.id() == potion.potion().effect().id()) {
                        entity.removeEffect(potion.potion().effect());
                        if (empty) {
                            empty = false;
                        }
                    }
                }

                if (empty) {
                    sender.sendMessage(Component.translatable(
                            "commands.effect.clear.specific.failed"
                    ).color(NamedTextColor.RED));
                } else {
                    entity.clearEffects();
                    sender.sendMessage(Component.translatable(
                            "commands.effect.clear.specific.success.single",
                            Component.translatable(effect.registry().translationKey()),
                            entity instanceof Player player
                                    ? Component.text(player.getUsername())
                                    : Component.translatable(entity.getEntityType().registry().translationKey())
                    ));
                }
            }
        }, clearArg, entityArg, effectArg);
    }
}
