// src/main/java/main/game/DayNightCycle.java
package main.game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

public class DayNightCycle {

    private final Plugin plugin;
    private final World world; //Le monde sur lequel est le cycle
    private int dayDuration;   // Durée du jour en secondes
    private int nightDuration; // Durée de la nuit en secondes
    private boolean isDay; // Important pour savoir si c'est le jour
    private final CampfireManager campfireManager; //Ajout de campfireManager

    public DayNightCycle(Plugin plugin, World world, int dayDuration, int nightDuration) {
        this.plugin = plugin;
        this.world = world;
        this.dayDuration = dayDuration;
        this.nightDuration = nightDuration;
        this.isDay = true; // Commence par le jour
        this.campfireManager = new CampfireManager(); //Initialisation
    }

    public void startCycle() {
        // Réinitialise le temps du monde (jour 0)
        world.setFullTime(0);

        // Commence directement par le jour
        world.setTime(0); // Temps de jour
        isDay = true;
        Bukkit.broadcastMessage(ChatColor.YELLOW + "Le jour commence !");
        campfireManager.startDay(); //Appelle la méthode du campfireManager

        // Planifie la nuit après la durée du jour
        scheduleNight();
    }

    private void scheduleNight() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            // Passe à la nuit
            world.setTime(13000); // Temps de nuit dans Minecraft
            isDay = false;
            Bukkit.broadcastMessage(ChatColor.BLUE + "La nuit tombe...");
            campfireManager.startNight();  //Appelle la méthode du campfireManager

            // Planifie le prochain jour après la durée de la nuit
            scheduleDay();
        }, dayDuration * 20L); // Convertit la durée du jour en ticks
    }

    private void scheduleDay() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            // Passe au jour
            world.setTime(1000); // Temps de jour, 1000 pour pas que ça fasse bizarre
            isDay = true;
            Bukkit.broadcastMessage(ChatColor.YELLOW + "Le jour se lève...");
            campfireManager.startDay(); //Appelle la méthode du campfireManager


            // Planifie la prochaine nuit après la durée du jour
            scheduleNight();
        }, nightDuration * 20L); // Convertit la durée de la nuit en ticks
    }


    public void setDayDuration(int dayDuration) {
        this.dayDuration = dayDuration;
    }

    public void setNightDuration(int nightDuration) {
        this.nightDuration = nightDuration;
    }

    public boolean isDay()
    {
        return isDay;
    }
}
