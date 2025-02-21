package main.skills;

import main.main;
import main.game.ManaManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class Nagakiba extends PlayerClass {

    @Override
    public void applyClass(Player player) {
        // Effet permanent : Vitesse constante
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
        // Donner l'arme de la classe
        player.getInventory().addItem(getClassItem());
    }

    public void useSkill(Player player) {
        if (ManaManager.getInstance().getMana(player) >= 50) {
            ManaManager.getInstance().consumeMana(player, 50);

            // Téléportation derrière l'ennemi
            player.getWorld().getNearbyEntities(player.getLocation(), 5, 5, 5).forEach(entity -> {
                if (entity instanceof Player && !entity.equals(player)) {
                    Location behindTarget = entity.getLocation().add(entity.getLocation().getDirection().multiply(-2));
                    player.teleport(behindTarget);

                    // Effet de flou avec des clones
                    for (int i = 0; i < 3; i++) {
                        ArmorStand clone = player.getWorld().spawn(behindTarget, ArmorStand.class);
                        clone.setVisible(false);
                        clone.setGravity(false);
                        clone.setSmall(true);
                        clone.setArms(true);
                        clone.setItemInHand(new ItemStack(Material.IRON_SWORD));

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                clone.remove();
                            }
                        }.runTaskLater(Bukkit.getPluginManager().getPlugin("EldenRingUHC"), 20L); // Supprime le clone après 1 seconde
                    }
                }
            });

            // Particules et son pour la téléportation
            player.getWorld().spawnParticle(org.bukkit.Particle.CLOUD, player.getLocation(), 50, 1, 1, 1, 0.1);
            player.getWorld().spawnParticle(org.bukkit.Particle.SMOKE_LARGE, player.getLocation(), 30, 1, 1, 1, 0.1);
            player.getWorld().playSound(player.getLocation(), org.bukkit.Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
        } else {
            player.sendMessage(ChatColor.RED + "Pas assez de Mana !");
        }
    }



    @Override
    public ItemStack getClassItem() {
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = sword.getItemMeta();
        meta.setDisplayName("§eNagakiba");
        meta.setLore(Arrays.asList("§7Cendre de guerre : Lame furtive"));
        meta.addEnchant(org.bukkit.enchantments.Enchantment.DAMAGE_ALL, 3, true); // Sharpness 3
        sword.setItemMeta(meta);
        return sword;
    }
}
