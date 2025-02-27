package main.listeners;

import main.game.CampfireData;
import main.game.CampfireManager;
import main.game.PlayerFlasks;
import main.utils.CampUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
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
    private final Map<Player, PlayerFlasks> playerFlasksMap;

    public CampfireListener(CampfireManager campfireManager, Map<Player, PlayerFlasks> playerFlasksMap) {
        this.campfireManager = campfireManager;
        this.playerFlasksMap = playerFlasksMap;
    }

    /**
     * Gérer l'interaction avec un feu de camp.
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
                    openCampfireMenu(player, campfire);
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

    /**
     * Gérer les clics dans les menus.
     */
    @EventHandler
    public void onInventoryClick(org.bukkit.event.inventory.InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        Inventory menu = event.getClickedInventory();
        ItemStack item = event.getCurrentItem();

        // Vérifier si le clic est dans un menu de feu de camp
        if (menu == null || item == null || !item.hasItemMeta()) return;

        String menuTitle = event.getView().getTitle();
        String itemName = item.getItemMeta().getDisplayName();

        // Gérer le menu des fioles
        if (menuTitle.equals("§6Feu de Camp")) {
            event.setCancelled(true);

            // Récupérer le feu de camp
            Block block = player.getTargetBlockExact(5);
            if (block == null || block.getType() != Material.CAMPFIRE) return;

            Location campfireLocation = block.getLocation();
            CampfireData campfire = campfireManager.getCampfireData(campfireLocation);
            PlayerFlasks playerFlasks = playerFlasksMap.get(player);

            if (playerFlasks == null) {
                // Initialiser les données du joueur
                playerFlasks = new PlayerFlasks();
                playerFlasksMap.put(player, playerFlasks);
                player.sendMessage("§aVos données ont été initialisées.");
            }

            // Gérer le choix du joueur
            switch (itemName) {
                case "§cFiole d'Estus":
                    if (playerFlasks.addEstus(player)) {
                        campfire.reduceCharges(1);
                    }
                    break;
                case "§bFiole de Mana":
                    if (playerFlasks.addMana(player)) {
                        campfire.reduceCharges(1);
                    }
                    break;
                case "§cAnnuler":
                    break;
            }

            // Fermer le menu
            player.closeInventory();
        }

        // Gérer le menu des charges
        else if (menuTitle.equals("§6Retirer des charges")) {
            event.setCancelled(true);

            // Récupérer le feu de camp
            Block block = player.getTargetBlockExact(5);
            if (block == null || block.getType() != Material.CAMPFIRE) return;

            Location campfireLocation = block.getLocation();
            CampfireData campfire = campfireManager.getCampfireData(campfireLocation);

            // Gérer le choix du joueur
            if (itemName.startsWith("§eRetirer ")) {
                int amount = Integer.parseInt(itemName.split(" ")[1]); // Extraire le nombre de charges

                if (campfire.getCharges() >= amount) {
                    campfire.reduceCharges(amount);
                    player.sendMessage("§aVous avez retiré " + amount + " charge(s) du feu de camp !");
                } else {
                    player.sendMessage("§cCe feu de camp n'a pas assez de charges !");
                }
            } else if (itemName.equals("§cAnnuler")) {
                // Fermer le menu sans rien faire
            }

            // Fermer le menu
            player.closeInventory();
        }
    }
}

