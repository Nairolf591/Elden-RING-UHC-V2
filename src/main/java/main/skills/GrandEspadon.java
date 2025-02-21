package main.skills;

import main.game.ManaManager;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class GrandEspadon extends PlayerClass {

    @Override
    public void applyClass(Player player) {
        // Effet permanent : Force niveau 1
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0));
        // Donner l'arme de la classe
        player.getInventory().addItem(getClassItem());
    }

    public void useSkill(Player player) {
        if (ManaManager.getInstance().getMana(player) >= 70) {
            ManaManager.getInstance().consumeMana(player, 70);

            // Dégâts aux ennemis proches
            player.getWorld().getNearbyEntities(player.getLocation(), 5, 5, 5).forEach(entity -> {
                if (entity instanceof Player && !entity.equals(player)) {
                    ((Player) entity).damage(6, player); // 6 dégâts = 3 cœurs
                }
            });

            // Particules pour l'animation
            Location start = player.getLocation().add(0, 1, 0); // Position de départ
            for (int i = 0; i < 10; i++) {
                // Ligne de particules en forme de griffes
                double angle = Math.toRadians(i * 36); // 360 degrés divisés en 10 points
                double x = Math.cos(angle) * 2;
                double z = Math.sin(angle) * 2;
                Location loc = start.clone().add(x, 0, z);

                // Particules rouges pour les griffes
                player.getWorld().spawnParticle(org.bukkit.Particle.REDSTONE, loc, 10, 0.2, 0.2, 0.2, 0, new org.bukkit.Particle.DustOptions(Color.RED, 2));

                // Particules d'énergie
                player.getWorld().spawnParticle(org.bukkit.Particle.CRIT, loc, 5, 0.2, 0.2, 0.2, 0.1);
            }

            // Son de l'attaque
            player.getWorld().playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f);
        } else {
            player.sendMessage(ChatColor.RED + "Pas assez de Mana !");
        }
    }


    @Override
    public ItemStack getClassItem() {
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = sword.getItemMeta();
        meta.setDisplayName("§6Grand Espadon");
        meta.setLore(Arrays.asList(
                "§7Cendre de guerre : Griffe du Lion",
                "§bCoût : 70 Mana",
                "§cDégâts : 3 cœurs (6 points)",
                "§aEffet : Force niveau 1"
        ));
        meta.addEnchant(org.bukkit.enchantments.Enchantment.DAMAGE_ALL, 3, true); // Sharpness 3
        sword.setItemMeta(meta);
        return sword;
    }
}
