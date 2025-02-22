package main.role;

import main.game.ManaManager;
import main.game.PlayerData;
import main.game.PlayerManager;
import main.game.Role;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class Morgott {

    private static final int MANA_COST_LAME_MAUDITE = 60; // Coût en Mana pour Lame Maudite
    private static final int MANA_COST_COURROUX_OMEN = 80; // Coût en Mana pour Courroux de l'Omen
    private static final int COOLDOWN_LAME_MAUDITE = 10; // Cooldown en secondes
    private static final int COOLDOWN_COURROUX_OMEN = 1200; // 20 minutes (1200 secondes)


    // Applique les effets de Morgott au joueur
    public static void applyMorgott(Player player) {
        PlayerData playerData = PlayerManager.getInstance().getPlayerData(player);
        if (playerData == null) {
            Bukkit.getLogger().warning("PlayerData est null pour " + player.getName());
            return; // Arrêter l'exécution si les données sont manquantes
        }

        playerData.setRole(Role.MORGOTT); // Assigner le rôle Morgott
        Bukkit.getLogger().info("Rôle Morgott appliqué à " + player.getName());

        // Donner 3 cœurs supplémentaires
        player.setMaxHealth(player.getMaxHealth() + 6); // 3 cœurs = 6 points de vie

        // Donner la Nether Star au joueur
        player.getInventory().addItem(getNetherStar()); // Utilisation de la nouvelle méthode

        // Donner l'épée maudite au joueur
        player.getInventory().addItem(getLameMaudite());

        // Envoyer un message de bienvenue
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        player.sendMessage(ChatColor.GOLD + "╔══════════════════════════════════════════╗");
        player.sendMessage(ChatColor.GOLD + "║               §eVous êtes §4Morgott §e!                          ║");
        player.sendMessage(ChatColor.GOLD + "╠══════════════════════════════════════════╣");
        player.sendMessage(ChatColor.GRAY + " Camp : " + ChatColor.RED + "Demi-dieux");
        player.sendMessage(ChatColor.GRAY + " Description : Un demi-dieu imposteur, roi de la nuit.");
        player.sendMessage(ChatColor.GRAY + " Pouvoirs :");
        player.sendMessage(ChatColor.GRAY + " - §4Lame Maudite §7: Applique un effet de saignement (Wither) aux ennemis.");
        player.sendMessage(ChatColor.GRAY + " - §4Courroux de l'Omen §7: Déchaîne une onde de ténèbres qui inflige des dégâts et réduit la régénération.");
        player.sendMessage(ChatColor.GOLD + "╚══════════════════════════════════════════╝");
    }

    // Utilise la compétence Lame Maudite de Morgott
    public static void useLameMaudite(Player player) {
        if (ManaManager.getInstance().getMana(player) >= MANA_COST_LAME_MAUDITE) {
            ManaManager.getInstance().consumeMana(player, MANA_COST_LAME_MAUDITE);

            // Effets visuels
            Location center = player.getLocation();
            for (int i = 0; i < 360; i += 10) {
                double angle = Math.toRadians(i);
                double x = Math.cos(angle) * 3; // Rayon de l'effet
                double z = Math.sin(angle) * 3;
                Location loc = center.clone().add(x, 1, z);

                // Particules sombres
                player.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 10, 0.2, 0.2, 0.2, 0.1);
                player.getWorld().spawnParticle(Particle.REDSTONE, loc, 10, 0.2, 0.2, 0.2, 0.1, new Particle.DustOptions(Color.fromRGB(0, 0, 0), 2));
            }

            // Son
            player.getWorld().playSound(center, Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);

            // Effets sur les ennemis
            player.getWorld().getNearbyEntities(center, 5, 5, 5).forEach(entity -> {
                if (entity instanceof Player && !entity.equals(player)) {
                    ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 160, 1)); // Wither niveau 1 pendant 8 secondes (160 ticks = 8 secondes)

                }
            });
        } else {
            player.sendMessage(ChatColor.RED + "Pas assez de Mana !");
        }
    }

    // Utilise la compétence Courroux de l'Omen
    public static void useCourrouxOmen(Player player) {
        if (ManaManager.getInstance().getMana(player) >= MANA_COST_COURROUX_OMEN) {
            ManaManager.getInstance().consumeMana(player, MANA_COST_COURROUX_OMEN);

            // Effets visuels
            Location center = player.getLocation();
            for (int i = 0; i < 5; i++) {
                double radius = i * 2; // Rayon de l'onde de choc
                for (int j = 0; j < 360; j += 10) {
                    double angle = Math.toRadians(j);
                    double x = Math.cos(angle) * radius;
                    double z = Math.sin(angle) * radius;
                    Location loc = center.clone().add(x, 1, z);

                    // Particules sombres
                    player.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 10, 0.2, 0.2, 0.2, 0.1);
                    player.getWorld().spawnParticle(Particle.REDSTONE, loc, 10, 0.2, 0.2, 0.2, 0.1, new Particle.DustOptions(Color.fromRGB(0, 0, 0), 2));
                }
            }

            // Son
            player.getWorld().playSound(center, Sound.ENTITY_WITHER_SPAWN, 1.0f, 1.0f);

            // Effets sur les ennemis
            player.getWorld().getNearbyEntities(center, 10, 10, 10).forEach(entity -> {
                if (entity instanceof Player && !entity.equals(player)) {
                    ((Player) entity).damage(8, player); // 4 cœurs de dégâts
                    ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1)); // Lenteur niveau 1
                    ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 1)); // Faiblesse niveau 1
                }
            });
        } else {
            player.sendMessage(ChatColor.RED + "Pas assez de Mana !");
        }
    }

    // Retourne l'épée maudite de Morgott
    public static ItemStack getLameMaudite() {
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = sword.getItemMeta();
        meta.setDisplayName("§4Lame Maudite de Morgott");
        meta.setLore(Arrays.asList(
                "§7Cendre de guerre : Inflict le saignement (Wither) aux ennemis.",
                "§bCoût : " + MANA_COST_LAME_MAUDITE + " Mana",
                "§cEffet : Wither niveau 1 pendant 8 secondes"
        ));
        meta.addEnchant(org.bukkit.enchantments.Enchantment.DAMAGE_ALL, 3, true); // Sharpness 3
        sword.setItemMeta(meta);
        return sword;
    }

    public static ItemStack getNetherStar() {
        // Créer la Nether Star
        ItemStack netherStar = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = netherStar.getItemMeta();
        meta.setDisplayName("§4Courroux de l'Omen");
        meta.setLore(Arrays.asList(
                "§7Utilisez pour activer vos compétences.",
                "§cClique droit pour utiliser.",
                "§7- Coût en Mana : §b" + MANA_COST_COURROUX_OMEN,
                "§7- Inflige des dégâts de zone et §eralentit §7les ennemis."

        ));
        netherStar.setItemMeta(meta);

        // Retourner la Nether Star
        return netherStar;
    }
}
