package main.game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class GameManager {

    private static GameManager instance; // Instance du singleton
    private GameState currentState;      // État actuel du jeu

    private GameManager() {
        this.currentState = GameState.CONFIG; // État initial : Configuration
    }


    // Méthode pour obtenir l'instance du singleton
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    // Getter et Setter pour l'état du jeu
    public GameState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GameState newState) {
        this.currentState = newState;

        // Notifier les joueurs du changement d'état
        Bukkit.broadcastMessage(ChatColor.GOLD + "État du jeu : " + newState.getDisplayName());
    }
}
