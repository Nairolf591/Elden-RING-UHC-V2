package main.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.TimeSkipEvent;
import main.game.PlayerFlasks;
import org.bukkit.entity.Player;
import java.util.Map;

public class NightListener implements Listener {
    private final Map<Player, PlayerFlasks> playerFlasksMap;

    public NightListener(Map<Player, PlayerFlasks> playerFlasksMap) {
        this.playerFlasksMap = playerFlasksMap;
    }

    @EventHandler
    public void onNightEnd(TimeSkipEvent event) {
        // Vérifier si c'est le passage de la nuit au jour
        if (event.getSkipAmount() >= 24000) {
            for (PlayerFlasks playerFlasks : playerFlasksMap.values()) {
                playerFlasks.resetFlasks(); // Vider les fioles
            }
        }
    }
}
