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
        World world =  Bukkit.getWorld("UHC");
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
    private static void updateMenu(Player player) {
        World world = Bukkit.getWorld("UHC");
        double borderSize = world.getWorldBorder().getSize();
        double borderDamage = world.getWorldBorder().getDamageAmount();

        // Créer l'inventaire de menu
        Inventory menu = Bukkit.createInventory(null, 9, TITLE);

        // Boutons pour modifier la bordure
        menu.setItem(1, createMenuItem(Material.ARROW, "§a§lAugmenter la bordure", "§7Cliquez pour augmenter la taille de la bordure."));
        menu.setItem(3, createMenuItem(Material.SPECTRAL_ARROW, "§c§lRéduire la bordure", "§7Cliquez pour réduire la taille de la bordure."));

        // Info sur la bordure
        menu.setItem(2, createMenuItem(Material.BEACON, "§6§lTaille Actuelle", "§7Taille : " + String.format("%.2f", borderSize) + " blocs."));

        // Boutons pour modifier les dégâts
        menu.setItem(5, createMenuItem(Material.TNT, "§4§lAugmenter les dégâts", "§7Cliquez pour augmenter les dégâts de la bordure."));
        menu.setItem(6, createMenuItem(Material.FEATHER, "§f§lRéduire les dégâts", "§7Cliquez pour réduire les dégâts de la bordure."));

        // Info sur les dégâts
        menu.setItem(6, createMenuItem(Material.REDSTONE, "§c§lDégâts Actuels", "§7Dégâts : " + String.format("%.2f", borderDamage) + " par seconde."));

        // Bouton Retour
        menu.setItem(8, createMenuItem(Material.BARRIER, "§c§lRetour", "§7Cliquez pour retourner au menu principal."));

        // Ouvrir le menu au joueur
        player.openInventory(menu);
    }

    private static ItemStack createMenuItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList(lore));
            item.setItemMeta(meta);
        }
        return item;
    }
}
