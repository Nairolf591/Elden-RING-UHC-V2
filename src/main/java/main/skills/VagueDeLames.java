package main.skills;

import main.game.ManaManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class VagueDeLames {

    public void useSkill(Player player) {
        if (ManaManager.getInstance().getMana(player) >= 50) {
            ManaManager.getInstance().consumeMana(player, 50);

            // Effet de zone devant le joueur
            Location start = player.getLocation().add(0, 1, 0);
            for (int i = 0; i < 10; i++) {
                Location loc = start.clone().add(player.getLocation().getDirection().multiply(i));

                // Particules
                player.getWorld().spawnParticle(org.bukkit.Particle.SWEEP_ATTACK, loc, 10, 0.2, 0.2, 0.2, 0.1);
                player.getWorld().spawnParticle(org.bukkit.Particle.CRIT, loc, 5, 0.2, 0.2, 0.2, 0.1);
            }

            // Effet de lames volantes avec ArmorStands
            for (int i = 0; i < 3; i++) {
                ArmorStand blade = start.getWorld().spawn(start.clone().add(0, i, 0), ArmorStand.class);
                blade.setVisible(false);
                blade.setGravity(false);
                blade.setSmall(true);
                blade.setCustomNameVisible(false);
                blade.setItemInHand(new ItemStack(Material.IRON_SWORD));

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        blade.remove();
                    }
                }.runTaskLater(Bukkit.getPluginManager().getPlugin("EldenRingUHC"), 20L); // Supprime après 1 seconde
            }

            // Son
            player.getWorld().playSound(start, org.bukkit.Sound.ENTITY_PLAYER_ATTACK_STRONG, 1.0f, 1.0f);

            // Effets sur les ennemis
            player.getWorld().getNearbyEntities(start, 5, 5, 5).forEach(entity -> {
                if (entity instanceof Player && !entity.equals(player)) {
                    ((Player) entity).damage(4, player); // 4 dégâts = 2 cœurs
                }
            });
        } else {
            player.sendMessage(ChatColor.RED + "Pas assez de Mana !");
        }
    }


}
