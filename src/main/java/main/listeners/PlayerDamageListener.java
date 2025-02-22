package main.listeners;

import main.game.TalismanEffects;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDamageListener implements Listener {

    private final TalismanEffects talismanEffects;

    public PlayerDamageListener(TalismanEffects talismanEffects) {
        this.talismanEffects = talismanEffects;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            // Vérifie si le joueur possède le talisman "Chat à longue queue"
            boolean hasLongTailCat = false;
            for (ItemStack item : player.getInventory().getContents()) {
                if (item != null && item.getItemMeta() != null && item.getItemMeta().getDisplayName() != null) {
                    if (item.getItemMeta().getDisplayName().equals("Talisman du chat à longue queue")) {
                        hasLongTailCat = true;
                        break;
                    }
                }
            }

            // Annule les dégâts de chute si le joueur possède le talisman
            if (hasLongTailCat && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                event.setCancelled(true);
            }
        }
    }
}
