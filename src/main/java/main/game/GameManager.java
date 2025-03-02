package main.game;

import main.main;
import main.ScoreboardManager;
import main.role.Melina;
import main.role.Morgott;
import main.role.Radahn;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

        // 1.  Compter les rôles ACTIVÉS
        List<Role> enabledRoles = new ArrayList<>();
        for (Role role : Role.values()) {
            if (roleManager.isRoleEnabled(role)) {
                enabledRoles.add(role);
            }
        }

        // 2. Vérifier s'il y a assez de rôles pour les joueurs.
        if (enabledRoles.isEmpty()) {
            Bukkit.broadcastMessage(ChatColor.RED + "Aucun rôle n'est activé !  L'UHC ne peut pas démarrer.");
            return; // Arrête l'attribution des rôles.
        }

        // 3. Créer une liste de joueurs *mélangée*.  C'est CRUCIAL pour une attribution aléatoire.
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        Collections.shuffle(players);  // MÉLANGE !

        // 4. Attribuer les rôles, en gérant le cas où il y a plus de joueurs que de rôles.
        int roleIndex = 0;
        for (Player player : players) {
            // Vérifier si PlayerData existe (toujours une bonne pratique).
            if (playerManager.getPlayerData(player) == null) {
                Bukkit.getLogger().warning("PlayerData non initialisé pour " + player.getName() + ", initialisation en cours...");
                playerManager.addPlayer(player); // Initialiser PlayerData si nécessaire
            }

            // Attribuer le rôle.
            Role roleToAssign = enabledRoles.get(roleIndex % enabledRoles.size()); // % = modulo.  Gère le "wrap-around".
            playerManager.setPlayerRole(player, roleToAssign);

            //Camp différent en fonction du rôle:
            switch(roleToAssign)
            {
                case SANS_ECLAT:
                    playerManager.setPlayerCamp(player, Camp.BASTION_DE_LA_TABLE_RONDE); // Assigne le camp
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f); // Son de niveau up
                    player.sendMessage(ChatColor.GOLD + "╔══════════════════════════════════════════╗");
                    player.sendMessage(ChatColor.GOLD + "║               §eVous êtes §6Le Sans-Éclat §e!                           ║");
                    player.sendMessage(ChatColor.GOLD + "╠══════════════════════════════════════════╣");
                    player.sendMessage(ChatColor.GRAY + " Camp : " + ChatColor.BLUE + "Bastion de la Table Ronde");
                    player.sendMessage(ChatColor.GRAY + " Description : Un humble chevalier sans gloire, mais au cœur pur.");
                    player.sendMessage(ChatColor.GRAY + " Pouvoirs :");
                    player.sendMessage(ChatColor.GRAY + " - §7Vous avez accès à 3 classes uniques, chacune avec");
                    player.sendMessage(ChatColor.GRAY + "   son arme et sa propre §3Cendre de Guerre§7.");
                    player.sendMessage(ChatColor.GRAY + " - §7Vous pouvez également choisir parmi 3 §cSorts Bonus§7.");
                    player.sendMessage(ChatColor.GRAY + " Sections disponibles :");
                    player.sendMessage(ChatColor.GRAY + " - §aClasse : §70/1 choisi. §7Choisissez une classe avec la §a§lNether Star§7.");
                    player.sendMessage(ChatColor.GRAY + " - §cSort Bonus : §70/1 choisi. §7Choisissez un sort avec la §a§lNether Star§7.");
                    player.sendMessage(ChatColor.GOLD + "╚══════════════════════════════════════════╝");

                    // Donner les Nether Stars
                    ItemStack classStar = new ItemStack(Material.NETHER_STAR);
                    ItemMeta classMeta = classStar.getItemMeta();
                    classMeta.setDisplayName("§a§lChoisir votre classe");
                    classMeta.setLore(Arrays.asList(
                            "§7Cliquez pour ouvrir le menu des classes.",
                            "§7Choix disponibles :",
                            "§6Grand Espadon §7(Force, dégâts élevés)",
                            "§eNagakiba §7(Vitesse, téléportation)",
                            "§dBâton d'éclat de Lusat §7(Sorts, régénération de Mana)"
                    ));
                    classStar.setItemMeta(classMeta);

                    ItemStack bonusStar = new ItemStack(Material.NETHER_STAR);
                    ItemMeta bonusMeta = bonusStar.getItemMeta();
                    bonusMeta.setDisplayName("§a§lCompétence bonus");
                    bonusMeta.setLore(Arrays.asList(
                            "§7Cliquez pour ouvrir le menu des sorts bonus.",
                            "§7Choix disponibles :",
                            "§4Flamme noire §7(Feu, faiblesse)",
                            "§eVague de lames §7(Dégâts de zone)",
                            "§bTempête de givre §7(Lenteur, gel)"
                    ));
                    bonusStar.setItemMeta(bonusMeta);

                    player.getInventory().addItem(classStar, bonusStar);
                    CampManager.getInstance().setPlayerCamp(player, "Bastion de la Table Ronde");
                    playerManager.setPlayerCamp(player, Camp.BASTION_DE_LA_TABLE_RONDE);
                    break;
                case MORGOTT:
                    Morgott.applyMorgott(player);
                    CampManager.getInstance().setPlayerCamp(player, "Demi-dieux"); //CORRECTION
                    playerManager.setPlayerCamp(player, Camp.DEMI_DIEUX);
                    break;
                case RADAHN:
                    Radahn.applyRadahn(player);
                    CampManager.getInstance().setPlayerCamp(player, "Solitaire"); //CORRECTION
                    playerManager.setPlayerCamp(player, Camp.SOLITAIRE);
                    break;
                case MELINA: //Nouveau rôle, on applique les effets.
                    Melina.applyMelina(player);
                    CampManager.getInstance().setPlayerCamp(player, "Bastion de la Table Ronde");
                    playerManager.setPlayerCamp(player, Camp.BASTION_DE_LA_TABLE_RONDE);
                    break;
                default: //Par defaut
                    player.sendMessage("§cErreur : Role non géré !");
            }
            roleIndex++; // Passer au rôle suivant (ou revenir au début si nécessaire).
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
            BossManager.getInstance(main.getInstance()).startBossSpawner();
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
