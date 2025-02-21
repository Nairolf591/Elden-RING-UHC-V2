// Nouveau fichier src/main/java/main/game/ManaManager.java
package main.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashMap;
import java.util.Map;

public class ManaManager extends BukkitRunnable {

    private static ManaManager instance;
    private Map<Player, Integer> playerMana;

    private ManaManager() {
        this.playerMana = new HashMap<>();
    }

    public static ManaManager getInstance() {
        if (instance == null) {
            instance = new ManaManager();
        }
        return instance;
    }

    public void startManaRegeneration(JavaPlugin plugin) {
        this.runTaskTimer(plugin, 0, 20); // 20 ticks = 1 seconde
    }

    public int getMana(Player player) {
        return playerMana.getOrDefault(player, 100);
    }

    public void setMana(Player player, int mana) {
        playerMana.put(player, Math.min(mana, 100));
    }

    public void consumeMana(Player player, int amount) {
        int currentMana = getMana(player);
        setMana(player, currentMana - amount);
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            int currentMana = getMana(player);
            if (currentMana < 100) {
                setMana(player, currentMana + 1);
            }
        }
    }
}
