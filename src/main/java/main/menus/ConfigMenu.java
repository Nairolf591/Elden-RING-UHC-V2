package main.menus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ConfigMenu {

    public static final String MENU_TITLE = "Configuration Avancée";

    public static void open(Player player) {
        // Créer l'inventaire du menu
        Inventory menu = Bukkit.createInventory(null, 9, MENU_TITLE);

        // Ajouter les options au menu
        menu.setItem(0, createMenuItem(Material.CLOCK, "Timer de Démarrage", "Modifier la durée du timer de démarrage."));
        menu.setItem(1, createMenuItem(Material.SUNFLOWER, "Durée du Jour", "Modifier la durée du jour (en minutes)."));
        menu.setItem(2, createMenuItem(Material.CLOCK, "Durée de la Nuit", "Modifier la durée de la nuit (en minutes)."));
        menu.setItem(8, createMenuItem(Material.BARRIER, "Fermer", "Fermer le menu de configuration."));

        // Ouvrir le menu pour le joueur
        player.openInventory(menu);
    }

    private static ItemStack createMenuItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GREEN + name);
            meta.setLore(Arrays.asList(lore));
            item.setItemMeta(meta);
        }
        return item;
    }
}
