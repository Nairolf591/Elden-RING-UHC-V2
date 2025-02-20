package main;

import main.game.GameScoreboard;
import main.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class ScoreboardManager {

    private final GameScoreboard gameScoreboard; // Scoreboard de la partie (Playing)
    private final EldenRingUHCScoreboard menuScoreboard; // Scoreboard du menu (Config/Starting)

    public ScoreboardManager() {
        this.gameScoreboard = new GameScoreboard();
        this.menuScoreboard = new EldenRingUHCScoreboard();
    }

    public void updateScoreboard(Player player, GameState gameState) {
        // Supprimer l'ancien scoreboard
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

        // Afficher le bon scoreboard en fonction du mode
        switch (gameState) {
            case CONFIG:
            case STARTING:
                menuScoreboard.updateScoreboard(player);
                break;
            case PLAYING:
                gameScoreboard.updateScoreboard(player, 10, 1, 0); // Exemple de paramètres à adapter
                break;
        }
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        GameState currentGameState = getCurrentGameState(); // Récupère l'état actuel du jeu
        scoreboardManager.updateScoreboard(player, currentGameState);
    }
}
