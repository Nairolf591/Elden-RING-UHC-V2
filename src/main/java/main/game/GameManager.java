package main.game;

import main.main;
import main.ScoreboardManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static org.bukkit.Bukkit.getLogger;


public class GameManager {

    private static GameManager instance; // Instance du singleton
    private GameState currentState;      // État actuel du jeu
    private final Plugin plugin;
    private DayNightCycle dayNightCycle;

    private GameManager() {
        this.plugin = Bukkit.getPluginManager().getPlugin("EldenRingUHC");
        if (this.plugin == null) {
            throw new IllegalStateException("Plugin EldenRingUHC non trouvé !");
        }
        this.currentState = GameState.CONFIG;
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

        // Debug
        getLogger().info("Changement d'état du jeu : " + newState);

        // Mettre à jour le scoreboard de tous les joueurs
        ScoreboardManager scoreboardManager = main.getInstance().getScoreboardManager();
        for (Player player : Bukkit.getOnlinePlayers()) {
            scoreboardManager.updateScoreboard(player, newState);
        }

        // Si l'état est STARTING, lancer la phase de démarrage
        if (newState == GameState.STARTING) {
            startStartingPhase();
        }
    }

    private void giveStuff() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getInventory().clear(); // Clear l'inventaire du joueur
            StuffManager.getInstance().applyStuff(player); // Applique le stuff
        }
        Bukkit.broadcastMessage(ChatColor.GREEN + "Stuff attribué à tous les joueurs !");
    }

    private void assignRoles() {
        RoleManager roleManager = RoleManager.getInstance();
        PlayerManager playerManager = PlayerManager.getInstance();

        for (Player player : Bukkit.getOnlinePlayers()) {
            // Assigner un rôle actif au joueur
            for (Role role : Role.values()) {
                if (roleManager.isRoleEnabled(role)) {
                    playerManager.setPlayerRole(player, role);
                    break; // Assigner un seul rôle par joueur
                }
            }
        }

        Bukkit.broadcastMessage(ChatColor.GOLD + "Les rôles ont été attribués !");
    }

    private void startStartingPhase() {
        World uhcWorld = Bukkit.getWorld("UHC");
        if (uhcWorld == null) {
            Bukkit.broadcastMessage(ChatColor.RED + "Erreur : Le monde UHC n'existe pas !");
            return;
        }

        // Téléporter tous les joueurs dans le monde "UHC"
        for (Player player : Bukkit.getOnlinePlayers()) {
            Location spawnLocation = uhcWorld.getSpawnLocation();
            Location randomLocation = spawnLocation.clone().add(
                    (Math.random() * 400 - 200),  // X : entre -200 et 200
                    100,                          // Y : fixe à 100
                    (Math.random() * 400 - 200)   // Z : entre -200 et 200
            );
            player.teleport(randomLocation);
            player.setGameMode(GameMode.SURVIVAL);
        }

        // Activer l'invincibilité pendant 2 minutes
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2400, 255, false, false));
        }

        // Informer les joueurs de l'invincibilité
        Bukkit.broadcastMessage(ChatColor.GOLD + "Invincibilité activée pendant 2 minutes !");

        // Désactiver le PvP pendant 2 minutes 30
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            for (World world : Bukkit.getWorlds()) {
                world.setPVP(true);
            }
            Bukkit.broadcastMessage(ChatColor.GREEN + "Le PvP est maintenant activé !");
        }, 3000); // 2 minutes 30 (3000 ticks)

        // Informer les joueurs du temps restant avant l'activation du PvP
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            Bukkit.broadcastMessage(ChatColor.YELLOW + "Le PvP sera activé dans 1 minute !");
        }, 2400); // 1 minute avant l'activation du PvP

        // Révoquer l'invincibilité après 2 minutes
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            }
            Bukkit.broadcastMessage(ChatColor.RED + "Vous n'êtes plus invincible !");
        }, 2400); // 2 minutes (2400 ticks)

        // Donner le stuff après 20 secondes
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this::giveStuff, 400);

        // Attribuer les rôles après 50 secondes (30 secondes après le stuff)
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this::assignRoles, 1000);

        // Passer à l'état PLAYING après tout le reste
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            this.currentState = GameState.PLAYING;
            Bukkit.broadcastMessage(ChatColor.GOLD + "Le jeu est maintenant en cours !");
        }, 1000); // 50 secondes (1000 ticks)

        // Démarrer le cycle jour/nuit
        uhcWorld = Bukkit.getWorld("UHC");
        if (uhcWorld != null) {
            dayNightCycle = new DayNightCycle(plugin, uhcWorld, 600, 600); // 10 minutes de jour, 10 minutes de nuit
            dayNightCycle.startCycle();
            Bukkit.broadcastMessage(ChatColor.YELLOW + "Le cycle jour/nuit est activé : 10 minutes de jour, 10 minutes de nuit.");
        }
    }



}
