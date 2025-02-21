package main.skills;

import main.game.ManaManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TempeteDeGivre {

    public void useSkill(Player player) {
        if (ManaManager.getInstance().getMana(player) >= 70) {
            ManaManager.getInstance().consumeMana(player, 70);
            // Applique un effet de lenteur aux ennemis proches
            player.getWorld().getNearbyEntities(player.getLocation(), 5, 5, 5).forEach(entity -> {
                if (entity instanceof Player) {
                    ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
                }
            });
            // Effets visuels et sonores
            player.getWorld().spawnParticle(org.bukkit.Particle.SNOW_SHOVEL, player.getLocation(), 30, 1, 1, 1, 0.1);
            player.getWorld().playSound(player.getLocation(), org.bukkit.Sound.ENTITY_SNOW_GOLEM_SHOOT, 1.0f, 1.0f);
        } else {
            player.sendMessage(ChatColor.RED + "Pas assez de Mana !");
        }
    }
}
