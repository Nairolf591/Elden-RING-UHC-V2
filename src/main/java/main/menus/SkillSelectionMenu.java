package main.menus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class SkillSelectionMenu {

    public static void open(Player player) {
        Inventory menu = Bukkit.createInventory(null, 9, "§6§lChoisissez votre compétence");

        // Compétence 1
        ItemStack skill1 = new ItemStack(Material.NETHER_STAR);
        ItemMeta skill1Meta = skill1.getItemMeta();
        skill1Meta.setDisplayName("§aGriffe du Lion");
        skill1Meta.setLore(Arrays.asList("§7Inflige 3 cœurs de dégâts.", "§bCoût : 70 Mana"));
        skill1.setItemMeta(skill1Meta);
        menu.setItem(1, skill1);

        // Compétence 2
        ItemStack skill2 = new ItemStack(Material.NETHER_STAR);
        ItemMeta skill2Meta = skill2.getItemMeta();
        skill2Meta.setDisplayName("§eLame furtive");
        skill2Meta.setLore(Arrays.asList("§7Téléporte derrière l'ennemi.", "§bCoût : 50 Mana"));
        skill2.setItemMeta(skill2Meta);
        menu.setItem(3, skill2);

        // Compétence 3
        ItemStack skill3 = new ItemStack(Material.NETHER_STAR);
        ItemMeta skill3Meta = skill3.getItemMeta();
        skill3Meta.setDisplayName("§dExplosion stellaire");
        skill3Meta.setLore(Arrays.asList("§7Explosion alentour.", "§bCoût : 80 Mana"));
        skill3.setItemMeta(skill3Meta);
        menu.setItem(5, skill3);

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
