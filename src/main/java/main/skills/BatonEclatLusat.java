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
            // Créer une explosion visuelle sans dégâts
            player.getWorld().spawnParticle(org.bukkit.Particle.EXPLOSION_HUGE, player.getLocation(), 1); // Grosse explosion
            player.getWorld().spawnParticle(org.bukkit.Particle.FLAME, player.getLocation(), 100, 2, 2, 2, 0.1); // Flux de flammes
            player.getWorld().playSound(player.getLocation(), org.bukkit.Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
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
