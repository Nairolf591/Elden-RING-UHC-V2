package main.listeners;

import main.game.CampfireData;
import main.game.CampfireManager;
import main.game.PlayerFlasks;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.Location;

import java.util.Map;

public class CampfireListener implements Listener {
    private CampfireManager campfireManager;
    private Map<Player, PlayerFlasks> playerFlasksMap;
    private PlayerFlasks playerFlasks;

    /**
     * Constructeur pour initialiser les dépendances.
     * @param campfireManager : Le gestionnaire des feux de camp.
     * @param playerFlasksMap : La map des fioles des joueurs.
     */
    public CampfireListener(CampfireManager campfireManager, Map<Player, PlayerFlasks> playerFlasksMap) {
        this.campfireManager = campfireManager;
        this.playerFlasksMap = playerFlasksMap;
    }

    /**
     * Gérer l'interaction des joueurs avec un feu de camp.
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        // Vérifier si le bloc cliqué est un feu de camp
        if (block != null && block.getType() == Material.CAMPFIRE) {
            Location campfireLocation = block.getLocation();

            // Vérifier si le feu de camp est géré par CampfireManager
            if (campfireManager.hasCampfire(campfireLocation)) {
                CampfireData campfire = campfireManager.getCampfireData(campfireLocation);

                // Si le feu de camp est allumé, ouvrir le menu
                if (campfire.isLit()) {
                    openCampfireMenu(player); // Ouvrir le menu du feu de camp
                } else {
                    player.sendMessage("§cCe feu de camp est éteint.");
                }
            } else {
                // Initialiser le feu de camp s'il n'existe pas encore
                campfireManager.initCampfire(campfireLocation);
                player.sendMessage("§aUn nouveau feu de camp a été enregistré !");
            }
        }
    }

    /**
     * Ouvrir le menu du feu de camp.
     * @param player : Le joueur à qui ouvrir le menu.
     */
    private void openCampfireMenu(Player player) {
        // Créer un inventaire de menu
        org.bukkit.inventory.Inventory menu = org.bukkit.Bukkit.createInventory(null, 9, "§6Feu de Camp");

        // Bouton pour une fiole d'Estus
        org.bukkit.inventory.ItemStack estusItem = new org.bukkit.inventory.ItemStack(org.bukkit.Material.RED_STAINED_GLASS_PANE);
        org.bukkit.inventory.meta.ItemMeta estusMeta = estusItem.getItemMeta();
        estusMeta.setDisplayName("§cFiole d'Estus");
        estusMeta.setLore(java.util.Arrays.asList("§7Cliquez pour choisir une fiole d'Estus."));
        estusItem.setItemMeta(estusMeta);
        menu.setItem(2, estusItem);

        // Bouton pour une fiole de Mana
        org.bukkit.inventory.ItemStack manaItem = new org.bukkit.inventory.ItemStack(org.bukkit.Material.BLUE_STAINED_GLASS_PANE);
        org.bukkit.inventory.meta.ItemMeta manaMeta = manaItem.getItemMeta();
        manaMeta.setDisplayName("§bFiole de Mana");
        manaMeta.setLore(java.util.Arrays.asList("§7Cliquez pour choisir une fiole de Mana."));
        manaItem.setItemMeta(manaMeta);
        menu.setItem(4, manaItem);

        // Bouton pour les deux fioles
        org.bukkit.inventory.ItemStack bothItem = new org.bukkit.inventory.ItemStack(org.bukkit.Material.PURPLE_STAINED_GLASS_PANE);
        org.bukkit.inventory.meta.ItemMeta bothMeta = bothItem.getItemMeta();
        bothMeta.setDisplayName("§dLes deux fioles");
        bothMeta.setLore(java.util.Arrays.asList("§7Cliquez pour choisir une fiole d'Estus et une fiole de Mana."));
        bothItem.setItemMeta(bothMeta);
        menu.setItem(6, bothItem);

        // Ouvrir le menu au joueur
        player.openInventory(menu);
    }

    @EventHandler
    public void onInventoryClick(org.bukkit.event.inventory.InventoryClickEvent event) {
        // Vérifier si le clic est dans le menu du feu de camp
        if (event.getView().getTitle().equals("§6Feu de Camp")) {
            event.setCancelled(true); // Empêcher le joueur de prendre l'item

            // Vérifier si un item a été cliqué
            if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) return;

            Player player = (Player) event.getWhoClicked();
            String itemName = event.getCurrentItem().getItemMeta().getDisplayName();

            // Récupérer le feu de camp cliqué
            org.bukkit.block.Block block = player.getTargetBlockExact(5);
            if (block == null || block.getType() != Material.CAMPFIRE) return;

            Location campfireLocation = block.getLocation();
            CampfireData campfire = campfireManager.getCampfireData(campfireLocation);

            // Vérifier si le feu de camp a encore des charges
            if (campfire.getCharges() <= 0) {
                player.sendMessage("§cCe feu de camp n'a plus de charges !");
                return;
            }

            // Gérer le choix du joueur en fonction des charges
            switch (itemName) {
                case "§cFiole d'Estus":
                    campfire.reduceCharges(1); // Retirer 1 charge
                    playerFlasks.addEstus(1); // Ajouter 1 fiole d'Estus
                    player.sendMessage("§aVous avez récupéré une fiole d'Estus !");
                    break;
                case "§bFiole de Mana":
                    campfire.reduceCharges(1); // Retirer 1 charge
                    playerFlasks.addMana(1); // Ajouter 1 fiole de Mana
                    player.sendMessage("§aVous avez récupéré une fiole de Mana !");
                    break;
                case "§dLes deux fioles":
                    if (campfire.getCharges() >= 2) { // Vérifier s'il y a assez de charges
                        campfire.reduceCharges(2); // Retirer 2 charges
                        playerFlasks.addEstus(1); // Ajouter 1 fiole d'Estus
                        playerFlasks.addMana(1); // Ajouter 1 fiole de Mana
                        player.sendMessage("§aVous avez récupéré une fiole d'Estus et une fiole de Mana !");
                    } else {
                        player.sendMessage("§cCe feu de camp n'a pas assez de charges !");
                        return;
                    }
                    break;
            }

            // Vérifier si le feu de camp est épuisé
            if (campfire.getCharges() <= 0) {
                campfire.extinguish(); // Éteindre le feu de camp
                player.sendMessage("§cLe feu de camp est éteint !");
            }

            // Fermer le menu après le choix
            player.closeInventory();
        }
    }


}
