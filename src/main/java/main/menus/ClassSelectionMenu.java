package main.menus;

import main.skills.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ClassSelectionMenu {

    public static void open(Player player) {
        Inventory menu = Bukkit.createInventory(null, 9, "§6§lChoisissez votre classe");

        // Grand Espadon
        ItemStack grandEspadon = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta espadonMeta = grandEspadon.getItemMeta();
        espadonMeta.setDisplayName("§6Grand Espadon");
        espadonMeta.setLore(Arrays.asList(
                "§7Cendre de guerre : Griffe du Lion",
                "§bCoût : 70 Mana",
                "§cDégâts : 3 cœurs (6 points)",
                "§aEffet : Force niveau 1"
        ));
        grandEspadon.setItemMeta(espadonMeta);
        menu.setItem(1, grandEspadon);

        // Nagakiba
        ItemStack nagakiba = new ItemStack(Material.IRON_SWORD);
        ItemMeta nagakibaMeta = nagakiba.getItemMeta();
        nagakibaMeta.setDisplayName("§eNagakiba");
        nagakibaMeta.setLore(Arrays.asList(
                "§7Cendre de guerre : Lame furtive",
                "§bCoût : 50 Mana",
                "§aEffet : Vitesse constante",
                "§dTéléporte derrière l'ennemi"
        ));
        nagakiba.setItemMeta(nagakibaMeta);
        menu.setItem(3, nagakiba);

        // Bâton d'éclat de Lusat
        ItemStack batonLusat = new ItemStack(Material.STICK);
        ItemMeta batonMeta = batonLusat.getItemMeta();
        batonMeta.setDisplayName("§dBâton d'éclat de Lusat");
        batonMeta.setLore(Arrays.asList(
                "§7Sort : Explosion stellaire",
                "§bCoût : 80 Mana",
                "§cExplosion visuelle sans dégâts",
                "§aEffet : Régénération de Mana augmentée"
        ));
        batonLusat.setItemMeta(batonMeta);
        menu.setItem(5, batonLusat);

        // Bouton Fermer
        ItemStack fermer = new ItemStack(Material.BARRIER);
        ItemMeta fermerMeta = fermer.getItemMeta();
        fermerMeta.setDisplayName("§cFermer");
        fermer.setItemMeta(fermerMeta);
        menu.setItem(8, fermer);

        // Ouvrir le menu au joueur
        player.openInventory(menu);
    }
}
