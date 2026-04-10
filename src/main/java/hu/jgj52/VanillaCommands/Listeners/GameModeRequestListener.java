package hu.jgj52.VanillaCommands.Listeners;

import hu.jgj52.Sulfur.Permissions.User;
import hu.jgj52.Sulfur.Utils.Listeners.Event;
import hu.jgj52.Sulfur.Utils.Listeners.Listener;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerGameModeRequestEvent;

public class GameModeRequestListener extends Listener {
    @Event
    public void onRequest(PlayerGameModeRequestEvent event) {
        Player player = event.getPlayer();
        GameMode gameMode = event.getRequestedGameMode();
        User permissions = new User(player);
        if (permissions.has("vanilla.command.gamemode")) player.setGameMode(gameMode);
    }
}
