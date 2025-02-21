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

public class Nagakiba extends PlayerClass {

    @Override
    public void applyClass(Player player) {
        // Effet permanent : Vitesse constante
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
        // Donner l'arme de la classe
        player.getInventory().addItem(getClassItem());
    }

    @Override
    public void useSkill(Player player) {
        if (ManaManager.getInstance().getMana(player) >= 50) {
            ManaManager.getInstance().consumeMana(player, 50);
            // Téléporte le joueur derrière l'ennemi
            player.getWorld().getNearbyEntities(player.getLocation(), 5, 5, 5).forEach(entity -> {
                if (entity instanceof Player) {
                    player.teleport(entity.getLocation().add(entity.getLocation().getDirection().multiply(-2)));
                }
            });
            // Effets visuels et sonores
            player.getWorld().spawnParticle(org.bukkit.Particle.CLOUD, player.getLocation(), 100, 2, 2, 2, 0.2); // Plus de particules
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
