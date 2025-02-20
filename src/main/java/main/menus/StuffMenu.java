package main.menus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class StuffMenu {

    public static final String TITLE = "§6§lConfiguration du Stuff";

    public static void open(Player player) {
        // Créer l'inventaire de menu
        Inventory menu = Bukkit.createInventory(null, 9, TITLE);

        // Bouton Retour
        ItemStack backButton = new ItemStack(Material.BARRIER);
        ItemMeta backButtonMeta = backButton.getItemMeta();
        backButtonMeta.setDisplayName("§c§lRetour");
        backButtonMeta.setLore(Arrays.asList("§7Cliquez pour retourner au menu principal."));
        backButton.setItemMeta(backButtonMeta);
        menu.setItem(0, backButton);

        // Bouton Modifier le stuff
        ItemStack editButton = new ItemStack(Material.ANVIL);
        ItemMeta editButtonMeta = editButton.getItemMeta();
        editButtonMeta.setDisplayName("§e§lModifier le Stuff");
        editButtonMeta.setLore(Arrays.asList("§7Cliquez pour modifier le stuff par défaut."));
        editButton.setItemMeta(editButtonMeta);
        menu.setItem(4, editButton);

        // Ouvrir le menu au joueur
        player.openInventory(menu);
    }
}
