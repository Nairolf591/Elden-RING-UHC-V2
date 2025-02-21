package main.skills;

import main.game.ManaManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class FlammeNoire {

    public void useSkill(Player player) {
        if (ManaManager.getInstance().getMana(player) >= 60) {
            ManaManager.getInstance().consumeMana(player, 60);

            // Effet de zone autour du joueur
            Location center = player.getLocation();
            for (int i = 0; i < 360; i += 10) {
                double angle = Math.toRadians(i);
                double x = Math.cos(angle) * 3; // Rayon de l'explosion
                double z = Math.sin(angle) * 3;
                Location loc = center.clone().add(x, 0, z);

                // Particules
                player.getWorld().spawnParticle(org.bukkit.Particle.SOUL_FIRE_FLAME, loc, 10, 0.2, 0.2, 0.2, 0.1);
                player.getWorld().spawnParticle(org.bukkit.Particle.SMOKE_LARGE, loc, 5, 0.2, 0.2, 0.2, 0.1);
            }

            // Effet de flammes tourbillonnantes avec ArmorStands
            for (int i = 0; i < 5; i++) {
                ArmorStand flame = center.getWorld().spawn(center.clone().add(0, i, 0), ArmorStand.class);
                flame.setVisible(false);
                flame.setGravity(false);
                flame.setSmall(true);
                flame.setCustomNameVisible(false);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        flame.remove();
                    }
                }.runTaskLater(Bukkit.getPluginManager().getPlugin("EldenRingUHC"), 20L); // Supprime après 1 seconde
            }

            // Son
            player.getWorld().playSound(center, org.bukkit.Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
            player.getWorld().playSound(center, org.bukkit.Sound.BLOCK_FIRE_EXTINGUISH, 1.0f, 1.0f);

            // Effets sur les ennemis
            player.getWorld().getNearbyEntities(center, 5, 5, 5).forEach(entity -> {
                if (entity instanceof Player && !entity.equals(player)) {
                    ((Player) entity).setFireTicks(100); // Brûle pendant 5 secondes
                    ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 1)); // Faiblesse
                }
            });
        } else {
            player.sendMessage(ChatColor.RED + "Pas assez de Mana !");
        }
    }
}
