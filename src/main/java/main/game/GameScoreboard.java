package main.game;

import main.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class GameScoreboard {

    private final Scoreboard scoreboard;
    private final Objective objective;

    public GameScoreboard() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        this.scoreboard = manager.getNewScoreboard();
        this.objective = scoreboard.registerNewObjective("GameBoard", "dummy", ChatColor.GOLD + "✨ Elden Ring UHC ✨");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    // Mettre à jour le scoreboard
    public void updateScoreboard(Player player, GameState state) {
        World world = player.getWorld();
        long time = world.getTime();
        String dayPhase = getDayPhase(time);
        String timeUntil = getTimeUntilNextPhase(time);

        objective.setDisplayName(ChatColor.GOLD + "✨ Elden Ring UHC ✨");

        // Supprimer les anciennes entrées
        for (String entry : scoreboard.getEntries()) {
            scoreboard.resetScores(entry);
        }

        // Ajouter les lignes du scoreboard
        addLine("§7§m--------------------------", 10);
        addLine("§f⌛ Mode: §a" + state.toString(), 9);
        addLine("§f👥 Joueurs en vie: §a" + getAlivePlayers(), 8); // À implémenter plus tard
        addLine("§f☀️ Jour actuel: §a" + getCurrentDay(world), 7);
        addLine("§f🌙 " + dayPhase + ": §a" + timeUntil, 6);
        addLine("§7§m--------------------------", 5);
        int mana = ManaManager.getInstance().getMana(player);
        addLine("§fMana: §b" + mana, 5);

        // Appliquer le scoreboard au joueur
        player.setScoreboard(scoreboard);
    }

    // Ajouter une ligne au scoreboard
    private void addLine(String text, int score) {
        objective.getScore(text).setScore(score);
    }

    // Obtenir la phase actuelle (Jour/Nuit)
    private String getDayPhase(long time) {
        return (time < 12000) ? "Prochaine nuit" : "Prochain jour";
    }

    // Calculer le temps avant la prochaine phase
    private String getTimeUntilNextPhase(long time) {
        long nextPhaseTime = (time < 12000) ? 12000 - time : 24000 - time;
        return formatTime(nextPhaseTime);
    }

    // Formater le temps en heures/minutes
    private String formatTime(long ticks) {
        int seconds = (int) (ticks / 20);
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    // Obtenir le jour actuel
    private int getCurrentDay(World world) {
        return (int) (world.getFullTime() / 24000);
    }

    // Obtenir le nombre de joueurs en vie (à implémenter plus tard)
    private int getAlivePlayers() {
        return Bukkit.getOnlinePlayers().size(); // Placeholder
    }

    public void updateScoreboard(Player player, int alivePlayers, int currentDay, long time) {        objective.getScore(ChatColor.GREEN + "Joueurs en vie: ").setScore(alivePlayers);
        objective.getScore(ChatColor.BLUE + "Jour: " + currentDay).setScore(currentDay);
        objective.getScore(ChatColor.YELLOW + "Temps: " + time).setScore((int) time);

        player.setScoreboard(scoreboard);
    }
}
