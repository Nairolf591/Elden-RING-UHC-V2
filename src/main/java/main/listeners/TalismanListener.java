package main.listeners;

import main.game.TalismanEffects;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class TalismanListener implements Listener {

    private final TalismanEffects talismanEffects;

    public TalismanListener(TalismanEffects talismanEffects) {
        this.talismanEffects = talismanEffects;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (talismanEffects.hasTakenFirstHit(player)) {
                event.setDamage(1); // Réduit les dégâts du premier coup à 1 demi cœur
                talismanEffects.setHasTakenFirstHit(player, true);
            }
        }
    }

}
