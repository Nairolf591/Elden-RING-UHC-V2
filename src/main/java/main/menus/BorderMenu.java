package main.menus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class BorderMenu {

    public static final String TITLE = "§6§lConfigurer la bordure";

    public static void open(Player player) {
        // Créer l'inventaire de menu
        Inventory menu = Bukkit.createInventory(null, 9, TITLE);

        // Bouton Augmenter la bordure
        ItemStack increaseBorder = new ItemStack(Material.ARROW);
        ItemMeta increaseBorderMeta = increaseBorder.getItemMeta();
        increaseBorderMeta.setDisplayName("§a§lAugmenter la bordure");
        increaseBorderMeta.setLore(Arrays.asList("§7Cliquez pour augmenter la taille de la bordure."));
        increaseBorder.setItemMeta(increaseBorderMeta);
        menu.setItem(2, increaseBorder);

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

        // Bouton Réduire les dégâts de la bordure
        ItemStack decreaseDamage = new ItemStack(Material.FEATHER);
        ItemMeta decreaseDamageMeta = decreaseDamage.getItemMeta();
        decreaseDamageMeta.setDisplayName("§f§lRéduire les dégâts");
        decreaseDamageMeta.setLore(Arrays.asList("§7Cliquez pour réduire les dégâts de la bordure."));
        decreaseDamage.setItemMeta(decreaseDamageMeta);
        menu.setItem(6, decreaseDamage);

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
