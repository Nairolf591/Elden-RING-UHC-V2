package main.skills;

import main.game.ManaManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class VagueDeLames {

    public void useSkill(Player player) {
        if (ManaManager.getInstance().getMana(player) >= 50) {
            ManaManager.getInstance().consumeMana(player, 50);
            // Inflige des dégâts dans une zone
            player.getWorld().getNearbyEntities(player.getLocation(), 5, 5, 5).forEach(entity -> {
                if (entity instanceof Player) {
                    ((Player) entity).damage(4, player); // 4 dégâts = 2 cœurs
                }
            });
            // Effets visuels et sonores
            player.getWorld().spawnParticle(org.bukkit.Particle.SWEEP_ATTACK, player.getLocation(), 50, 2, 2, 2, 0.1); // Particules de coupure
            player.getWorld().spawnParticle(org.bukkit.Particle.CRIT, player.getLocation(), 50, 2, 2, 2, 0.1); // Particules de dégâts
            player.getWorld().playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f);
        } else {
            player.sendMessage(ChatColor.RED + "Pas assez de Mana !");
        }
    }

}
