package main.game;

import main.game.ManaManager;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Objects;

public class TalismanEffects {

    private final JavaPlugin plugin;
    private final Map<Player, Integer> playerHitCount = new HashMap<>(); // Pour compter les coups pour les dagues
    private final Map<Player, Boolean> hasTakenFirstHit = new HashMap<>(); // Pour le talisman du bouclier rituel

    // UUIDs uniques pour chaque modificateur de santé.  *DOIVENT ÊTRE DIFFÉRENTS*
    private static final UUID AMBER_PENDANT_UUID = UUID.fromString("a1b2c3d4-e5f6-7890-1234-567890abcdef");
    private static final UUID DEATH_PRINCE_CYST_UUID = UUID.fromString("b2c3d4e5-f6a7-8901-2345-67890abcdef1");
    private static final UUID DEATH_PRINCE_GROWTH_UUID = UUID.fromString("c3d4e5f6-a7b8-9012-3456-7890abcdef2");


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

                // Enlever *TOUS* les modificateurs de santé *AVANT* d'en ajouter un nouveau
                removeAmberPendant(player);
                removeDeathPrinceCyst(player);
                removeDeathPrinceGrowth(player);


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

    // --- Méthodes pour appliquer/supprimer les modificateurs ---

    private void applyAmberPendant(Player player) {
        AttributeModifier modifier = new AttributeModifier(
                AMBER_PENDANT_UUID,
                "Amber Pendant Health Bonus",
                4.0, // +4 points de vie (2 cœurs)
                AttributeModifier.Operation.ADD_NUMBER
                // PAS de EquipmentSlot ici !
        );
        Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).addModifier(modifier);
    }

    private void removeAmberPendant(Player player) {
        removeModifier(player, AMBER_PENDANT_UUID);
    }

    private void applyDeathPrinceCyst(Player player) {
        AttributeModifier modifier = new AttributeModifier(
                DEATH_PRINCE_CYST_UUID,
                "Death Prince Cyst Health Bonus",
                6.0, // +6 points de vie (3 cœurs)
                AttributeModifier.Operation.ADD_NUMBER
                // PAS de EquipmentSlot ici !
        );
        Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).addModifier(modifier);
    }

    private void removeDeathPrinceCyst(Player player) {
        removeModifier(player, DEATH_PRINCE_CYST_UUID);
    }

    private void applyDeathPrinceGrowth(Player player) {
        AttributeModifier modifier = new AttributeModifier(
                DEATH_PRINCE_GROWTH_UUID,
                "Death Prince Growth Health Bonus",
                1.0, // +1 points de vie (1/2 cœur)
                AttributeModifier.Operation.ADD_NUMBER
                // PAS de EquipmentSlot ici !
        );
        Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).addModifier(modifier);
    }

    private void removeDeathPrinceGrowth(Player player) {
        removeModifier(player, DEATH_PRINCE_GROWTH_UUID);
    }

    private void removeModifier(Player player, UUID modifierUUID) {
        AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealth != null) {
            for (AttributeModifier modifier : maxHealth.getModifiers()) {
                if (modifier.getUniqueId().equals(modifierUUID)) {
                    maxHealth.removeModifier(modifier);
                    break; // Important: une fois qu'on l'a trouvé, on arrête la boucle
                }
            }
        }
    }

    // --- Autres méthodes (inchangées, sauf applyBlessedBlade) ---
    private void applyAmberCerulean(Player player) {
        ManaManager.getInstance().setMana(player, ManaManager.getInstance().getMana(player) + 30);
    }

    private void applyBlessedBlade(Player player) {
        // Soigne 1 HP toutes les 8 secondes si le joueur n'est pas full HP.
        if (player.getHealth() < player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    // Vérifie si le joueur a toujours le talisman et est en ligne
                    if (player.isOnline() && hasTalisman(player, "Talisman de la lame bénie"))
                    {
                        if (player.getHealth() < player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
                            //Soigne de 1 et non de la moitié de sa vie.
                            double newHealth = Math.min(player.getHealth() + 1, player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()); //On prend la valeur minimal entre la vie max et ce qu'on doit ajouter
                            player.setHealth(newHealth);                    }
                    } else {
                        cancel(); //Annulé si le joueur n'a plus le talisman ou est hors ligne
                    }
                }
            }.runTaskLater(plugin, 8 * 20L); // *20 pour convertir en ticks
        }
    }
    private boolean hasTalisman(Player player, String talismanName) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName() &&
                    item.getItemMeta().getDisplayName().equals(talismanName)) {
                return true;
            }
        }
        return false;
    }

    private void applyDragonShield(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1, 0)); // +Resistance 1, et verifie si c'est bien 10% de dégats en moins
    }

    private void applyLongTailCat(Player player) {
        //Aucun effet
    }

    private void applyRitualShield(Player player) {
        if (player.getHealth() == player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
            hasTakenFirstHit.put(player, false); // Réinitialiser l'état
        }
    }

    private void applyAncientLord(Player player) {
        // TODO: Implémenter
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

    private void applyJarWarriorFragment(Player player) {
        // TODO: Implémenter
    }

    private void applyPurpleDagger(Player player) {
        playerHitCount.putIfAbsent(player, 0);
        if (playerHitCount.get(player) >= 5) {
            player.setHealth(player.getHealth() + 1); // Heal 1/2 coeur
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
    private boolean hasExactTalisman(Player player, String talismanName) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if (meta.hasDisplayName() && meta.getDisplayName().equals(talismanName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
