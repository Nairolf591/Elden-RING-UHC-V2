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

import static org.bukkit.Bukkit.getLogger;

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

        // Debug
        getLogger().info("Mise à jour du scoreboard pour " + player.getName() + " en mode " + gameState);

        // Afficher le bon scoreboard en fonction du mode
        switch (gameState) {
            case CONFIG:
                menuScoreboard.createScoreboard(player); // Afficher EldenRingUHCScoreboard
                break;
            case STARTING:
            case PLAYING:
            case END:
                gameScoreboard.updateScoreboard(player, gameState); // Afficher GameScoreboard
                break;
            default:
                // Par défaut, afficher le scoreboard du menu
                menuScoreboard.createScoreboard(player);
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
