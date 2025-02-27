package main.listeners;

import main.game.*;
import main.menus.CampfireMenu;
import main.utils.CampUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class CampfireListener implements Listener {
    private final CampfireManager campfireManager;
    private Map<Player, PlayerFlasks> playerFlasksMap;

    public CampfireListener(CampfireManager campfireManager, Map<Player, PlayerFlasks> playerFlasksMap) {
        this.campfireManager = campfireManager;
        this.playerFlasksMap = playerFlasksMap;
    }

    // TRÈS IMPORTANT : Enregistre le feu de camp quand il est placé.
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (GameManager.getInstance().getCurrentState() != GameState.PLAYING){
            return;
        }
        Block block = event.getBlock();
        if (block.getType() == Material.CAMPFIRE) {
            campfireManager.initCampfire(block.getLocation()); // Enregistrement immédiat
        }
    }


    /**
     * Ouvrir le menu du feu de camp.
     * @param player : Le joueur à qui ouvrir le menu.
     * @param campfire : Le feu de camp avec lequel interagir.
     */
    private void openCampfireMenu(Player player, CampfireData campfire) {
        // Vérifier si le joueur fait partie du camp Bastion de la Table Ronde
        boolean isTableRonde = CampUtils.isTableRonde(player);

        if (isTableRonde) {
            // Menu pour les joueurs de la Table Ronde (fioles)
            openFlasksMenu(player, campfire);
        } else {
            // Menu pour les autres joueurs (charges)
            openChargesMenu(player, campfire);
        }
    }

    /**
     * Ouvrir le menu pour choisir une fiole.
     * @param player : Le joueur à qui ouvrir le menu.
     * @param campfire : Le feu de camp avec lequel interagir.
     */
    private void openFlasksMenu(Player player, CampfireData campfire) {
        Inventory menu = Bukkit.createInventory(null, 9, "§6Feu de Camp");

        // Bouton pour une fiole d'Estus
        ItemStack estusItem = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta estusMeta = estusItem.getItemMeta();
        estusMeta.setDisplayName("§cFiole d'Estus");
        estusMeta.setLore(java.util.Arrays.asList("§7Cliquez pour choisir une fiole d'Estus."));
        estusItem.setItemMeta(estusMeta);
        menu.setItem(2, estusItem);

        // Bouton pour une fiole de Mana
        ItemStack manaItem = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
        ItemMeta manaMeta = manaItem.getItemMeta();
        manaMeta.setDisplayName("§bFiole de Mana");
        manaMeta.setLore(java.util.Arrays.asList("§7Cliquez pour choisir une fiole de Mana."));
        manaItem.setItemMeta(manaMeta);
        menu.setItem(4, manaItem);

        // Bouton pour annuler
        ItemStack cancelItem = new ItemStack(Material.BARRIER);
        ItemMeta cancelMeta = cancelItem.getItemMeta();
        cancelMeta.setDisplayName("§cAnnuler");
        cancelItem.setItemMeta(cancelMeta);
        menu.setItem(8, cancelItem);

        // Ouvrir le menu au joueur
        player.openInventory(menu);
    }

    /**
     * Ouvrir le menu pour retirer des charges.
     * @param player : Le joueur à qui ouvrir le menu.
     * @param campfire : Le feu de camp avec lequel interagir.
     */
    private void openChargesMenu(Player player, CampfireData campfire) {
        Inventory menu = Bukkit.createInventory(null, 27, "§6Retirer des charges");

        // Boutons pour retirer de 1 à 6 charges
        for (int i = 1; i <= 6; i++) {
            ItemStack chargeItem = new ItemStack(Material.CHARCOAL);
            ItemMeta chargeMeta = chargeItem.getItemMeta();
            chargeMeta.setDisplayName("§eRetirer " + i + " charge(s)");
            chargeItem.setItemMeta(chargeMeta);
            menu.setItem(i + 9, chargeItem);
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


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (GameManager.getInstance().getCurrentState() != GameState.PLAYING){
            return;
        }
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if (block != null && block.getType() == Material.CAMPFIRE) {
            Location loc = block.getLocation();
            if (campfireManager.hasCampfire(loc)) {
                CampfireData campfire = campfireManager.getCampfireData(loc);
                //On ouvre le menu uniquement si le feu est allumé
                if(campfire.isLit()){
                    CampfireMenu.openCampfireMenu(player); // Ouvre l'interface utilisateur (à voir plus tard)
                } else {
                    player.sendMessage("§cCe feu de camp est éteint.");
                }
            } else {
                //Normalement impossible car on gère le onBlockPlace
                campfireManager.initCampfire(loc);
                player.sendMessage("§aNouveau feu de camp enregistré!"); // Debug
            }
        }
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        //Verification de l'état de la partie
        if (GameManager.getInstance().getCurrentState() != GameState.PLAYING){
            return;
        }

        if (event.getView().getTitle().equals("§6Feu de Camp")) {
            event.setCancelled(true);

            if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) return;

            Player player = (Player) event.getWhoClicked();
            String itemName = event.getCurrentItem().getItemMeta().getDisplayName();


            // Récupération du feu de camp.  On utilise getTargetBlockExact pour plus de précision.
            Block block = player.getTargetBlockExact(5);
            if (block == null || block.getType() != Material.CAMPFIRE) {
                player.sendMessage("§cVeuillez cliquer sur un feu de camp.");
                return;
            }
            Location campfireLocation = block.getLocation();
            CampfireData campfire = campfireManager.getCampfireData(campfireLocation);

            if (campfire == null) { // Vérification supplémentaire importante!
                player.sendMessage("§cCe feu de camp n'est pas enregistré.");
                return;
            }


            if (!campfire.isLit()) { // Vérification d'état
                player.sendMessage("§cCe feu de camp est éteint!");
                player.closeInventory();
                return;
            }

            if (campfire.getCharges() <= 0) {
                player.sendMessage("§cCe feu de camp n'a plus de charges !");
                player.closeInventory();
                return;
            }

            // Récupération/Création de PlayerFlasks.  *TOUJOURS* faire ça avant de modifier.
            //PlayerFlasks playerFlasks = playerFlasksMap.getOrDefault(player, new PlayerFlasks());


            // Gestion des clics. Plus lisible avec un switch.
            switch (itemName) {
                case "§cFiole d'Estus":
                    if (campfire.reduceCharges(1)) { // La méthode reduceCharges renvoie true si la réduction a réussi
                        //playerFlasks.addEstus(1);
                        player.sendMessage("§aVous avez récupéré une fiole d'Estus !");
                    }
                    break;
                case "§bFiole de Mana":
                    if(campfire.reduceCharges(1)) {
                        //playerFlasks.addMana(1);
                        player.sendMessage("§aVous avez récupéré une fiole de Mana !");
                    }
                    break;
                case "§dLes deux fioles":
                    if (campfire.reduceCharges(2)) {  //Vérification géré par le reduceCharges
                        //playerFlasks.addEstus(1);
                        //playerFlasks.addMana(1);
                        player.sendMessage("§aVous avez récupéré une fiole d'Estus et une fiole de Mana !");
                    }
                    break;
                case "§cQuitter": //Pour quitter le menu proprement
                    player.closeInventory();
                    return;
                default: // Cas par défaut, au cas où.
                    return;
            }

            // MISE À JOUR DE LA MAP *APRÈS* LES MODIFICATIONS.
            //playerFlasksMap.put(player, playerFlasks);
            if (campfire.getCharges() <= 0) {
                campfire.extinguish();
                player.sendMessage("§cLe feu de camp est éteint !");
            }
            player.closeInventory();
        }
    }
}

