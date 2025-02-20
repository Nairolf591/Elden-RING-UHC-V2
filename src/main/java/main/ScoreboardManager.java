package main;

import main.game.GameManager;
import main.game.GameScoreboard;
import main.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ScoreboardManager implements Listener {

    private final GameScoreboard gameScoreboard; // Scoreboard de la partie (Playing)
    private final EldenRingUHCScoreboard menuScoreboard; // Scoreboard du menu (Config/Starting)

    public ScoreboardManager(JavaPlugin plugin) {
        this.gameScoreboard = new GameScoreboard();
        this.menuScoreboard = new EldenRingUHCScoreboard(plugin);
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
        GameState currentGameState = GameManager.getInstance().getCurrentState(); // Récupère l'état actuel du jeu
        this.updateScoreboard(player, currentGameState);
    }

    public EldenRingUHCScoreboard getMenuScoreboard() {
        return this.menuScoreboard;
    }
}
