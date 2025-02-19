package main;

import main.command.ConfirmStuffCommand;
import main.command.SetHostCommand;
import main.game.GameManager;
import main.game.PlayerManager;
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

    @Override
    public void onEnable() {
        getLogger().info("Elden Ring UHC Activé !");

        // Désactiver la régénération naturelle
        Bukkit.getWorlds().forEach(world -> {
            world.setGameRule(GameRule.NATURAL_REGENERATION, false);
        });

        getServer().getPluginManager().registerEvents(new MenuListener(), this);

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

        Bukkit.getPluginManager().registerEvents(new ConfigurationCompassListener(), this);

        // Initialiser le gestionnaire de scoreboard
        scoreboardManager = new EldenRingUHCScoreboard(this);

        // Enregistrer la commande /sethote
        SetHostCommand setHostCommand = new SetHostCommand(scoreboardManager);
        this.getCommand("sethote").setExecutor(setHostCommand);
        this.getCommand("confirmstuff").setExecutor(new ConfirmStuffCommand());


        // Enregistrer les événements
        getServer().getPluginManager().registerEvents(this, this);
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
}
