package main.skills;

import main.game.ManaManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FlammeNoire {

    public void useSkill(Player player) {
        if (ManaManager.getInstance().getMana(player) >= 60) {
            ManaManager.getInstance().consumeMana(player, 60);
            // Applique un effet de faiblesse aux ennemis proches
            player.getWorld().getNearbyEntities(player.getLocation(), 5, 5, 5).forEach(entity -> {
                if (entity instanceof Player) {
                    ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 1));
                }
            });
            // Effets visuels et sonores
            player.getWorld().spawnParticle(org.bukkit.Particle.SMOKE_LARGE, player.getLocation(), 50, 2, 2, 2, 0.1); // Plus de fumée
            player.getWorld().spawnParticle(org.bukkit.Particle.FLAME, player.getLocation(), 50, 2, 2, 2, 0.1); // Ajout de flammes
            player.getWorld().playSound(player.getLocation(), org.bukkit.Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.0f);
        } else {
            player.sendMessage(ChatColor.RED + "Pas assez de Mana !");
        }
    }

}
