// Nouveau fichier src/main/java/main/skills/SkillManager.java
package main.skills;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class SkillManager implements Listener {

    private static SkillManager instance;
    private Map<Player, PlayerClass> playerClasses;
    private Map<Player, Long> cooldowns = new HashMap<>();


    private SkillManager() {
        this.playerClasses = new HashMap<>();
    }

    public static SkillManager getInstance() {
        if (instance == null) {
            instance = new SkillManager();
        }
        return instance;
    }

    public void assignClass(Player player, PlayerClass playerClass) {
        if (playerClasses.containsKey(player)) {
            player.sendMessage(ChatColor.RED + "Vous avez déjà choisi une classe !");
            return;
        }
        playerClasses.put(player, playerClass);
        playerClass.applyClass(player);
    }

    public PlayerClass getPlayerClass(Player player) {
        return playerClasses.get(player);
    }
    public boolean isOnCooldown(Player player) {
        if (cooldowns.containsKey(player)) {
            long lastUsed = cooldowns.get(player);
            return (System.currentTimeMillis() - lastUsed) < 10 * 60 * 1000; // 10 minutes en millisecondes
        }
        return false;
    }

    public void setCooldown(Player player) {
        cooldowns.put(player, System.currentTimeMillis());
    }

    public long getCooldownRemaining(Player player) {
        if (cooldowns.containsKey(player)) {
            long lastUsed = cooldowns.get(player);
            long cooldownTime = 10 * 60 * 1000; // 10 minutes en millisecondes
            long remaining = cooldownTime - (System.currentTimeMillis() - lastUsed);
            return remaining > 0 ? remaining : 0; // Retourne 0 si le cooldown est terminé
        }
        return 0;
    }

    public void startCooldownChecker(JavaPlugin plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (cooldowns.containsKey(player)) {
                        long remaining = getCooldownRemaining(player);
                        if (remaining <= 0) {
                            cooldowns.remove(player);
                            player.sendMessage(ChatColor.GREEN + "Votre compétence est prête à être utilisée !");
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L); // Exécute toutes les secondes
    }

}
