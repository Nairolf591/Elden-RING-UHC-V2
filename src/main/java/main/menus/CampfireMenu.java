package main.menus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class CampfireMenu {

    public static void openCampfireMenu(Player player) {
        // Crée un inventaire de menu
        Inventory menu = Bukkit.createInventory(null, 9, "§6Feu de Camp");

        // Bouton pour les fioles d'Estus
        ItemStack estusItem = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta estusMeta = estusItem.getItemMeta();
        estusMeta.setDisplayName("§cFiole d'Estus");
        estusMeta.setLore(Arrays.asList(
                "§7Soigne 1,5 cœurs.",
                "§b(Utilise 1 charge)"
        ));
        estusItem.setItemMeta(estusMeta);
        menu.setItem(1, estusItem);

        // Bouton pour les fioles de Mana
        ItemStack manaItem = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
        ItemMeta manaMeta = manaItem.getItemMeta();
        manaMeta.setDisplayName("§bFiole de Mana");
        manaMeta.setLore(Arrays.asList(
                "§7Restaure 90 de mana.",
                "§b(Utilise 1 charge)"
        ));
        manaItem.setItemMeta(manaMeta);
        menu.setItem(3, manaItem);

        // Bouton pour les deux fioles
        ItemStack bothItem = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE);
        ItemMeta bothMeta = bothItem.getItemMeta();
        bothMeta.setDisplayName("§dLes deux fioles");
        bothMeta.setLore(Arrays.asList(
                "§71 Fiole d'Estus + 1 Fiole de Mana.",
                "§b(Utilise 2 charges)"
        ));
        bothItem.setItemMeta(bothMeta);
        menu.setItem(5, bothItem);

        // Bouton pour quitter
        ItemStack quitItem = new ItemStack(Material.BARRIER);
        ItemMeta quitMeta = quitItem.getItemMeta();
        quitMeta.setDisplayName("§cQuitter");
        quitItem.setItemMeta(quitMeta);
        menu.setItem(8, quitItem);

        // Ouvrir le menu au joueur
        player.openInventory(menu);
    }

    public static void openCampfireMenuForSoloOrDemigod(Player player) {
        // Crée un inventaire de menu spécial pour les demi-dieux et solitaires
        Inventory menu = Bukkit.createInventory(null, 9, "§6Feu de Camp");

        // Bouton pour retirer des charges
        ItemStack chargeItem = new ItemStack(Material.CAMPFIRE);
        ItemMeta chargeMeta = chargeItem.getItemMeta();
        chargeMeta.setDisplayName("§6Retirer des charges");
        chargeMeta.setLore(Arrays.asList(
                "§7Retirez toutes les charges ou seulement une partie.",
                "§bCliquez pour choisir."
        ));
        chargeItem.setItemMeta(chargeMeta);
        menu.setItem(4, chargeItem);

        // Bouton pour quitter
        ItemStack quitItem = new ItemStack(Material.BARRIER);
        ItemMeta quitMeta = quitItem.getItemMeta();
        quitMeta.setDisplayName("§cQuitter");
        quitItem.setItemMeta(quitMeta);
        menu.setItem(8, quitItem);

        // Ouvrir le menu au joueur
        player.openInventory(menu);
    }
}
