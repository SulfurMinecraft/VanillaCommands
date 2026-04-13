package hu.jgj52.VanillaCommands.Commands;

import dev.sulfurmc.Sulfur.Permissions.User;
import dev.sulfurmc.Sulfur.Utils.SulfurCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandExecutor;
import net.minestom.server.command.builder.arguments.*;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentResourceLocation;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.minestom.server.potion.TimedPotion;

import java.util.*;

@SuppressWarnings("ConstantConditions")
public class EffectCommand extends SulfurCommand {
    public EffectCommand() {
        super("effect");

        Component here = Component.translatable("command.context.here").color(NamedTextColor.RED).decoration(TextDecoration.UNDERLINED, false);

        setCondition((commandSender, _) -> !(commandSender instanceof Player player) || new User(player).has("vanilla.command.effect"));

        setDefaultExecutor((sender, context) -> {
            sender.sendMessage(Component.translatable("command.unknown.command").color(NamedTextColor.RED));
            sender.sendMessage(Component.text(context.getInput()).color(NamedTextColor.GRAY).append(here));
        });

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
            String search = context.getRaw(effectArg);
            System.out.println(search);
            suggest.forEach(s -> {
                if (search == null || search.isBlank()) {
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
                        name(entity)
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

                if (empty) {
                    sender.sendMessage(Component.translatable(
                            "commands.effect.clear.everything.failed"
                    ).color(NamedTextColor.RED));
                } else {
                    entity.clearEffects();
                    sender.sendMessage(Component.translatable(
                            "commands.effect.clear.everything.success.single",
                            name(entity)
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
                    sender.sendMessage(Component.translatable(
                            "commands.effect.clear.specific.success.single",
                            Component.translatable(effect.registry().translationKey()),
                            name(entity)
                    ));
                }
            }
        }, clearArg, entityArg, effectArg);

        addSyntax((sender, context) -> {
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
            Potion potion = new Potion(effect, 0, 600);

            give(entities, potion, sender, effect);
        }, giveArg, entityArg, effectArg);

        addSyntax((sender, context) -> {
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
            Potion potion = new Potion(effect, 0, Potion.INFINITE_DURATION);

            give(entities, potion, sender, effect);
        }, giveArg, entityArg, effectArg, infiniteArg);

        addSyntax((sender, context) -> {
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
            int ticks = context.get(secondsArg);
            Potion potion = new Potion(effect, 0, ticks * 20);

            give(entities, potion, sender, effect);
        }, giveArg, entityArg, effectArg, secondsArg);

        addSyntax((sender, context) -> {
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
            int amplifier = context.get(amplifierArg);
            Potion potion = new Potion(effect, amplifier, Potion.INFINITE_DURATION);

            give(entities, potion, sender, effect);
        }, giveArg, entityArg, effectArg, infiniteArg, amplifierArg);

        addSyntax((sender, context) -> {
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
            int ticks = context.get(secondsArg);
            int amplifier = context.get(amplifierArg);
            Potion potion = new Potion(effect, amplifier, ticks * 20);

            give(entities, potion, sender, effect);
        }, giveArg, entityArg, effectArg, secondsArg, amplifierArg);

        addSyntax((sender, context) -> {
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
            int amplifier = context.get(amplifierArg);
            boolean hide = context.get(hideArg);
            Potion potion = hide
                    ? new Potion(effect, amplifier, Potion.INFINITE_DURATION, Potion.PARTICLES_FLAG)
                    : new Potion(effect, amplifier, Potion.INFINITE_DURATION);

            give(entities, potion, sender, effect);
        }, giveArg, entityArg, effectArg, infiniteArg, amplifierArg, hideArg);

        addSyntax((sender, context) -> {
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
            int ticks = context.get(secondsArg);
            int amplifier = context.get(amplifierArg);
            boolean hide = context.get(hideArg);
            Potion potion = hide
                    ? new Potion(effect, amplifier, ticks * 20, Potion.PARTICLES_FLAG)
                    : new Potion(effect, amplifier, ticks * 20);

            give(entities, potion, sender, effect);
        }, giveArg, entityArg, effectArg, secondsArg, amplifierArg, hideArg);
    }

    private Component name(Entity entity) {
        return entity instanceof Player player
                ? Component.text(player.getUsername())
                : Component.translatable(entity.getEntityType().registry().translationKey());
    }

    private void give(List<Entity> entities, Potion potion, CommandSender sender, PotionEffect effect) {
        int size = entities.size();
        if (size > 1) {
            int errs = 0;
            for (Entity entity : entities) {
                TimedPotion pot = entity.getEffect(effect);
                if (pot != null) {
                    int duration = pot.potion().duration();
                    if (duration > potion.duration() || (duration == Potion.INFINITE_DURATION && potion.duration() != Potion.INFINITE_DURATION) || pot.potion().amplifier() > potion.amplifier()) {
                        errs++;
                        continue;
                    }
                }
                entity.addEffect(potion);
            }

            if (errs >= size) {
                sender.sendMessage(Component.translatable(
                        "commands.effect.give.failed"
                ).color(NamedTextColor.RED));
            }

            sender.sendMessage(Component.translatable(
                    "commands.effect.give.success.multiple",
                    Component.translatable(effect.registry().translationKey()),
                    Component.text(size)
            ));
        } else {
            Entity entity = entities.getFirst();
            TimedPotion pot = entity.getEffect(effect);
            if (pot != null) {
                int duration = pot.potion().duration();
                if (duration > potion.duration() || (duration == Potion.INFINITE_DURATION && potion.duration() != Potion.INFINITE_DURATION) || pot.potion().amplifier() > potion.amplifier()) {
                    sender.sendMessage(Component.translatable(
                            "commands.effect.give.failed"
                    ).color(NamedTextColor.RED));
                    return;
                }
            }
            entity.addEffect(potion);

            sender.sendMessage(Component.translatable(
                    "commands.effect.give.success.single",
                    Component.translatable(effect.registry().translationKey()),
                    name(entity)
            ));
        }
    }
}
