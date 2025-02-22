package main.listeners;

import main.game.TalismanEffects;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    private final TalismanEffects talismanEffects;

    public PlayerInteractListener(TalismanEffects talismanEffects) {
        this.talismanEffects = talismanEffects;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        talismanEffects.incrementHitCount(player); // Incrémente le nombre de coups
    }
}
