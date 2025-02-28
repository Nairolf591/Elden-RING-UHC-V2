// src/main/java/main/game/BossManager.java (Corrigé + Améliorations)
package main.game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class BossManager implements Listener {

    private final JavaPlugin plugin;
    private final Random random = new Random();
    private final List<Talisman> availableTalismans = new ArrayList<>(); // Talismans DISPONIBLES
    private static BossManager instance;

    public BossManager(JavaPlugin plugin) {
        this.plugin = plugin;
        initializeTalismans();

        Bukkit.getPluginManager().registerEvents(this, plugin); // Enregistrement du listener, UNE SEULE FOIS
    }

    public static BossManager getInstance(JavaPlugin plugin) {
        if (instance == null) {
            instance = new BossManager(plugin);
        }
        return instance;
    }

    private void initializeTalismans() {
        // Initialisation de la liste des talismans DISPONIBLES.
        availableTalismans.add(new Talisman("Médaillon d'ambre pourpre +3", Material.EMERALD, "§a+2 cœurs permanents"));
        availableTalismans.add(new Talisman("Médaillon d'ambre céruléen +3", Material.LAPIS_LAZULI, "§b+30 de mana"));
        availableTalismans.add(new Talisman("Talisman de la lame bénie", Material.IRON_SWORD, "§e1hp/8sec"));
        availableTalismans.add(new Talisman("Talisman du bouclier draconique +2", Material.SHIELD, "§c+10% de résistance contre les coups"));
        availableTalismans.add(new Talisman("Talisman du chat à longue queue", Material.FEATHER, "§6Pas de dégâts de chute"));
        availableTalismans.add(new Talisman("Talisman du bouclier rituel", Material.SHIELD, "§7Quand full vie, le premier coup reçu fait seulement 1 demi cœur"));
        availableTalismans.add(new Talisman("Talisman d’ancien seigneur", Material.NETHER_STAR, "§dRajoute 5sec à la compétence actuellement utilisée"));
        availableTalismans.add(new Talisman("Lame cornue aux plumes rouges", Material.REDSTONE, "§4Quand en dessous de 5hp, 10% de force"));
        availableTalismans.add(new Talisman("Lame cornue aux plumes bleues", Material.LAPIS_LAZULI, "§1Quand en dessous de 5hp, 10% de résistance"));
        availableTalismans.add(new Talisman("Kyste du prince de la mort", Material.ROTTEN_FLESH, "§8+3 cœurs permanents"));
        availableTalismans.add(new Talisman("Fragment de guerrier jarre", Material.CLAY_BALL, "§a+1 dégât par compétence"));
        availableTalismans.add(new Talisman("Excroissance du prince de la mort", Material.BONE, "§8+1 cœur Permanent"));
        availableTalismans.add(new Talisman("Dague pourpre de l'assassin", Material.IRON_SWORD, "§5Tous les 5 coups, 1 demi cœur de heal"));
        availableTalismans.add(new Talisman("Dague céruléenne de l'assassin", Material.DIAMOND_SWORD, "§1Tous les 5 coups, 10 de mana gagné"));
        availableTalismans.add(new Talisman("Talisman de la tortue bleue", Material.CLAY_BALL, "§b2 mana par seconde de regen"));
    }

    public void startBossSpawner() {  // Plus besoin de vérifier l'état du jeu ici
        // Nombre aléatoire de boss entre 1 et 10
        int numberOfBosses = random.nextInt(10) + 1;

        for (int i = 0; i < numberOfBosses; i++) {
            // Temps aléatoire pour le spawn entre 0 et 20 minutes
            int spawnDelay = random.nextInt(20 * 60 * 20); // Entre 0 et 20 minutes (en ticks)

            // Planifier le spawn du boss
            new BukkitRunnable() {
                @Override
                public void run() {
                    spawnBoss();
                }
            }.runTaskLater(plugin, spawnDelay); // Spawn après un délai aléatoire
        }

        // Message dans la console pour indiquer le nombre de boss qui vont spawn
        plugin.getLogger().info("§eDébut du spawn des boss : " + numberOfBosses + " boss vont apparaître dans les 20 minutes.");
    }

    private void spawnBoss() {
        World uhcWorld = Bukkit.getWorld("UHC"); // Utilisez "UHC"
        if (uhcWorld == null) {
            plugin.getLogger().warning("Le monde UHC n'existe pas !");
            return;
        }

        int maxDistance = 300; // Distance maximale de 300 blocs du 0 0
        int x = random.nextInt(maxDistance * 2) - maxDistance;
        int z = random.nextInt(maxDistance * 2) - maxDistance;
        Location spawnLocation = new Location(uhcWorld, x, uhcWorld.getHighestBlockYAt(x, z) + 1, z);

        double distance = spawnLocation.distance(new Location(uhcWorld, 0, uhcWorld.getHighestBlockYAt(0, 0), 0));
        String distanceRange = getDistanceRange(distance);

        int bossType = random.nextInt(3);
        LivingEntity boss;


        switch (bossType) {
            case 0:
                boss = (Zombie) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.ZOMBIE);
                boss.setCustomName("Chevalier du creset");
                boss.setMaxHealth(50);
                boss.setHealth(50);
                boss.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
                boss.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0));
                boss.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
                break;
            case 1:
                boss = (LivingEntity) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.SKELETON);
                boss.setCustomName("Archer spectral");
                boss.setMaxHealth(40);
                boss.setHealth(40);
                boss.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
                // Correction: Vérifiez si le cast est possible avant de l'utiliser
                if (boss instanceof org.bukkit.entity.Skeleton) {
                    ((org.bukkit.entity.Skeleton) boss).getEquipment().setItemInHand(new ItemStack(Material.BOW));
                }
                break;
            case 2:
                boss = (LivingEntity) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.WITHER_SKELETON);
                boss.setCustomName("Légionnaire de l'ombre");
                boss.setMaxHealth(60);
                boss.setHealth(60);
                boss.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 0));
                boss.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
                break;
            default:
                boss = (Zombie) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.ZOMBIE);
                boss.setCustomName("Chevalier du creset");
                boss.setMaxHealth(50);
                boss.setHealth(50);
                boss.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
                boss.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0));
                boss.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
                break;
        }

        boss.setCustomNameVisible(true);

        // Affichage des coordonnées de spawn dans la console
        plugin.getLogger().info("Un boss est apparu aux coordonnées : X=" + x + ", Z=" + z);

        Bukkit.broadcastMessage("§6Un boss est apparu " + distanceRange + " du 0 0 !");
    }

    @EventHandler // AJOUTEZ CECI
    public void onBossDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof LivingEntity && event.getEntity().getCustomName() != null) {
            Player killer = event.getEntity().getKiller();
            if (killer != null) {
                // Vérifiez si le joueur a déjà 2 talismans.
                if (countTalismans(killer) >= 2) {
                    killer.sendMessage("§cVous possédez déjà deux talismans. Vous ne pouvez pas en équiper davantage.");
                    return; // Empêche d'obtenir plus de 2 talismans
                }
                // Gestion des doublons
                if (availableTalismans.isEmpty())
                {
                    killer.sendMessage("§eIl se pourrait qu'aucun talisman n'est été généré");
                }
                Talisman talisman = getRandomTalisman();  // Utilise la nouvelle méthode
                if (talisman != null) {  // Vérification supplémentaire
                    killer.getInventory().addItem(talisman.getItem());
                    killer.sendMessage("§aVous avez obtenu le Talisman : " + talisman.getName());
                }
            }
        }
    }

    private int countTalismans(Player player) {
        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) { //Correction avec .getContents()
            if (item != null && item.getItemMeta() != null && item.getItemMeta().getLore() != null) {
                for (Talisman talisman : availableTalismans) { //On itère sur la liste
                    //On check avec equals, et on getName();
                    if (item.getItemMeta().getDisplayName().equals(talisman.getName())) {
                        count++;
                        break; // Important pour ne pas compter le même talisman plusieurs fois
                    }
                }
            }
        }
        return count;
    }

    private Talisman getRandomTalisman() {
        if (availableTalismans.isEmpty()) {
            return null; // Retourne null si plus de talismans disponibles
        }
        int index = random.nextInt(availableTalismans.size());
        return availableTalismans.remove(index); // Retire et retourne le talisman.
    }

    private static class Talisman {
        private final String name;
        private final Material material;
        private final String lore;

        public Talisman(String name, Material material, String lore) {
            this.name = name;
            this.material = material;
            this.lore = lore;
        }

        public String getName() {
            return name;
        }

        public ItemStack getItem() {
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(name);
            meta.setLore(List.of(lore));
            item.setItemMeta(meta);
            return item;
        }
    }

    private String getDistanceRange(double distance) {
        if (distance < 100) {
            return "à moins de 100 blocs";
        } else if (distance < 200) {
            return "entre 100 et 200 blocs";
        } else if (distance < 300) {
            return "entre 200 et 300 blocs";
        } else if (distance < 400) {
            return "entre 300 et 400 blocs";
        } else if (distance < 500) {
            return "entre 400 et 500 blocs";
        } else {
            return "à plus de 500 blocs";
        }
    }
}
