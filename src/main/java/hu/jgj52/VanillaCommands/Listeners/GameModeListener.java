package hu.jgj52.VanillaCommands.Listeners;

import dev.sulfurmc.Sulfur.Permissions.User;
import dev.sulfurmc.Sulfur.Utils.Listeners.Event;
import dev.sulfurmc.Sulfur.Utils.Listeners.Listener;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerGameModeChangeEvent;
import net.minestom.server.event.player.PlayerGameModeRequestEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameModeListener extends Listener {
    private final Map<UUID, Boolean> wasInvisible = new HashMap<>();

    @Event
    public void onRequest(PlayerGameModeRequestEvent event) {
        Player player = event.getPlayer();
        GameMode gameMode = event.getRequestedGameMode();
        User permissions = new User(player);
        if (permissions.has("vanilla.command.gamemode")) player.setGameMode(gameMode);
    }

    @Event
    public void onChange(PlayerGameModeChangeEvent event) {

        Player player = event.getPlayer();
        UUID uuid = player.getUuid();
        GameMode from = player.getGameMode();
        GameMode to = event.getNewGameMode();

        if (from == GameMode.SPECTATOR && to != GameMode.SPECTATOR) {
            player.setInvisible(wasInvisible.getOrDefault(uuid, false));
            wasInvisible.remove(uuid);
        }

        if (from != GameMode.SPECTATOR && to == GameMode.SPECTATOR) {
            wasInvisible.put(uuid, player.isInvisible());
            player.setInvisible(true);
        }

    }

}
