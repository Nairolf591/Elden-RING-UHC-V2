// src/main/java/main/listeners/RadahnListener.java
package main.listeners;

import main.game.PlayerManager;
import main.game.Role;
import main.role.Radahn;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class RadahnListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        //Vérifier si le joueur a le rôle Radahn
        if (PlayerManager.getInstance().getPlayerRole(player) != Role.RADAHN) {
            return; // Si le joueur n'est pas Radahn, on ne fait rien.
        }


        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (item != null && item.getType() == Material.DIAMOND_SWORD && item.hasItemMeta() &&
                    item.getItemMeta().getDisplayName().equals("§6Fléau Stellaire")) {
                Radahn.useStarscourge(player); //Appelle la méthode
                event.setCancelled(true); //Pour empêcher l'utilisation normal

            } else if (item != null && item.getType() == Material.NETHER_STAR && item.hasItemMeta() &&
                    item.getItemMeta().getDisplayName().equals("§6Appel Météorique")) {
                Radahn.useMeteorCalling(player);
                event.setCancelled(true); //Pour empêcher l'utilisation normal
            }
        }
    }
}

