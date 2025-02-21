package main.skills;

import main.game.ManaManager;
import org.bukkit.ChatColor;
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

    @Override
    public void useSkill(Player player) {
        if (ManaManager.getInstance().getMana(player) >= 70) {
            ManaManager.getInstance().consumeMana(player, 70);

            // Inflige 3 cœurs de dégâts aux ennemis dans une zone
            player.getWorld().getNearbyEntities(player.getLocation(), 5, 5, 5).forEach(entity -> {
                if (entity instanceof Player && !entity.equals(player)) {
                    ((Player) entity).damage(6, player); // 6 dégâts = 3 cœurs
                }
            });

            // Effets visuels et sonores
            player.getWorld().spawnParticle(org.bukkit.Particle.CRIT, player.getLocation(), 30, 1, 1, 1, 0.1);
            player.getWorld().spawnParticle(org.bukkit.Particle.SWEEP_ATTACK, player.getLocation(), 1); // Particule de coupure
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
