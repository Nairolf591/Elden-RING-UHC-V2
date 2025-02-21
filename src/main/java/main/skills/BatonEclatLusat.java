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
        // Sort : Explosion stellaire
        if (ManaManager.getInstance().getMana(player) >= 80) {
            ManaManager.getInstance().consumeMana(player, 80);
            // Explosion qui inflige des dégâts
            player.getWorld().createExplosion(player.getLocation(), 3, false);
            // Effets visuels et sonores
            player.getWorld().spawnParticle(org.bukkit.Particle.FLAME, player.getLocation(), 50, 1, 1, 1, 0.1);
            player.getWorld().playSound(player.getLocation(), org.bukkit.Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
        } else {
            player.sendMessage(ChatColor.RED + "Pas assez de Mana !");
        }
    }

    @Override
    public ItemStack getClassItem() {
        ItemStack stick = new ItemStack(Material.STICK);
        ItemMeta meta = stick.getItemMeta();
        meta.setDisplayName("§dBâton d'éclat de Lusat");
        meta.setLore(Arrays.asList("§7Sort : Explosion stellaire"));
        stick.setItemMeta(meta);
        return stick;
    }
}
