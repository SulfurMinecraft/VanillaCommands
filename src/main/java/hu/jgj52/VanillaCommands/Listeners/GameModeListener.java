package hu.jgj52.VanillaCommands.Listeners;

import hu.jgj52.Sulfur.Permissions.User;
import hu.jgj52.Sulfur.Utils.Listeners.Event;
import hu.jgj52.Sulfur.Utils.Listeners.Listener;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerGameModeChangeEvent;
import net.minestom.server.event.player.PlayerGameModeRequestEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameModeListener extends Listener {

    private Map<UUID, Boolean> wasInvisible = new HashMap<>();

    @Event
    public void onRequest(PlayerGameModeRequestEvent event) {
        Player player = event.getPlayer();
        GameMode gameMode = event.getRequestedGameMode();
        User permissions = new User(player);
        if (permissions.has("vanilla.command.gamemode")) player.setGameMode(gameMode);
    }

    @Event
    public void onGamemodeChange(PlayerGameModeChangeEvent event) {

        Player player = event.getPlayer();
        UUID uuid = player.getUuid();

        if (player.getGameMode().equals(GameMode.SPECTATOR)) player.setInvisible(wasInvisible.getOrDefault(uuid, false));

        if (event.getNewGameMode().equals(GameMode.SPECTATOR)) {

            player.setInvisible(true);

            if (player.getGameMode().equals(GameMode.SPECTATOR)) return;

            wasInvisible.put(uuid, player.isInvisible());

        }

    }

}
