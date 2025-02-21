package main.menus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class BonusSkillMenu {

    public static void open(Player player) {
        Inventory menu = Bukkit.createInventory(null, 9, "§6§lChoisissez une compétence bonus");

        // Flamme noire
        ItemStack flammeNoire = new ItemStack(Material.NETHER_STAR);
        ItemMeta flammeMeta = flammeNoire.getItemMeta();
        flammeMeta.setDisplayName("§4Flamme noire");
        flammeMeta.setLore(Arrays.asList(
                "§7Applique un effet de faiblesse et met les ennemis en feu.",
                "§bCoût : 60 Mana",
                "§cDurée : 2 secondes"
        ));
        flammeNoire.setItemMeta(flammeMeta);
        menu.setItem(1, flammeNoire);

        // Vague de lames
        ItemStack vagueDeLames = new ItemStack(Material.NETHER_STAR);
        ItemMeta vagueMeta = vagueDeLames.getItemMeta();
        vagueMeta.setDisplayName("§eVague de lames");
        vagueMeta.setLore(Arrays.asList(
                "§7Inflige des dégâts dans une zone.",
                "§bCoût : 50 Mana",
                "§cDégâts : 2 cœurs (4 points)"
        ));
        vagueDeLames.setItemMeta(vagueMeta);
        menu.setItem(3, vagueDeLames);

        // Tempête de givre
        ItemStack tempeteDeGivre = new ItemStack(Material.NETHER_STAR);
        ItemMeta tempeteMeta = tempeteDeGivre.getItemMeta();
        tempeteMeta.setDisplayName("§bTempête de givre");
        tempeteMeta.setLore(Arrays.asList(
                "§7Applique un effet de lenteur aux ennemis.",
                "§bCoût : 70 Mana",
                "§cDurée : 5 secondes"
        ));
        tempeteDeGivre.setItemMeta(tempeteMeta);
        menu.setItem(5, tempeteDeGivre);


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