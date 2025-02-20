package main.menus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class BorderMenu {

    public static final String TITLE = "§6§lConfigurer la bordure";

    public static void open(Player player) {
        World world = player.getWorld();
        double borderSize = world.getWorldBorder().getSize();
        double borderDamage = world.getWorldBorder().getDamageAmount();

        // Créer l'inventaire de menu
        Inventory menu = Bukkit.createInventory(null, 9, TITLE);

        // Bouton Augmenter la bordure
        ItemStack increaseBorder = new ItemStack(Material.ARROW);
        ItemMeta increaseBorderMeta = increaseBorder.getItemMeta();
        increaseBorderMeta.setDisplayName("§a§lAugmenter la bordure");
        increaseBorderMeta.setLore(Arrays.asList("§7Cliquez pour augmenter la taille de la bordure."));
        increaseBorder.setItemMeta(increaseBorderMeta);
        menu.setItem(1, increaseBorder);

        // Item pour afficher la taille actuelle de la bordure
        ItemStack borderInfo = new ItemStack(Material.BEACON);
        ItemMeta borderInfoMeta = borderInfo.getItemMeta();
        borderInfoMeta.setDisplayName("§6§lTaille Actuelle");
        borderInfoMeta.setLore(Arrays.asList("§7Taille : " + String.format("%.2f", borderSize) + " blocs."));
        borderInfo.setItemMeta(borderInfoMeta);
        menu.setItem(2, borderInfo);

        // Bouton Réduire la bordure
        ItemStack decreaseBorder = new ItemStack(Material.SPECTRAL_ARROW);
        ItemMeta decreaseBorderMeta = decreaseBorder.getItemMeta();
        decreaseBorderMeta.setDisplayName("§c§lRéduire la bordure");
        decreaseBorderMeta.setLore(Arrays.asList("§7Cliquez pour réduire la taille de la bordure."));
        decreaseBorder.setItemMeta(decreaseBorderMeta);
        menu.setItem(3, decreaseBorder);

        // Bouton Augmenter les dégâts de la bordure
        ItemStack increaseDamage = new ItemStack(Material.TNT);
        ItemMeta increaseDamageMeta = increaseDamage.getItemMeta();
        increaseDamageMeta.setDisplayName("§4§lAugmenter les dégâts");
        increaseDamageMeta.setLore(Arrays.asList("§7Cliquez pour augmenter les dégâts de la bordure."));
        increaseDamage.setItemMeta(increaseDamageMeta);
        menu.setItem(5, increaseDamage);

        // Item pour afficher les dégâts actuels de la bordure
        ItemStack damageInfo = new ItemStack(Material.REDSTONE);
        ItemMeta damageInfoMeta = damageInfo.getItemMeta();
        damageInfoMeta.setDisplayName("§c§lDégâts Actuels");
        damageInfoMeta.setLore(Arrays.asList("§7Dégâts : " + String.format("%.2f", borderDamage) + " par seconde."));
        damageInfo.setItemMeta(damageInfoMeta);
        menu.setItem(6, damageInfo);

        // Bouton Retour au menu principal
        ItemStack backButton = new ItemStack(Material.BARRIER);
        ItemMeta backButtonMeta = backButton.getItemMeta();
        backButtonMeta.setDisplayName("§c§lRetour");
        backButtonMeta.setLore(Arrays.asList("§7Cliquez pour retourner au menu principal."));
        backButton.setItemMeta(backButtonMeta);
        menu.setItem(8, backButton);

        // Ouvrir le menu au joueur
        player.openInventory(menu);

        // Jouer un son
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
    }
}
