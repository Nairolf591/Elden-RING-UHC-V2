package main.listeners;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import main.menus.MainMenu;
import main.main;

import java.util.Arrays;

public class ConfigurationCompassListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        // Vérifier si l'item est une boussole "Configuration"
        if (item != null && item.getType() == Material.COMPASS && item.hasItemMeta() &&
                item.getItemMeta().getDisplayName().equals("§a§lConfiguration")) {
            event.setCancelled(true); // Empêcher l'utilisation normale de la boussole
            MainMenu.open(player); // Ouvrir le menu principal
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
        }
    }

    // Méthode pour donner la boussole à l'hôte
    public static void giveConfigurationCompass(Player player) {
        ItemStack compass = new ItemStack(Material.COMPASS);
        ItemMeta meta = compass.getItemMeta();
        meta.setDisplayName("§a§lConfiguration");
        meta.setLore(Arrays.asList("§7Cliquez pour ouvrir le menu de configuration."));
        compass.setItemMeta(meta);
        player.getInventory().addItem(compass);
    }
}
