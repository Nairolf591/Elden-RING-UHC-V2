package main.skills;

import main.game.ManaManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FlammeNoire {

    public void useSkill(Player player) {
        if (ManaManager.getInstance().getMana(player) >= 60) {
            ManaManager.getInstance().consumeMana(player, 60);

            // Applique un effet de faiblesse et met les joueurs en feu
            for (Entity entity : player.getWorld().getNearbyEntities(player.getLocation(), 5, 5, 5)) {
                if (entity instanceof Player && !entity.equals(player)) { // Ne pas affecter le joueur qui utilise la compétence
                    Player target = (Player) entity;
                    target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 40, 1)); // 40 ticks = 2 secondes
                    target.setFireTicks(40); // Met en feu pendant 2 secondes (40 ticks)
                }
            }

            // Effets visuels et sonores
            player.getWorld().spawnParticle(org.bukkit.Particle.SMOKE_LARGE, player.getLocation(), 50, 2, 2, 2, 0.1);
            player.getWorld().spawnParticle(org.bukkit.Particle.FLAME, player.getLocation(), 50, 2, 2, 2, 0.1);
            player.getWorld().playSound(player.getLocation(), org.bukkit.Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.0f);
        } else {
            player.sendMessage(ChatColor.RED + "Pas assez de Mana !");
        }
    }

}
