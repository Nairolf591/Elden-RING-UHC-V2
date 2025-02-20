package main.menus;

import main.game.StuffManager;
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
        Inventory menu = Bukkit.createInventory(null, 9 * 3, TITLE); // 3 lignes

        // Bouton Retour
        ItemStack returnItem = createMenuItem(Material.BARRIER, "§c§lRetour", "§7Cliquez pour retourner au menu principal.");
        menu.setItem(0, returnItem);

        // Afficher le stuff actuel
        StuffManager stuffManager = StuffManager.getInstance();
        ItemStack[] defaultStuff = stuffManager.getDefaultStuff();
        // Limiter la boucle à la taille de defaultStuff (36 emplacements) et à la taille du menu (27 emplacements)
        for (int i = 0; i < defaultStuff.length && i < 26; i++) { // 27 emplacements dans le menu
            if (defaultStuff[i] != null) {
                menu.setItem(i + 1, defaultStuff[i]); // Commencer à partir de l'emplacement 1 pour éviter d'écraser le bouton Retour
            }
        }

        // Bouton Modifier le stuff
        ItemStack modifyItem = createMenuItem(Material.ANVIL, "§e§lModifier le Stuff", "§7Cliquez pour modifier le stuff par défaut.");
        menu.setItem(26, modifyItem); // Place le bouton en bas à droite

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
