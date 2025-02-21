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
        // Cendre de guerre : Lame furtive
        if (ManaManager.getInstance().getMana(player) >= 50) {
            ManaManager.getInstance().consumeMana(player, 50);
            // Téléporte le joueur derrière l'ennemi
            player.getWorld().getNearbyEntities(player.getLocation(), 5, 5, 5).forEach(entity -> {
                if (entity instanceof Player) {
                    player.teleport(entity.getLocation().add(entity.getLocation().getDirection().multiply(-2)));
                }
            });
            // Effets visuels et sonores
            player.getWorld().spawnParticle(org.bukkit.Particle.CLOUD, player.getLocation(), 30, 1, 1, 1, 0.1);
            player.getWorld().playSound(player.getLocation(), org.bukkit.Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
        } else {
            player.sendMessage(ChatColor.RED + "Pas assez de Mana !");
        }
    }

    @Override
    public ItemStack getClassItem() {
        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        ItemMeta meta = sword.getItemMeta();
        meta.setDisplayName("§eNagakiba");
        meta.setLore(Arrays.asList("§7Cendre de guerre : Lame furtive"));
        sword.setItemMeta(meta);
        return sword;
    }
}
