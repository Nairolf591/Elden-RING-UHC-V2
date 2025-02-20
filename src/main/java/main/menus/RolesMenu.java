package main.menus;

import main.game.RoleManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import main.game.Camp;
import main.game.Role;

import java.util.Arrays;

public class RolesMenu {

    public static final String TITLE = "§6§lConfiguration des Rôles";

    public static void open(Player player) {
        // Créer l'inventaire de menu
        Inventory menu = Bukkit.createInventory(null, 9, TITLE);

        // Bouton pour Le Sans-Éclat
        ItemStack sansEclat = new ItemStack(Material.IRON_SWORD);
        ItemMeta sansEclatMeta = sansEclat.getItemMeta();
        sansEclatMeta.setDisplayName("§aLe Sans-Éclat");
        sansEclatMeta.setLore(Arrays.asList(
                "§6Camp: " + Camp.BASTION_DE_LA_TABLE_RONDE.getColor() + Camp.BASTION_DE_LA_TABLE_RONDE.getName(),
                "§7Un humble chevalier sans gloire.",
                RoleManager.getInstance().isRoleEnabled(Role.SANS_ECLAT) ? "§aActivé" : "§cDésactivé",
                "§eClique pour activer/désactiver ce rôle."
        ));
        sansEclat.setItemMeta(sansEclatMeta);
        menu.setItem(1, sansEclat);

        // Bouton pour Radahn
        ItemStack radahn = new ItemStack(Material.BOW);
        ItemMeta radahnMeta = radahn.getItemMeta();
        radahnMeta.setDisplayName("§6Radahn");
        radahnMeta.setLore(Arrays.asList(
                "§6Camp: " + Camp.SOLITAIRE.getColor() + Camp.SOLITAIRE.getName(),
                "§7Un guerrier solitaire, maître des étoiles.",
                RoleManager.getInstance().isRoleEnabled(Role.RADAHN) ? "§aActivé" : "§cDésactivé",
                "§eClique pour activer/désactiver ce rôle."
        ));
        radahn.setItemMeta(radahnMeta);
        menu.setItem(3, radahn);

        // Bouton pour Morgott
        ItemStack morgott = new ItemStack(Material.SKELETON_SKULL);
        ItemMeta morgottMeta = morgott.getItemMeta();
        morgottMeta.setDisplayName("§4Morgott");
        morgottMeta.setLore(Arrays.asList(
                "§6Camp: " + Camp.DEMI_DIEUX.getColor() + Camp.DEMI_DIEUX.getName(),
                "§7Un demi-dieu imposteur, roi de la nuit.",
                RoleManager.getInstance().isRoleEnabled(Role.MORGOTT) ? "§aActivé" : "§cDésactivé",
                "§eClique pour activer/désactiver ce rôle."
        ));
        morgott.setItemMeta(morgottMeta);
        menu.setItem(5, morgott);

        // Bouton Retour
        ItemStack backButton = new ItemStack(Material.BARRIER);
        ItemMeta backButtonMeta = backButton.getItemMeta();
        backButtonMeta.setDisplayName("§c§lRetour");
        backButtonMeta.setLore(Arrays.asList("§7Cliquez pour retourner au menu principal."));
        backButton.setItemMeta(backButtonMeta);
        menu.setItem(8, backButton);

        // Ouvrir le menu au joueur
        player.openInventory(menu);

        // Jouer un son
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
    }
}
