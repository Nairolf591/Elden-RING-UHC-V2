package main.menus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class StuffMenu {

    public static final String TITLE = "§6§lConfiguration du Stuff";

    public static void open(Player player) {
        // Créer l'inventaire de menu
        Inventory menu = Bukkit.createInventory(null, 9, TITLE);

        // Bouton Retour
        ItemStack backButton = new ItemStack(Material.BARRIER);
        menu.setItem(7, backButton);

        // Bouton Modifier
        ItemStack editButton = new ItemStack(Material.ANVIL);
        menu.setItem(8, editButton);

        // Ouvrir le menu au joueur
        player.openInventory(menu);
    }
}
