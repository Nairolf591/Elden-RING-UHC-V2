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
        menu.setItem(0, createMenuItem(Material.BARRIER, "§c§lRetour", "§7Cliquez pour retourner au menu principal."));

        // Bouton Modifier le stuff
        menu.setItem(4, createMenuItem(Material.ANVIL, "§e§lModifier le Stuff", "§7Cliquez pour modifier le stuff par défaut."));

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
