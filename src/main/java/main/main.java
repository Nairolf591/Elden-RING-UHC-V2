package main;

import main.command.ConfirmStuffCommand;
import main.command.SetHostCommand;
import main.command.StopUHCCommand;
import main.game.GameManager;
import main.game.GameScoreboard;
import main.game.GameState;
import main.game.PlayerManager;
import main.listeners.ConfigMenuListener;
import main.listeners.ConfigurationCompassListener;
import main.listeners.MenuListener;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin implements Listener {

    private EldenRingUHCScoreboard scoreboardManager; // Utilisation de la classe renommée
    private GameScoreboard gameScoreboard; // Déclarer gameScoreboard en tant que champ

    @Override
    public void onEnable() {
        getLogger().info("Elden Ring UHC Activé !");

        // Désactiver la régénération naturelle
        Bukkit.getWorlds().forEach(world -> {
            world.setGameRule(GameRule.NATURAL_REGENERATION, false);
        });


        // Configurer la bordure du monde
        Bukkit.getWorlds().forEach(world -> {
            WorldBorder border = world.getWorldBorder();
            border.setCenter(0, 0);
            border.setSize(1000);
            border.setDamageAmount(2.0);
            border.setDamageBuffer(5.0);
            border.setWarningDistance(10);
        });
        // Initialiser le GameManager et PlayerManager
        PlayerManager playerManager = PlayerManager.getInstance();
        GameManager gameManager = GameManager.getInstance();

        //Listeners menus
        Bukkit.getPluginManager().registerEvents(new ConfigurationCompassListener(), this);
        getServer().getPluginManager().registerEvents(new ConfigMenuListener(), this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);

        // Initialiser le gestionnaire de scoreboard
        scoreboardManager = new EldenRingUHCScoreboard(this);
        getServer().getPluginManager().registerEvents(new ConfigMenuListener(), this);

        // Enregistrer commandes
        SetHostCommand setHostCommand = new SetHostCommand(scoreboardManager);
        this.getCommand("sethote").setExecutor(setHostCommand);
        this.getCommand("confirmstuff").setExecutor(new ConfirmStuffCommand());
        this.getCommand("stopuhc").setExecutor(new StopUHCCommand());


        // Enregistrer les événements
        getServer().getPluginManager().registerEvents(this, this);

        // Initialiser le scoreboard en jeu
        this.gameScoreboard = new GameScoreboard();

        // Exemple : Mettre à jour le scoreboard pour tous les joueurs
        startScoreboardUpdater();
        scoreboardManager = new EldenRingUHCScoreboard(this);

    }

    @Override
    public void onDisable() {
        getLogger().info("Elden Ring UHC Désactivé...");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Créer et afficher le scoreboard pour le joueur
        scoreboardManager.createScoreboard(player);
        scoreboardManager.updateScoreboard(player);
    }

    public EldenRingUHCScoreboard getScoreboardManager() {
        return scoreboardManager;
    }

    private void startScoreboardUpdater() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                gameScoreboard.updateScoreboard(player, GameState.STARTING); // ou GameState.PLAYING
            }
        }, 0L, 20L); // Exécuter toutes les secondes (20 ticks = 1 seconde)
    }

}
