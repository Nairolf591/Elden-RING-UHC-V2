package main.game;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;

public class CampManager {
    private static CampManager instance;
    private final Map<Player, String> playerCamps = new HashMap<>();

    private CampManager() {}

    public static CampManager getInstance() {
        if (instance == null) {
            instance = new CampManager();
        }
        return instance;
    }

    /**
     * Définir le camp d'un joueur.
     * @param player : Le joueur.
     * @param camp : Le camp du joueur.
     */
    public void setPlayerCamp(Player player, String camp) {
        playerCamps.put(player, camp);
    }

    /**
     * Obtenir le camp d'un joueur.
     * @param player : Le joueur.
     * @return Le camp du joueur, ou null s'il n'a pas de camp.
     */
    public String getPlayerCamp(Player player) {
        return playerCamps.get(player);
    }
}
