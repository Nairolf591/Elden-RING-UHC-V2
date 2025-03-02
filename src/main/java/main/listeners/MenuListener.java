package main.listeners;

import main.main; // Importe la classe `main` depuis le package `main`
import main.EldenRingUHCScoreboard;

import main.ScoreboardManager;
import main.game.*;
import main.menus.*;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
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
                    StuffMenu.open(player); // Ouvrir le menu StuffMenu
                    break;
                case "§c§lConfiguration Avancée": // Nouveau cas
                    ConfigMenu.open(player); // Ouvrir le menu de configuration avancée
                    break;
            }
        }

        else if (event.getView().getTitle().equals(RolesMenu.TITLE)) {
            RoleManager roleManager = RoleManager.getInstance();

            switch (itemName) {
                case "§aLe Sans-Éclat":
                    roleManager.toggleRole(Role.SANS_ECLAT);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                    player.sendMessage("§7Le Sans-Éclat est maintenant " + (roleManager.isRoleEnabled(Role.SANS_ECLAT) ? "§aactivé" : "§cdésactivé") + "§7.");
                    RolesMenu.open(player); // Recharger le menu pour afficher le nouvel état
                    break;
                case "§6Radahn":
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
                case "§6Melina": //AJOUT DU CASE
                    roleManager.toggleRole(Role.MELINA);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                    player.sendMessage("§6Melina est maintenant " + (roleManager.isRoleEnabled(Role.MELINA) ? "§aactivé" : "§cdésactivé") + "§6.");
                    RolesMenu.open(player); // Recharger le menu
                    break;
                case "§c§lRetour":
                    MainMenu.open(player);
                    break;
            }
            // Mettre à jour le scoreboard pour tous les joueurs
            ScoreboardManager scoreboardManager = main.getInstance().getScoreboardManager();
            GameState currentGameState = GameManager.getInstance().getCurrentState(); // Récupérer l'état actuel du jeu
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                scoreboardManager.updateScoreboard(onlinePlayer,currentGameState);
            }
        }

        else if (event.getView().getTitle().equals(StuffMenu.TITLE)) {
            event.setCancelled(true);

            switch (itemName) {
                case "§c§lRetour":
                    MainMenu.open(player);
                    break;
                case "§e§lModifier le Stuff":
                    StuffManager.getInstance().applyStuff(player);
                    player.sendMessage("§eModifie le stuff, puis tape §a/ConfirmStuff §epour sauvegarder.");
                    break;
            }
        }
        // Vérifier si le clic se produit dans un menu
        if (event.getView().getTitle().equals(BorderMenu.TITLE)) {
            event.setCancelled(true); // Empêcher de bouger les items

            clickedItem = event.getCurrentItem();
            if (clickedItem == null || !clickedItem.hasItemMeta()) return;

            itemName = clickedItem.getItemMeta().getDisplayName();
            World world = player.getWorld();

            switch (itemName) {
                case "§a§lAugmenter la bordure":
                    world.getWorldBorder().setSize(world.getWorldBorder().getSize() + 100); // Augmenter la bordure de 100 blocs
                    BorderMenu.open(player); // Rafraîchir le menu
                    break;

                case "§c§lRéduire la bordure":
                    world.getWorldBorder().setSize(Math.max(50, world.getWorldBorder().getSize() - 100)); // Réduire la bordure de 100 blocs (minimum 50)
                    BorderMenu.open(player); // Rafraîchir le menu
                    break;

                case "§4§lAugmenter les dégâts":
                    world.getWorldBorder().setDamageAmount(world.getWorldBorder().getDamageAmount() + 1); // Augmenter les dégâts de 1
                    BorderMenu.open(player); // Rafraîchir le menu
                    break;

                case "§f§lRéduire les dégâts":
                    world.getWorldBorder().setDamageAmount(Math.max(0, world.getWorldBorder().getDamageAmount() - 1)); // Réduire les dégâts de 1 (minimum 0)
                    BorderMenu.open(player); // Rafraîchir le menu
                    break;

                case "§c§lRetour":
                    MainMenu.open(player); // Retour au menu principal
                    break;
            }
        }

    }


}
