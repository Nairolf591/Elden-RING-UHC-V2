package main.listeners;

import main.menus.ConfigMenu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ConfigMenuListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Vérifier si le clic est dans le menu de configuration
        if (!event.getView().getTitle().equals(ConfigMenu.MENU_TITLE)) return;

        event.setCancelled(true); // Empêcher le joueur de prendre les items

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || !clickedItem.hasItemMeta()) return;

        Player player = (Player) event.getWhoClicked();
        String itemName = clickedItem.getItemMeta().getDisplayName();

        // Gérer les clics selon l'item cliqué
        switch (itemName) {
            case "Timer de Démarrage":
                player.sendMessage(ChatColor.YELLOW + "À implémenter : Modifier le timer de démarrage.");
                break;
            case "Durée du Jour":
                player.sendMessage(ChatColor.YELLOW + "À implémenter : Modifier la durée du jour.");
                break;
            case "Durée de la Nuit":
                player.sendMessage(ChatColor.YELLOW + "À implémenter : Modifier la durée de la nuit.");
                break;
            case "Fermer":
                player.closeInventory();
                break;
            default:
                break;
        }
    }
}
