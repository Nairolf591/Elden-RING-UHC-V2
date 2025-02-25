package main.game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BossManager implements Listener {

    private final JavaPlugin plugin;
    private final Random random = new Random();
    private final List<Talisman> talismans = new ArrayList<>();

    public BossManager(JavaPlugin plugin) {
        this.plugin = plugin;
        initializeTalismans();
        disableMobSpawning();
        startBossSpawner();
    }

    private void initializeTalismans() {
        talismans.add(new Talisman("Médaillon d'ambre pourpre +3", Material.EMERALD, "§a+2 cœurs permanents"));
        talismans.add(new Talisman("Médaillon d'ambre céruléen +3", Material.LAPIS_LAZULI, "§b+30 de mana"));
        talismans.add(new Talisman("Talisman de la lame bénie", Material.IRON_SWORD, "§e1hp/8sec"));
        talismans.add(new Talisman("Talisman du bouclier draconique +2", Material.SHIELD, "§c+10% de résistance contre les coups"));
        talismans.add(new Talisman("Talisman du chat à longue queue", Material.FEATHER, "§6Pas de dégâts de chute"));
        talismans.add(new Talisman("Talisman du bouclier rituel", Material.SHIELD, "§7Quand full vie, le premier coup reçu fait seulement 1 demi cœur"));
        talismans.add(new Talisman("Talisman d’ancien seigneur", Material.NETHER_STAR, "§dRajoute 5sec à la compétence actuellement utilisée"));
        talismans.add(new Talisman("Lame cornue aux plumes rouges", Material.REDSTONE, "§4Quand en dessous de 5hp, 10% de force"));
        talismans.add(new Talisman("Lame cornue aux plumes bleues", Material.LAPIS_LAZULI, "§1Quand en dessous de 5hp, 10% de résistance"));
        talismans.add(new Talisman("Kyste du prince de la mort", Material.ROTTEN_FLESH, "§8+3 cœurs permanents"));
        talismans.add(new Talisman("Fragment de guerrier jarre", Material.CLAY_BALL, "§a+1 dégât par compétence"));
        talismans.add(new Talisman("Excroissance du prince de la mort", Material.BONE, "§8+1 cœur permanent"));
        talismans.add(new Talisman("Dague pourpre de l'assassin", Material.IRON_SWORD, "§5Tous les 5 coups, 1 demi cœur de heal"));
        talismans.add(new Talisman("Dague céruléenne de l'assassin", Material.DIAMOND_SWORD, "§1Tous les 5 coups, 10 de mana gagné"));
        talismans.add(new Talisman("Talisman de la tortue bleue", Material.CLAY_BALL, "§b2 mana par seconde de regen"));
    }


    private void startBossSpawner() {
        // Nombre aléatoire de boss entre 1 et 6
        int numberOfBosses = random.nextInt(6) + 1;

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

    private void disableMobSpawning() {
        // Désactive le spawn de tous les mobs normaux
        Bukkit.getWorld("world").setSpawnFlags(false, false); // Désactive le spawn des mobs hostiles et amicaux
    }

    private void spawnBoss() {
        int borderSize = (int) Bukkit.getWorld("UHC").getWorldBorder().getSize();
        int x = random.nextInt(borderSize * 2) - borderSize;
        int z = random.nextInt(borderSize * 2) - borderSize;
        Location spawnLocation = new Location(Bukkit.getWorld("UHC"), x, 100, z);

        // Calcul de la distance par rapport à (0, 0)
        double distance = spawnLocation.distance(new Location(spawnLocation.getWorld(), 0, 100, 0));
        String distanceRange = getDistanceRange(distance); // Obtenir la plage de distance

        // Choix aléatoire d'un boss parmi plusieurs types
        int bossType = random.nextInt(3); // 0, 1 ou 2
        LivingEntity boss;

        switch (bossType) {
            case 0:
                boss = (Zombie) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.ZOMBIE);
                boss.setCustomName("Chevalier du creset");
                boss.setMaxHealth(50);
                boss.setHealth(50);
                boss.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
                boss.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0));
                break;
            case 1:
                boss = (LivingEntity) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.SKELETON);
                boss.setCustomName("Archer spectral");
                boss.setMaxHealth(40);
                boss.setHealth(40);
                ((org.bukkit.entity.Skeleton) boss).getEquipment().setItemInHand(new ItemStack(Material.BOW));
                break;
            case 2:
                boss = (LivingEntity) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.WITHER_SKELETON);
                boss.setCustomName("Légionnaire de l'ombre");
                boss.setMaxHealth(60);
                boss.setHealth(60);
                boss.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 0));
                break;
            default:
                boss = (Zombie) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.ZOMBIE);
                boss.setCustomName("Chevalier du creset");
                boss.setMaxHealth(50);
                boss.setHealth(50);
                boss.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
                boss.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0));
                break;
        }

        boss.setCustomNameVisible(true);

        // Affichage des coordonnées de spawn dans la console
        plugin.getLogger().info("Un boss est apparu aux coordonnées : X=" + x + ", Z=" + z);

        Bukkit.broadcastMessage("§6Un boss est apparu " + distanceRange + " du 0 0 !");
    }

    @EventHandler
    public void onBossDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Zombie && event.getEntity().getCustomName() != null && event.getEntity().getCustomName().equals("Chevalier du creset")) {
            Player killer = event.getEntity().getKiller();
            if (killer != null) {
                if (countTalismans(killer) < 2) {
                    Talisman talisman = getRandomTalisman();
                    killer.getInventory().addItem(talisman.getItem());
                    killer.sendMessage("§aVous avez obtenu le Talisman : " + talisman.getName());
                } else {
                    killer.sendMessage("§cVous ne pouvez pas posséder plus de deux talismans.");
                }
            }
        }
    }

    private int countTalismans(Player player) {
        int count = 0;
        for (ItemStack item : player.getInventory()) {
            if (item != null && item.getItemMeta() != null && item.getItemMeta().getLore() != null) {
                for (Talisman talisman : talismans) {
                    if (item.getItemMeta().getDisplayName().equals(talisman.getName())) {
                        count++;
                        break;
                    }
                }
            }
        }
        return count;
    }

    private Talisman getRandomTalisman() {
        return talismans.get(random.nextInt(talismans.size()));
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
