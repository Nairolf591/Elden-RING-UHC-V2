// Nouveau fichier src/main/java/main/skills/SkillManager.java
package main.skills;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
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
}
