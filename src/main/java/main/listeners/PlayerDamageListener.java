package main.listeners;

import main.game.PlayerManager;
import main.game.Role;
import main.game.TalismanEffects;
import main.role.Melina;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;

public class PlayerDamageListener implements Listener {

    private final TalismanEffects talismanEffects;
    private final JavaPlugin plugin; // Ajout du champ plugin

    public PlayerDamageListener(TalismanEffects talismanEffects, JavaPlugin plugin) { // Ajout du paramètre plugin
        this.talismanEffects = talismanEffects;
        this.plugin = plugin;
    }
    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) { //Changement de event
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
            // Vérifier si l'attaquant est Melina et la cible marquée
            // Vérifier si le damager est une flèche

            if (event.getDamager() instanceof Arrow) {
                Arrow arrow = (Arrow) event.getDamager();
                ProjectileSource shooter = arrow.getShooter();

                // Vérifier si le tireur est Melina
                if (shooter instanceof Player && PlayerManager.getInstance().getPlayerRole((Player) shooter) == Role.MELINA) {
                    if (event.getEntity() instanceof LivingEntity)
                    {
                        LivingEntity target = (LivingEntity) event.getEntity();
                        if (Melina.isMarked(target, plugin)) {
                            double finalDamage = event.getFinalDamage() * 1.10;
                            event.setDamage(finalDamage);
                            target.removeMetadata("MarqueSpectrale", plugin);
                        }
                    }

                }
            }
            else if (event.getDamager() instanceof Player) //Si c'est un joueur
            {
                Player attacker = (Player) event.getDamager();
                //On vérifie que son rôle est bien Melina
                if (PlayerManager.getInstance().getPlayerRole(attacker) == Role.MELINA && event.getEntity() instanceof LivingEntity) {
                    LivingEntity target = (LivingEntity) event.getEntity();
                    if (Melina.isMarked(target, plugin)) { // Utilisation correcte de isMarked
                        // Appliquer les dégâts supplémentaires (10%)
                        double finalDamage = event.getFinalDamage() * 1.10; // Augmentation de 10%, on utilise FinalDamage
                        event.setDamage(finalDamage);

                        // On retire la marque après avoir infligé les dégâts.  Ici, c'est *après* avoir appliqué le bonus.
                        target.removeMetadata("MarqueSpectrale", plugin);
                    }
                }
            }
        }
    }
}
