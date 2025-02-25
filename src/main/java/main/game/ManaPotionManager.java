package main.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class ManaPotionManager {

    private final JavaPlugin plugin;
    private final Map<Player, Integer> manaCharges = new HashMap<>(); // Charges de fioles de Mana par joueur

    public ManaPotionManager(JavaPlugin plugin) {
        this.plugin = plugin;
        startManaChecker();
    }

    private void startManaChecker() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (ManaManager.getInstance().getMana(player) < 50 && manaCharges.getOrDefault(player, 0) > 0) {
                        // Utilisation automatique de la fiole de Mana
                        useManaPotion(player);
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 20); // Exécuter toutes les secondes
    }

    private void useManaPotion(Player player) {
        int charges = manaCharges.getOrDefault(player, 0);
        if (charges > 0) {
            // Restaure 90 de mana
            ManaManager.getInstance().setMana(player, ManaManager.getInstance().getMana(player) + 90);
            manaCharges.put(player, charges - 1);

            player.sendMessage("§aVous avez utilisé une fiole de Mana !");
        }
    }
}
