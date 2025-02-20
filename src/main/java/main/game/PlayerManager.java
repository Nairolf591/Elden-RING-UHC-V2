package main.game;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;

public class PlayerManager {

    private static PlayerManager instance; // Instance du singleton
    private Map<Player, PlayerData> playerDataMap; // Données des joueurs

    private PlayerManager() {
        this.playerDataMap = new HashMap<>();
    }

    // Méthode pour obtenir l'instance du singleton
    public static PlayerManager getInstance() {
        if (instance == null) {
            instance = new PlayerManager();
        }
        return instance;
    }

    // Ajouter un joueur
    public void addPlayer(Player player) {
        playerDataMap.put(player, new PlayerData(player));
    }

    // Assigner un camp à un joueur
    public void setPlayerCamp(Player player, Camp camp) {
        PlayerData playerData = playerDataMap.get(player);
        if (playerData != null) {
            playerData.setCamp(camp);
        }
    }

    // Assigner un rôle à un joueur
    public void setPlayerRole(Player player, Role role) {
        PlayerData playerData = playerDataMap.get(player);
        if (playerData != null) {
            playerData.setRole(role);
        }
    }

    // Obtenir le camp d'un joueur
    public Camp getPlayerCamp(Player player) {
        PlayerData playerData = playerDataMap.get(player);
        return (playerData != null) ? playerData.getCamp() : null;
    }

    // Obtenir le rôle d'un joueur
    public Role getPlayerRole(Player player) {
        PlayerData playerData = playerDataMap.get(player);
        return (playerData != null) ? playerData.getRole() : null;
    }

    // Supprimer un joueur (quand il quitte la partie)
    public void removePlayer(Player player) {
        playerDataMap.remove(player);
    }
}
