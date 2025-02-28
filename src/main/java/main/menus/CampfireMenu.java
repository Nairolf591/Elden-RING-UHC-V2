// src/main/java/main/menus/CampfireMenu.java
package main.menus;

import main.game.Camp;
import main.game.CampfireData;
import main.game.PlayerData;
import main.game.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class CampfireMenu {

    //Méthode centralisé pour ouvrir les menus
    public static void openCampfireMenu(Player player, CampfireData campfire) {
        // Vérifie si le joueur est dans le camp de la Table Ronde
        PlayerData playerData = PlayerManager.getInstance().getPlayerData(player);

        if(playerData == null) //Important pour éviter les NullPointerException
        {
            Bukkit.getLogger().warning("PlayerData null dans CampfireMenu pour le joueur " + player.getName());
            return;
        }

        //Si il fait partie du camp,
        //CORRECTION ICI: On vérifie si le camp est non null, ET si c'est BASTION_DE_LA_TABLE_RONDE
        if (playerData.getCamp() != null && playerData.getCamp() == Camp.BASTION_DE_LA_TABLE_RONDE) {
            openFlasksMenu(player); //Alors menu des fioles
        } else {
            openChargesMenu(player, campfire); //Sinon menu pour retirer des charges
        }
    }
    private static void openFlasksMenu(Player player) {
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


        // Bouton pour quitter
        ItemStack quitItem = new ItemStack(Material.BARRIER);
        ItemMeta quitMeta = quitItem.getItemMeta();
        quitMeta.setDisplayName("§cQuitter");
        quitItem.setItemMeta(quitMeta);
        menu.setItem(8, quitItem);

        // Ouvrir le menu au joueur
        player.openInventory(menu);
    }

    private static void openChargesMenu(Player player, CampfireData campfire)
    {
        Inventory menu = Bukkit.createInventory(null, 27, "§6Retirer des charges");

        // Boutons pour retirer de 1 à 6 charges
        for (int i = 1; i <= 6; i++) {
            ItemStack chargeItem = new ItemStack(Material.CHARCOAL);
            ItemMeta chargeMeta = chargeItem.getItemMeta();
            chargeMeta.setDisplayName("§eRetirer " + i + " charge(s)");
            chargeItem.setItemMeta(chargeMeta);
            menu.setItem(i + 9, chargeItem); //Pour les positionner au bon endroit
        }

        // Bouton pour annuler
        ItemStack cancelItem = new ItemStack(Material.BARRIER);
        ItemMeta cancelMeta = cancelItem.getItemMeta();
        cancelMeta.setDisplayName("§cAnnuler");
        cancelItem.setItemMeta(cancelMeta);
        menu.setItem(26, cancelItem);

        // Ouvrir le menu au joueur
        player.openInventory(menu);
    }
}
