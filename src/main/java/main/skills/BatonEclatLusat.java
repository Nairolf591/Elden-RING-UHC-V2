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

public class BatonEclatLusat extends PlayerClass {

    @Override
    public void applyClass(Player player) {
        // Effet permanent : Régénération de Mana augmentée
        ManaManager.getInstance().setMana(player, 150); // Augmente le Mana maximum à 150
        // Donner l'arme de la classe
        player.getInventory().addItem(getClassItem());
    }

    @Override
    public void useSkill(Player player) {
        if (ManaManager.getInstance().getMana(player) >= 80) {
            ManaManager.getInstance().consumeMana(player, 80);

            // Effets visuels
            Location center = player.getLocation();
            for (int i = 0; i < 360; i += 10) {
                double angle = Math.toRadians(i);
                double x = Math.cos(angle) * 3; // Rayon de l'explosion
                double z = Math.sin(angle) * 3;
                Location loc = center.clone().add(x, 1, z);

                // Particules cosmiques
                player.getWorld().spawnParticle(org.bukkit.Particle.FIREWORKS_SPARK, loc, 10, 0.2, 0.2, 0.2, 0.1);
                player.getWorld().spawnParticle(org.bukkit.Particle.ENCHANTMENT_TABLE, loc, 5, 0.2, 0.2, 0.2, 0.1);
                player.getWorld().spawnParticle(org.bukkit.Particle.SOUL_FIRE_FLAME, loc, 3, 0.2, 0.2, 0.2, 0.1);
                player.getWorld().spawnParticle(org.bukkit.Particle.REDSTONE, loc, 5, 0.2, 0.2, 0.2, 0.1, new org.bukkit.Particle.DustOptions(Color.PURPLE, 2));
            }

            // Effet d'onde de choc
            for (int i = 0; i < 5; i++) {
                Location waveLoc = center.clone().add(0, i * 0.5, 0);
                player.getWorld().spawnParticle(org.bukkit.Particle.EXPLOSION_HUGE, waveLoc, 1); // Effet d'onde
            }

            // Son de l'explosion
            player.getWorld().playSound(player.getLocation(), org.bukkit.Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
            player.getWorld().playSound(player.getLocation(), org.bukkit.Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0f, 1.0f);

            // Dégâts aux ennemis proches
            player.getWorld().getNearbyEntities(player.getLocation(), 5, 5, 5).forEach(entity -> {
                if (entity instanceof Player && !entity.equals(player)) {
                    ((Player) entity).damage(8, player); // 8 dégâts = 4 cœurs
                }
            });
        } else {
            player.sendMessage(ChatColor.RED + "Pas assez de Mana !");
        }
    }



    @Override
    public ItemStack getClassItem() {
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = sword.getItemMeta();
        meta.setDisplayName("§dBâton d'éclat de Lusat");
        meta.setLore(Arrays.asList("§7Sort : Explosion stellaire"));
        meta.addEnchant(org.bukkit.enchantments.Enchantment.DAMAGE_ALL, 3, true); // Sharpness 3
        sword.setItemMeta(meta);
        return sword;
    }
}
