package main.utils;

import org.bukkit.entity.Player;
import main.game.CampManager; // Si tu as une classe pour gérer les camps

public class CampUtils {

    /**
     * Vérifie si un joueur fait partie du camp Bastion de la Table Ronde.
     * @param player : Le joueur à vérifier.
     * @return true si le joueur fait partie de la Table Ronde, false sinon.
     */
    public static boolean isTableRonde(Player player) {
        // Utiliser une classe CampManager pour gérer les camps des joueurs
        CampManager campManager = CampManager.getInstance();

        // Récupérer le camp du joueur
        String playerCamp = campManager.getPlayerCamp(player);

        // Comparer avec "Bastion de la Table Ronde"
        return playerCamp != null && playerCamp.equals("Bastion de la Table Ronde");
    }
}
