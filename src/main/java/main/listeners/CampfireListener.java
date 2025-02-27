// src/main/java/main/listeners/CampfireListener.java (Simplifié et corrigé)
package main.listeners;

import main.game.*;
import main.menus.CampfireMenu;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Campfire;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class CampfireListener implements Listener {
    private final CampfireManager campfireManager;
    private final Map<Player, PlayerFlasks> playerFlasksMap;

    public CampfireListener(CampfireManager campfireManager, Map<Player, PlayerFlasks> playerFlasksMap) {
        this.campfireManager = campfireManager;
        this.playerFlasksMap = playerFlasksMap;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (GameManager.getInstance().getCurrentState() != GameState.PLAYING) {
            return;
        }
        Block block = event.getBlock();
        if (block.getType() == Material.CAMPFIRE) {
            campfireManager.initCampfire(block.getLocation());
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (GameManager.getInstance().getCurrentState() != GameState.PLAYING) {
            return;
        }
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if (block != null && block.getType() == Material.CAMPFIRE) {
            Location loc = block.getLocation();
            if (campfireManager.hasCampfire(loc)) {
                CampfireData campfire = campfireManager.getCampfireData(loc);
                if (campfire.isLit()) {
                    CampfireMenu.openCampfireMenu(player, campfire);//On appelle la méthode centralisé
                } else {
                    player.sendMessage("§cCe feu de camp est éteint.");
                }
            } else {
                campfireManager.initCampfire(loc);
                player.sendMessage("§aNouveau feu de camp enregistré!");
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (GameManager.getInstance().getCurrentState() != GameState.PLAYING) {
            return;
        }

        if (event.getView().getTitle().equals("§6Feu de Camp")) {
            event.setCancelled(true);

            if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) return;

            Player player = (Player) event.getWhoClicked();
            String itemName = event.getCurrentItem().getItemMeta().getDisplayName();

            Block block = player.getTargetBlockExact(5);
            if (block == null || block.getType() != Material.CAMPFIRE) {
                player.sendMessage("§cVeuillez cliquer sur un feu de camp.");
                return;
            }
            Location campfireLocation = block.getLocation();
            CampfireData campfire = campfireManager.getCampfireData(campfireLocation);

            if (campfire == null) {
                player.sendMessage("§cCe feu de camp n'est pas enregistré.");
                return;
            }

            if (!campfire.isLit()) {
                player.sendMessage("§cCe feu de camp est éteint!");
                player.closeInventory();
                return;
            }

            // Récupération/Création de PlayerFlasks.  *TOUJOURS* faire ça avant de modifier.
            PlayerFlasks playerFlasks = playerFlasksMap.getOrDefault(player, new PlayerFlasks());

            // Gestion des clics. Plus lisible avec un switch.
            switch (itemName) {
                case "§cFiole d'Estus":
                    if (campfire.getCharges() > 0 && (playerFlasks.getEstus() + playerFlasks.getMana()) < 2 && playerFlasks.getEstus() < 2) {
                        campfire.reduceCharges(1);
                        playerFlasks.addEstus(player);

                    } else if(campfire.getCharges() <= 0) {
                        player.sendMessage("§cCe feu de camp n'a plus de charges.");
                    }
                    else
                    {
                        player.sendMessage("§cVous avez déjà atteint la limite de fioles.");
                    }
                    break;
                case "§bFiole de Mana":
                    if (campfire.getCharges() > 0 && (playerFlasks.getEstus() + playerFlasks.getMana()) < 2  && playerFlasks.getMana() < 2) {
                        campfire.reduceCharges(1);
                        playerFlasks.addMana(player);

                    } else if(campfire.getCharges() <= 0) {
                        player.sendMessage("§cCe feu de camp n'a plus de charges.");
                    }
                    else
                    {
                        player.sendMessage("§cVous avez déjà atteint la limite de fioles.");
                    }
                    break;
                case "§cQuitter":
                    player.closeInventory();
                    return;
                default:
                    return;
            }

            playerFlasksMap.put(player, playerFlasks);
            if (campfire.getCharges() <= 0)
            {
                campfire.extinguish(); //Extinction du feu de camp;

                Campfire campfireBlock = (Campfire) block.getBlockData(); //Récupération des données, et non du bloc lui même
                campfireBlock.setLit(false); //Extinction VISUEL du feu
                block.setBlockData(campfireBlock); //Applique le changement VISUEL

                player.sendMessage("§cLe feu de camp est à court de charge !");
            }


            player.closeInventory();
        }

        if (event.getView().getTitle().equals("§6Retirer des charges")) { //Menu de retrait de charges
            event.setCancelled(true);
            if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) return;

            Player player = (Player) event.getWhoClicked();
            String itemName = event.getCurrentItem().getItemMeta().getDisplayName();

            // Récupération du feu de camp
            Block block = player.getTargetBlockExact(5);
            if (block == null || block.getType() != Material.CAMPFIRE) {
                player.sendMessage("§cVeuillez cliquer sur un feu de camp.");
                return;
            }
            Location campfireLocation = block.getLocation();
            CampfireData campfire = campfireManager.getCampfireData(campfireLocation);

            if (campfire == null) {  // Vérification supplémentaire
                player.sendMessage("§cCe feu de camp n'est pas enregistré.");
                return;
            }

            if (!campfire.isLit()) { // Vérification de l'état
                player.sendMessage("§cCe feu de camp est éteint!");
                player.closeInventory();
                return;
            }


            // Gestion des clics
            if (itemName.startsWith("§eRetirer ")) {
                int amount = Integer.parseInt(itemName.split(" ")[1]);

                if(campfire.reduceCharges(amount)){ //Si la condition est respecté
                    player.sendMessage("§aVous avez retiré " + amount + " charge(s) du feu de camp!");
                } else {
                    player.sendMessage("§cCe feu de camp n'a pas assez de charges!"); //Message pas assez de charges
                }
            } else if (itemName.equals("§cAnnuler")) {
                player.closeInventory();
                return;
            }

            if (campfire.getCharges() <= 0)
            {
                campfire.extinguish(); //Extinction du feu de camp;

                Campfire campfireBlock = (Campfire) block.getBlockData(); //Récupération des données, et non du bloc lui même
                campfireBlock.setLit(false); //Extinction VISUEL du feu
                block.setBlockData(campfireBlock); //Applique le changement VISUEL

                player.sendMessage("§cLe feu de camp est à court de charge !");
            }

            player.closeInventory();
        }
    }
}
