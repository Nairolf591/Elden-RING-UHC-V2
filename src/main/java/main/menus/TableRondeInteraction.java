package main.menus;

import main.game.CampfireData;
import main.game.CampfireManager;
import main.game.PlayerFlasks;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TableRondeInteraction {
    private CampfireManager campfireManager;
    private PlayerFlasks playerFlasks;

    public TableRondeInteraction(CampfireManager campfireManager, PlayerFlasks playerFlasks) {
        this.campfireManager = campfireManager;
        this.playerFlasks = playerFlasks;
    }

    // Gérer le choix du joueur
    public void handleChoice(Player player, Location campfireLocation, String choice) {
        CampfireData campfire = campfireManager.getCampfireData(campfireLocation);
        if (campfire != null && campfire.isLit()) {
            switch (choice) {
                case "Estus":
                    playerFlasks.setEstus(playerFlasks.getEstus() + 1);
                    player.sendMessage("Vous avez choisi une fiole d'Estus !");
                    break;
                case "Mana":
                    playerFlasks.setMana(playerFlasks.getMana() + 1);
                    player.sendMessage("Vous avez choisi une fiole de Mana !");
                    break;
                default:
                    player.sendMessage("Choix invalide.");
            }
        } else {
            player.sendMessage("Le feu de camp est éteint !");
        }
    }
}
