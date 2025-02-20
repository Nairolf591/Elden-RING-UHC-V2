package main.game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class DayNightCycle {

    private final Plugin plugin;
    private final World world;
    private int dayDuration; // Durée du jour en secondes
    private int nightDuration; // Durée de la nuit en secondes
    private boolean isDay; // Indique si c'est le jour ou la nuit

    public DayNightCycle(Plugin plugin, World world, int dayDuration, int nightDuration) {
        this.plugin = plugin;
        this.world = world;
        this.dayDuration = dayDuration;
        this.nightDuration = nightDuration;
        this.isDay = true; // Commencer par le jour
    }

    public void startCycle() {
        // Réinitialiser le temps du monde (jour 0)
        world.setFullTime(0);

        // Commencer directement par le jour
        world.setTime(0); // Temps de jour
        isDay = true;
        Bukkit.broadcastMessage(ChatColor.YELLOW + "Le jour commence !");

        // Planifier la nuit après la durée du jour
        scheduleNight();
    }

    private void scheduleNight() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            // Passer à la nuit
            world.setTime(18000); // Temps de nuit dans Minecraft
            isDay = false;
            Bukkit.broadcastMessage(ChatColor.BLUE + "La nuit tombe...");

            // Planifier le prochain jour après la durée de la nuit
            scheduleDay();
        }, dayDuration * 20); // Convertir la durée du jour en ticks
    }

    private void scheduleDay() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            // Passer au jour
            world.setTime(0); // Temps de jour
            isDay = true;
            Bukkit.broadcastMessage(ChatColor.YELLOW + "Le jour se lève...");

            // Planifier la prochaine nuit après la durée du jour
            scheduleNight();
        }, nightDuration * 20); // Convertir la durée de la nuit en ticks
    }

    public void setDayDuration(int dayDuration) {
        this.dayDuration = dayDuration;
    }

    public void setNightDuration(int nightDuration) {
        this.nightDuration = nightDuration;
    }
}
