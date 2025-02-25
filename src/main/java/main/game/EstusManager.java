package main.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class EstusManager {

    private final JavaPlugin plugin;
    private final Map<Player, Integer> estusCharges = new HashMap<>(); // Charges de fioles d'Estus par joueur

    public EstusManager(JavaPlugin plugin) {
        this.plugin = plugin;
        startEstusChecker();
    }

    private void startEstusChecker() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getHealth() < 10 && estusCharges.getOrDefault(player, 0) > 0) {
                        // Utilisation automatique de la fiole d'Estus
                        useEstus(player);
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 20); // Exécuter toutes les secondes
    }

    private void useEstus(Player player) {
        int charges = estusCharges.getOrDefault(player, 0);
        if (charges > 0) {
            // Soigne 1,5 cœurs (3 points de vie)
            player.setHealth(player.getHealth() + 3);
            estusCharges.put(player, charges - 1);

            if (isNight()) {
                // Réduit l'efficacité la nuit (soigne 0,75 cœurs)
                player.setHealth(player.getHealth() + 1.5);
            }

            player.sendMessage("§aVous avez utilisé une fiole d'Estus !");
        }
    }

    private boolean isNight() {
        long time = Bukkit.getWorld("UHC").getTime();
        return time > 13000 && time < 23000;
    }
}
