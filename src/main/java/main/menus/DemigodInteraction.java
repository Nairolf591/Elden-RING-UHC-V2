package main.menus;

import main.game.CampfireData;
import main.game.CampfireManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class DemigodInteraction {
    private CampfireManager campfireManager;

    public DemigodInteraction(CampfireManager campfireManager) {
        this.campfireManager = campfireManager;
    }

    // Gérer le retrait des charges
    public void handleChargeRemoval(Player player, Location campfireLocation, int amount) {
        CampfireData campfire = campfireManager.getCampfireData(campfireLocation);
        if (campfire != null && campfire.isLit()) {
            if (amount >= 1 && amount <= 6) {
                campfire.reduceCharges(amount);
                player.sendMessage("Vous avez retiré " + amount + " charges.");
            } else {
                player.sendMessage("Le nombre de charges à retirer doit être entre 1 et 6.");
            }
        } else {
            player.sendMessage("Le feu de camp est éteint !");
        }
    }
}
