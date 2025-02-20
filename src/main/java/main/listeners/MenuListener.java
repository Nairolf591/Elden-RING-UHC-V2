package main.listeners;

import main.game.*;
import main.menus.*;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MenuListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        // Vérifier si le clic se produit dans un menu
        if (event.getView().getTitle().equals(MainMenu.TITLE) ||
                event.getView().getTitle().equals(BorderMenu.TITLE) ||
                event.getView().getTitle().equals(RolesMenu.TITLE)) {
            event.setCancelled(true); // Annuler l'événement pour empêcher de bouger les items
        }

        ItemStack clickedItem = event.getCurrentItem();

        // Vérifier si un item a été cliqué
        if (clickedItem == null || !clickedItem.hasItemMeta()) return;

        String itemName = clickedItem.getItemMeta().getDisplayName();

        // Gérer les clics dans le menu principal
        if (event.getView().getTitle().equals(MainMenu.TITLE)) {
            switch (itemName) {
                case "§a§lLancer la partie":
                    GameManager.getInstance().setCurrentState(GameState.STARTING);
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                    player.sendMessage("§aLa partie est lancée ! Démarrage en cours...");
                    break;
                case "§6§lConfigurer la bordure":
                    BorderMenu.open(player);
                    break;
                case "§b§lConfigurer les rôles":
                    RolesMenu.open(player);
                    break;
                case "§e§lConfigurer le stuff":
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                    player.sendMessage("§eConfiguration du stuff bientôt disponible !");
                    break;
                case "§c§lConfiguration Avancée": // Nouveau cas
                    ConfigMenu.open(player); // Ouvrir le menu de configuration avancée
                    break;
            }
        }

        // Gérer les clics dans le menu des rôles
        else if (event.getView().getTitle().equals(RolesMenu.TITLE)) {
            RoleManager roleManager = RoleManager.getInstance();

            switch (itemName) {
                case "§7Le Sans-Éclat":
                    roleManager.toggleRole(Role.SANS_ECLAT);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                    player.sendMessage("§7Le Sans-Éclat est maintenant " + (roleManager.isRoleEnabled(Role.SANS_ECLAT) ? "§aactivé" : "§cdésactivé") + "§7.");
                    RolesMenu.open(player); // Recharger le menu pour afficher le nouvel état
                    break;
                case "§cRadahn":
                    roleManager.toggleRole(Role.RADAHN);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                    player.sendMessage("§cRadahn est maintenant " + (roleManager.isRoleEnabled(Role.RADAHN) ? "§aactivé" : "§cdésactivé") + "§c.");
                    RolesMenu.open(player); // Recharger le menu pour afficher le nouvel état
                    break;
                case "§4Morgott":
                    roleManager.toggleRole(Role.MORGOTT);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                    player.sendMessage("§4Morgott est maintenant " + (roleManager.isRoleEnabled(Role.MORGOTT) ? "§aactivé" : "§cdésactivé") + "§4.");
                    RolesMenu.open(player); // Recharger le menu pour afficher le nouvel état
                    break;
                case "§c§lRetour":
                    MainMenu.open(player);
                    break;
            }
        }

        else if (event.getView().getTitle().equals(StuffMenu.TITLE)) {
            event.setCancelled(true);

            switch (itemName) {
                case "§c§lRetour":
                    MainMenu.open(player);
                    break;
                case "§e§lModifier":
                    StuffManager.getInstance().applyStuff(player);
                    player.sendMessage("§eModifie le stuff, puis tape §a/ConfirmStuff §epour sauvegarder.");
                    break;
            }
        }

    }


}
