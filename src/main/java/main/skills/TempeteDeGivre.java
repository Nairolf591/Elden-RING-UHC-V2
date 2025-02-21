package main.skills;

import main.game.ManaManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class TempeteDeGivre {

    public void useSkill(Player player) {
        if (ManaManager.getInstance().getMana(player) >= 70) {
            ManaManager.getInstance().consumeMana(player, 70);

            // Effet de zone autour du joueur
            Location center = player.getLocation();
            for (int i = 0; i < 360; i += 10) {
                double angle = Math.toRadians(i);
                double x = Math.cos(angle) * 3; // Rayon de la tempête
                double z = Math.sin(angle) * 3;
                Location loc = center.clone().add(x, 0, z);

                // Particules
                player.getWorld().spawnParticle(org.bukkit.Particle.SNOW_SHOVEL, loc, 10, 0.2, 0.2, 0.2, 0.1);
                player.getWorld().spawnParticle(org.bukkit.Particle.SOUL_FIRE_FLAME, loc, 5, 0.2, 0.2, 0.2, 0.1);
            }

            // Son
            player.getWorld().playSound(center, org.bukkit.Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0f, 1.0f);

            // Effets sur les ennemis
            player.getWorld().getNearbyEntities(center, 5, 5, 5).forEach(entity -> {
                if (entity instanceof Player && !entity.equals(player)) {
                    ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2)); // Lenteur
                }
            });

            // Blocs de glace temporaires
            Block block = center.getBlock();
            block.setType(Material.ICE);
            new BukkitRunnable() {
                @Override
                public void run() {
                    block.setType(Material.AIR); // Retire la glace après 3 secondes
                }
            }.runTaskLater(Bukkit.getPluginManager().getPlugin("EldenRingUHC"), 60L);
        } else {
            player.sendMessage(ChatColor.RED + "Pas assez de Mana !");
        }
    }


}
