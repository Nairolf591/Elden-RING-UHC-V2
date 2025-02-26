package main.game;

import main.game.CampfireData;
import main.game.CampfireManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;

public class PassiveRegeneration {
    private CampfireManager campfireManager;

    public PassiveRegeneration(CampfireManager campfireManager) {
        this.campfireManager = campfireManager;
    }

    // Appliquer la régénération passive
    public void applyRegeneration(Player player) {
        for (Location campfireLocation : campfireManager.getCampfireLocations()) {
            CampfireData campfire = campfireManager.getCampfireData(campfireLocation);
            if (campfire.isLit() && campfire.getCharges() > 0 && player.getLocation().distance(campfire.getLocation()) < 10) {
                // Régénérer 1hp toutes les 10 secondes
                double health = player.getHealth();
                if (health < 20) { // Ne pas dépasser la santé maximale
                    player.setHealth(health + 1);
                }
                break;
            }
        }
    }
}
