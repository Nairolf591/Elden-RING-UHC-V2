package main.game;

import main.game.ManaManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class TalismanEffects {

    private final JavaPlugin plugin;
    private final Map<Player, Integer> playerHitCount = new HashMap<>(); // Pour compter les coups pour les dagues
    private final Map<Player, Boolean> hasTakenFirstHit = new HashMap<>(); // Pour le talisman du bouclier rituel

    public TalismanEffects(JavaPlugin plugin) {
        this.plugin = plugin;
        startTalismanEffectChecker();
    }

    private void startTalismanEffectChecker() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    applyTalismanEffects(player);
                }
            }
        }.runTaskTimer(plugin, 0, 20); // Exécuter toutes les secondes
    }

    public void applyTalismanEffects(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getItemMeta() != null && item.getItemMeta().getDisplayName() != null) {
                String talismanName = item.getItemMeta().getDisplayName();

                switch (talismanName) {
                    case "Médaillon d'ambre pourpre +3":
                        applyAmberPendant(player);
                        break;
                    case "Médaillon d'ambre céruléen +3":
                        applyAmberCerulean(player);
                        break;
                    case "Talisman de la lame bénie":
                        applyBlessedBlade(player);
                        break;
                    case "Talisman du bouclier draconique +2":
                        applyDragonShield(player);
                        break;
                    case "Talisman du chat à longue queue":
                        applyLongTailCat(player);
                        break;
                    case "Talisman du bouclier rituel":
                        applyRitualShield(player);
                        break;
                    case "Talisman d’ancien seigneur":
                        applyAncientLord(player);
                        break;
                    case "Lame cornue aux plumes rouges":
                        applyRedFeatherBlade(player);
                        break;
                    case "Lame cornue aux plumes bleues":
                        applyBlueFeatherBlade(player);
                        break;
                    case "Kyste du prince de la mort":
                        applyDeathPrinceCyst(player);
                        break;
                    case "Fragment de guerrier jarre":
                        applyJarWarriorFragment(player);
                        break;
                    case "Excroissance du prince de la mort":
                        applyDeathPrinceGrowth(player);
                        break;
                    case "Dague pourpre de l'assassin":
                        applyPurpleDagger(player);
                        break;
                    case "Dague céruléenne de l'assassin":
                        applyCeruleanDagger(player);
                        break;
                    case "Talisman de la tortue bleue":
                        applyBlueTurtleTalisman(player);
                        break;
                }
            }
        }
    }

    // Effets des talismans

    private void applyAmberPendant(Player player) {
        player.setMaxHealth(player.getMaxHealth() + 4);
    }

    private void applyAmberCerulean(Player player) {
        ManaManager.getInstance().setMana(player, ManaManager.getInstance().getMana(player) + 30); // +30 de mana
    }

    private void applyBlessedBlade(Player player) {
        if (player.getHealth() < player.getMaxHealth()) {
            player.setHealth(player.getHealth() + 1); // 1hp/8sec
        }
    }

    private void applyDragonShield(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1, 0)); // +10% de résistance
    }

    private void applyLongTailCat(Player player) {
    }

    private void applyRitualShield(Player player) {
        if (player.getHealth() == player.getMaxHealth()) {
            hasTakenFirstHit.put(player, false); // Réinitialiser l'état
        }
    }

    private void applyAncientLord(Player player) {
        // Implémentez un système pour prolonger la durée des compétences de 5 secondes
    }

    private void applyRedFeatherBlade(Player player) {
        if (player.getHealth() < 5) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1, 0)); // +10% de force
        }
    }

    private void applyBlueFeatherBlade(Player player) {
        if (player.getHealth() < 5) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1, 0)); // +10% de résistance
        }
    }

    private void applyDeathPrinceCyst(Player player) {
        player.setMaxHealth(player.getMaxHealth() + 6); // 3 cœurs = 6 points de vie
    }

    private void applyJarWarriorFragment(Player player) {
        // Implémentez un système pour augmenter les dégâts des compétences de 1 cœur
    }

    private void applyDeathPrinceGrowth(Player player) {
        player.setMaxHealth(player.getMaxHealth() + 1);
    }

    private void applyPurpleDagger(Player player) {
        playerHitCount.putIfAbsent(player, 0);
        if (playerHitCount.get(player) >= 5) {
            player.setHealth(player.getHealth() + 1); // Heal 1 demi cœur
            playerHitCount.put(player, 0);
        }
    }

    private void applyCeruleanDagger(Player player) {
        playerHitCount.putIfAbsent(player, 0);
        if (playerHitCount.get(player) >= 5) {
            ManaManager.getInstance().setMana(player, ManaManager.getInstance().getMana(player) + 10); // +10 de mana
            playerHitCount.put(player, 0);
        }
    }

    private void applyBlueTurtleTalisman(Player player) {
        ManaManager.getInstance().setMana(player, ManaManager.getInstance().getMana(player) + 2); // +2 mana par seconde
    }

    public boolean hasTakenFirstHit(Player player) {
        return hasTakenFirstHit.getOrDefault(player, false);
    }

    public void setHasTakenFirstHit(Player player, boolean value) {
        hasTakenFirstHit.put(player, value);
    }

    public void incrementHitCount(Player player) {
        playerHitCount.put(player, playerHitCount.getOrDefault(player, 0) + 1);
    }



}
