package main;

import main.command.ConfirmStuffCommand;
import main.command.SetHostCommand;
import main.command.StopUHCCommand;
import main.game.*;
import main.listeners.*;
import main.skills.SkillManager;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class main extends JavaPlugin implements Listener {

    private EldenRingUHCScoreboard eldenScoreboardManager; // Utilisation de la classe renommée
    private GameScoreboard gameScoreboard; // Déclarer gameScoreboard en tant que champ
    private ScoreboardManager scoreboardManager;
    private static main instance; // Stocker une instance statique de la classe
    private CampfireManager campfireManager;
    private Map<Player, PlayerFlasks> playerFlasksMap;

    @Override
    public void onEnable() {
        getLogger().info("Elden Ring UHC test");

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
        eldenScoreboardManager = new EldenRingUHCScoreboard(this);
        getServer().getPluginManager().registerEvents(new ConfigMenuListener(), this);

        // Enregistrer commandes
        SetHostCommand setHostCommand = new SetHostCommand(eldenScoreboardManager);
        this.getCommand("sethote").setExecutor(setHostCommand);
        this.getCommand("confirmstuff").setExecutor(new ConfirmStuffCommand());
        this.getCommand("stopuhc").setExecutor(new StopUHCCommand());


        // Enregistrer les événements
        getServer().getPluginManager().registerEvents(this, this);

        // Initialiser le scoreboard en jeu
        this.gameScoreboard = new GameScoreboard();

        // Exemple : Mettre à jour le scoreboard pour tous les joueurs
        startScoreboardUpdater();
        // Initialiser le gestionnaire de scoreboard
        this.scoreboardManager = new ScoreboardManager(this);

        instance = this; // Initialiser l'instance

        Bukkit.getPluginManager().registerEvents(new SkillListener(), this);
        ManaManager.getInstance().startManaRegeneration(this);
        SkillManager.getInstance().startCooldownChecker(this);
        new BossManager(this);
        TalismanEffects talismanEffects = new TalismanEffects(this);
        getServer().getPluginManager().registerEvents(new PlayerDamageListener(talismanEffects), this);
        getServer().getPluginManager().registerEvents(new BossManager(this), this);
        new EstusManager(this);
        new ManaPotionManager(this);
        // Initialiser les managers
        campfireManager = new CampfireManager();
        playerFlasksMap = new HashMap<>();

        // Enregistrer les événements
        getServer().getPluginManager().registerEvents(new CampfireListener(campfireManager, playerFlasksMap), this);
        getServer().getPluginManager().registerEvents(new NightListener(playerFlasksMap), this);
        // Lancer la tâche de régénération passive
        startRegenerationTask();

        startFlaskUsageTask();

        getLogger().info("Elden Ring UHC Activé !");
    }

    @Override
    public void onDisable() {
        getLogger().info("Elden Ring UHC Désactivé...");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerManager.getInstance().addPlayer(player); // Initialiser les données du joueur
        Bukkit.getLogger().info("PlayerData initialisé pour " + player.getName()); // Debug
    }


    public ScoreboardManager getScoreboardManager() { // Retourne un ScoreboardManager
        return scoreboardManager;
    }

    private void startScoreboardUpdater() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            if (GameManager.getInstance().getCurrentState() != GameState.CONFIG) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    gameScoreboard.updateScoreboard(player, GameManager.getInstance().getCurrentState());
                }
            }
        }, 0L, 20L); // Exécuter toutes les secondes (20 ticks = 1 seconde)
    }

    private void startRegenerationTask() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                // Vérifier si le joueur est près d'un feu de camp allumé
                for (Location campfireLocation : campfireManager.getCampfireLocations()) {
                    if (player.getLocation().distance(campfireLocation) <= 5 && campfireManager.getCampfireData(campfireLocation).isLit()) {
                        // Appliquer la régénération passive (1 HP toutes les 10 secondes)
                        if (player.getHealth() < 20) {
                            player.setHealth(Math.min(player.getHealth() + 1, 20));
                        }
                        break; // Un seul feu de camp à la fois
                    }
                }
            }
        }, 0L, 200L); // 200 ticks = 10 secondes
    }

    private void startFlaskUsageTask() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerFlasks playerFlasks = playerFlasksMap.get(player);

                if (playerFlasks != null) {
                    // Utiliser une fiole d'Estus si nécessaire
                    playerFlasks.autoUseEstus(player, isNight());

                    // Utiliser une fiole de Mana si nécessaire
                    playerFlasks.autoUseMana(player);
                }
            }
        }, 0L, 20L); // 20 ticks = 1 seconde
    }

    private boolean isNight() {
        // Vérifier si c'est la nuit (temps entre 13000 et 23000 ticks)
        long time = Bukkit.getWorlds().get(0).getTime();
        return time >= 13000 && time < 23000;
    }

    public static main getInstance() {
        return instance;
    }
}
