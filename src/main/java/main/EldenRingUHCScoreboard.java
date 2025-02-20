package main;

import main.game.Role;
import main.game.RoleManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class EldenRingUHCScoreboard {

    private JavaPlugin plugin;
    private String hostName = "Kayou"; // Valeur par défaut

    public EldenRingUHCScoreboard(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void createScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();

        Objective objective = scoreboard.registerNewObjective("EldenRingUHC", "dummy", ChatColor.GOLD + "Elden Ring UHC V1.1");
        objective.setDisplaySlot(org.bukkit.scoreboard.DisplaySlot.SIDEBAR);

        setScoreboardLines(player, objective);

        player.setScoreboard(scoreboard);
    }

    private void setScoreboardLines(Player player, Objective objective) {
        for (String entry : objective.getScoreboard().getEntries()) {
            objective.getScoreboard().resetScores(entry);
        }

        // Titre du scoreboard
        objective.setDisplayName("§6▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        addLine(objective, "§f⚔️ §lElden Ring UHC §r§fV1.1", 10);
        addLine(objective, "§6▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬", 9);

        // Informations du scoreboard
        addLine(objective, "§f👥 Joueurs: §a" + Bukkit.getOnlinePlayers().size(), 8);
        addLine(objective, "§f👑 Hôte: §c" + this.hostName, 7);

        // Rôles activés
        StringBuilder rolesBuilder = new StringBuilder("§f🎭 Rôles: §a");
        RoleManager roleManager = RoleManager.getInstance();
        for (Role role : Role.values()) {
            if (roleManager.isRoleEnabled(role)) {
                rolesBuilder.append(role.getName()).append(" ");
            }
        }
        addLine(objective, rolesBuilder.toString(), 6);
    }

    private void addLine(Objective objective, String text, int score) {
        Score line = objective.getScore(text);
        line.setScore(score);
    }

    public void updateScoreboard(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        Objective objective = scoreboard.getObjective("EldenRingUHC");

        if (objective != null) {
            setScoreboardLines(player, objective);
        }
    }

    // Getter et setter pour hostName
    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public void updateHostName(String newHostName) {
        this.hostName = newHostName; // Mettre à jour le nom de l'hôte

        // Mettre à jour le scoreboard de tous les joueurs
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateScoreboard(player);
        }
    }
}
