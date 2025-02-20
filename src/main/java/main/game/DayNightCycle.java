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
        // Démarrer le cycle jour/nuit
        new BukkitRunnable() {
            @Override
            public void run() {
                if (isDay) {
                    // Passer à la nuit
                    world.setTime(18000); // Temps de nuit dans Minecraft
                    isDay = false;
                    Bukkit.broadcastMessage(ChatColor.BLUE + "La nuit tombe...");
                    // Planifier la fin de la nuit
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this::startDay, nightDuration * 20);
                } else {
                    // Passer au jour
                    world.setTime(0); // Temps de jour dans Minecraft
                    isDay = true;
                    Bukkit.broadcastMessage(ChatColor.YELLOW + "Le jour se lève...");
                    // Planifier la fin du jour
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this::startNight, dayDuration * 20);
                }
            }

            private void startDay() {
                isDay = true;
            }

            private void startNight() {
                isDay = false;
            }
        }.runTaskTimer(plugin, 0, (dayDuration + nightDuration) * 20);
    }

    public void setDayDuration(int dayDuration) {
        this.dayDuration = dayDuration;
    }

    public void setNightDuration(int nightDuration) {
        this.nightDuration = nightDuration;
    }
}
