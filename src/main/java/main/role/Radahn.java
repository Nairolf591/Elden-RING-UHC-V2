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
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Radahn {

    private static final int MANA_COST_STARCOURGE = 80;
    private static final int MANA_COST_METEOR_CALLING = 70;
    private static final int METEOR_CALLING_COOLDOWN = 10 * 60 * 20; // 10 minutes en ticks
    private static final double meteorExplosionDamage = 2.0;
    private static final double meteorExplosionRadius = 2.0;

    // Applique les effets de Radahn au joueur
    public static void applyRadahn(Player player) {
        PlayerData playerData = PlayerManager.getInstance().getPlayerData(player);
        if (playerData == null) {
            Bukkit.getLogger().warning("PlayerData est null pour " + player.getName());
            return;
        }

        playerData.setRole(Role.RADAHN);
        Bukkit.getLogger().info("Rôle Radahn appliqué à " + player.getName());

        // Donner l'épée pour la cendre de guerre
        player.getInventory().addItem(getStarscourgeSword());
        // Donner la Nether Star pour le sort
        player.getInventory().addItem(getMeteorCallingStar());


        // Envoyer un message de bienvenue (similaire à Morgott, à adapter)
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        player.sendMessage(ChatColor.GOLD + "╔══════════════════════════════════════════╗");
        player.sendMessage(ChatColor.GOLD + "║               §eVous êtes §6Radahn §e!                           ║");
        player.sendMessage(ChatColor.GOLD + "╠══════════════════════════════════════════╣");
        player.sendMessage(ChatColor.GRAY + " Camp : " + ChatColor.YELLOW + "Solitaire");
        player.sendMessage(ChatColor.GRAY + " Description : Un guerrier solitaire, maître des étoiles.");
        player.sendMessage(ChatColor.GRAY + " Pouvoirs :");
        player.sendMessage(ChatColor.GRAY + " - §6Fléau Stellaire §7: Attire les ennemis et provoque une explosion gravitationnelle.");
        player.sendMessage(ChatColor.GRAY + " - §6Appel Météorique §7: Fait pleuvoir des météores sur une zone.");
        player.sendMessage(ChatColor.GOLD + "╚══════════════════════════════════════════╝");

        // Force 1 permanent
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));

        // 3 cœurs supplémentaires (6 points de vie) permanent
        AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealth != null) {
            maxHealth.setBaseValue(maxHealth.getBaseValue() + 6.0);
        }

        // 10% de résistance la nuit (pas d'effet de potion, juste une réduction de dégâts)
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player == null || !player.isOnline()) {
                    cancel(); // Si le joueur n'est plus là, on arrête
                    return;
                }

                World world = player.getWorld();
                long time = world.getTime();

                // Vérifie si c'est la nuit (entre 13000 et 23000 ticks)
                if (time >= 13000 && time <= 23000) {
                    // La nuit, donc 10% de résistance géré plus bas. Comme c'est en continue il faut vérifier
                } else {
                    //Il fait jour
                }
            }
        }.runTaskTimer(JavaPlugin.getPlugin(main.main.class), 0L, 20L); // Vérifie toutes les secondes (20 ticks)
    }


    public static double applyNightResistance(LivingEntity entity, double damage) {
        World world = entity.getWorld();
        long time = world.getTime();
        // Vérifie si c'est la nuit (entre 13000 et 23000 ticks). On fait comme si la résistance s'activait.
        if (time >= 13000 && time <= 23000)
        {
            return damage * 0.9; // Réduit les dégâts de 10%
        }
        return damage; // Renvoie les dégâts inchangés
    }



    // Cendre de Guerre : Fléau Stellaire
    public static void useStarscourge(Player radahn) {
        if (ManaManager.getInstance().getMana(radahn) < MANA_COST_STARCOURGE) {
            radahn.sendMessage(ChatColor.RED + "Pas assez de Mana !");
            return;
        }
        ManaManager.getInstance().consumeMana(radahn, MANA_COST_STARCOURGE);


        // 1. Attraction
        World world = radahn.getWorld();
        double attractionRadius = 15.0;
        double attractionForce = 1.5; //  Ajustez

        // Particules d'attraction
        for (double phi = 0; phi <= 2 * Math.PI; phi += Math.PI / 16) {
            for (double theta = 0; theta <= Math.PI; theta += Math.PI / 8) {
                double x = attractionRadius * Math.sin(theta) * Math.cos(phi);
                double y = attractionRadius * Math.sin(theta) * Math.sin(phi);
                double z = attractionRadius * Math.cos(theta);
                Location particleLoc = radahn.getLocation().add(x, y + 1, z);
                world.spawnParticle(Particle.SPELL_WITCH, particleLoc, 1, 0, 0, 0, 0); //particule de base
            }
        }
        //On combine avec des particules de portail
        for (int i = 0; i < 50; i++) { //  Nombre de particules
            Location loc = radahn.getLocation().add(Math.random() * attractionRadius * 2 - attractionRadius,
                    1 + Math.random() * 2,
                    Math.random() * attractionRadius * 2 - attractionRadius);
            world.spawnParticle(Particle.PORTAL, loc, 1, 0, 0, 0, 1); //Particules de tp
        }


        List<Entity> nearbyEntities = radahn.getNearbyEntities(attractionRadius, attractionRadius, attractionRadius);
        for (Entity entity : nearbyEntities) {
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;
                //On récupère la direction, on la normalise, on la multiplie
                Vector direction = radahn.getLocation().subtract(livingEntity.getLocation()).toVector().normalize().multiply(attractionForce);
                //On l'inverse ou pas
                if (livingEntity.getLocation().distance(radahn.getLocation()) < 3) {
                    direction.multiply(-1);
                }
                livingEntity.setVelocity(direction);
            }
        }
        //Son d'attraction
        radahn.getWorld().playSound(radahn.getLocation(), Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, 1.0f, 1.0f);


        // 2. Explosion Gravitationnelle (après un délai)
        new BukkitRunnable() {
            @Override
            public void run() {
                double explosionRadius = 6.0; //  Ajustez
                double explosionDamage = 6.0; // 3 coeurs
                double knockbackHeight = 1.5;  //Ajustez

                // Particules d'explosion
                world.spawnParticle(Particle.EXPLOSION_HUGE, radahn.getLocation(), 1);
                world.spawnParticle(Particle.SPELL_WITCH, radahn.getLocation(), 100, explosionRadius, 0.5, explosionRadius, 0.1);
                //Autre type de particules
                for (int i = 0; i < 50; i++) { //  Nombre de particules
                    Location loc = radahn.getLocation().add(Math.random() * explosionRadius * 2 - explosionRadius,
                            1 + Math.random() * 2,
                            Math.random() * explosionRadius * 2 - explosionRadius);
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 2, 0, 0, 0, 0.1);
                    world.spawnParticle(Particle.TOWN_AURA, loc, 5, 0, 0, 0, 0.3);
                }

                //Son d'explosion
                radahn.getWorld().playSound(radahn.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);

                //Infliger des dégats et du knockback
                for (Entity entity : nearbyEntities) {
                    if (entity instanceof LivingEntity) {
                        LivingEntity livingEntity = (LivingEntity) entity;
                        double distance = livingEntity.getLocation().distance(radahn.getLocation());

                        if (distance <= explosionRadius) {
                            double damage = explosionDamage * (1 - (distance / explosionRadius)); // Dégats dégressifs
                            livingEntity.damage(damage, radahn);
                            Vector knockback = livingEntity.getLocation().subtract(radahn.getLocation()).toVector().normalize();
                            knockback.setY(knockbackHeight); // Hauteur du knockback
                            livingEntity.setVelocity(knockback);

                            //Effet de Slowness, Weakness
                            ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1)); // Slowness II, 5 secondes
                            ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 0));
                        }
                    }
                }
            }
        }.runTaskLater(JavaPlugin.getPlugin(main.main.class), 30L); // 1.5 secondes * 20 ticks/seconde
    }
    public static void useMeteorCalling(Player radahn) {
        // ... (vérifications de mana et cooldown, comme avant) ...
        if (ManaManager.getInstance().getMana(radahn) < MANA_COST_METEOR_CALLING) {
            radahn.sendMessage(ChatColor.RED + "Pas assez de Mana !");
            return;
        }
        if (radahn.hasMetadata("meteor_calling_cooldown")) {
            long timeLeft = (radahn.getMetadata("meteor_calling_cooldown").get(0).asLong() - System.currentTimeMillis()) / 1000;
            if (timeLeft > 0) {
                radahn.sendMessage(ChatColor.RED + "Appel Météorique est en cooldown ! Temps restant : " + timeLeft + " secondes.");
                return;
            }
        }

        ManaManager.getInstance().consumeMana(radahn, MANA_COST_METEOR_CALLING);
        radahn.setMetadata("meteor_calling_cooldown", new org.bukkit.metadata.FixedMetadataValue(JavaPlugin.getPlugin(main.main.class), System.currentTimeMillis() + (METEOR_CALLING_COOLDOWN / 20) * 1000L));

        World world = radahn.getWorld();
        double zoneRadius = 10.0;
        int numberOfMeteors = 6;
        double meteorImpactDamage = 4.0;

        List<Entity> targetEntities = radahn.getNearbyEntities(zoneRadius, zoneRadius, zoneRadius);
        List<LivingEntity> livingTargets = new ArrayList<>();

        for (Entity e : targetEntities) {
            if (e instanceof LivingEntity) {
                livingTargets.add((LivingEntity) e);
            }
        }

        Location targetLocation;
        if (livingTargets.isEmpty()) {
            targetLocation = radahn.getLocation();
        } else {
            targetLocation = livingTargets.get(0).getLocation();
        }


        for (int i = 0; i < numberOfMeteors; i++) {
            double randomX = (Math.random() - 0.5) * 2 * zoneRadius;
            double randomZ = (Math.random() - 0.5) * 2 * zoneRadius;
            Location meteorSpawnLocation = targetLocation.clone().add(randomX, 25, randomZ);

            Fireball fireball = world.spawn(meteorSpawnLocation, Fireball.class);
            fireball.setYield(2.5f);
            fireball.setIsIncendiary(true);
            fireball.setShooter(radahn);

            // ANNULER l'héritage de vélocité, *PUIS* définir la vélocité verticale:
            fireball.setVelocity(new Vector(0, 0, 0)); // Annule TOUTE vélocité
            fireball.setVelocity(new Vector(0, -0.1, 0)); // Définit la vélocité verticale

            new BukkitRunnable() {
                Location lastLoc = null;
                int ticksLived = 0;

                @Override
                public void run() {
                    ticksLived++;

                    if (!fireball.isDead() && ticksLived < 300) {
                        for(Player p : Bukkit.getOnlinePlayers()){//Parcourt tout les joueurs
                            if(p.getWorld() == world){ //Du même monde
                                if(p.getLocation().distance(fireball.getLocation()) < 20){ //20 block à la ronde
                                    p.playSound(p.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,0.2f,1f);
                                    p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 40, 1, true, false)); // Légère nausée
                                }
                            }
                        }

                        if (lastLoc != null) {
                            double distance = fireball.getLocation().distance(lastLoc);
                            for (double d = 0; d < distance; d += 0.5) {
                                // Interpolation linéaire pour les particules le long de la *vraie* trajectoire
                                Location particleLoc = lastLoc.clone().add(fireball.getVelocity().clone().normalize().multiply(d)); //CORRECTION
                                world.spawnParticle(Particle.FLAME, particleLoc, 0, 0, 0, 0, 0.1);
                                world.spawnParticle(Particle.SMOKE_NORMAL, particleLoc, 0, 0, 0, 0, 0.05);

                            }
                        }

                        lastLoc = fireball.getLocation();

                        // Collision (inchangé)
                        for (Entity nearby : fireball.getNearbyEntities(1.5, 1.5, 1.5)) {
                            if (nearby instanceof LivingEntity && !nearby.equals(radahn)) {
                                LivingEntity target = (LivingEntity) nearby;
                                target.damage(meteorImpactDamage, radahn);
                                target.setFireTicks(80);
                                explode(fireball.getLocation(), radahn); // Son et effet ici
                                fireball.remove();
                                cancel();
                                return;
                            }
                        }

                        if (fireball.isOnGround()) {
                            explode(fireball.getLocation(), radahn);
                            fireball.remove();
                            cancel();
                            return;
                        }
                    } else {
                        if(!fireball.isDead()){ //Pour eviter les lags
                            explode(fireball.getLocation(), radahn);
                        }
                        fireball.remove();
                        cancel();
                    }
                }
            }.runTaskTimer(JavaPlugin.getPlugin(main.main.class), 0L, 1L);
        }
    }

    private static void explode(Location loc, Player source) {
        source.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 1);
        //Son de l'explosion, un peu plus fort
        source.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1.5f, 1f);
        //Effet visuel supplementaire
        source.getWorld().spawnParticle(Particle.FLASH, loc, 1);

        for (Entity nearbyEntity : loc.getWorld().getNearbyEntities(loc, meteorExplosionRadius, meteorExplosionRadius, meteorExplosionRadius)) {
            if (nearbyEntity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) nearbyEntity;
                livingEntity.damage(meteorExplosionDamage, source);  //Dégâts de l'explosion
            }
        }
    }



    public static ItemStack getStarscourgeSword() {
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = sword.getItemMeta();
        meta.setDisplayName("§6Fléau Stellaire");
        meta.setLore(Arrays.asList(
                "§7Cendre de guerre : Attire les ennemis et provoque une explosion gravitationnelle.",
                "§bCoût : " + MANA_COST_STARCOURGE + " Mana",
                "§cDégâts d'explosion : 3 cœurs"
        ));
        meta.addEnchant(org.bukkit.enchantments.Enchantment.DAMAGE_ALL, 3, true); // Tranchant III
        sword.setItemMeta(meta);
        return sword;
    }

    public static ItemStack getMeteorCallingStar() {
        ItemStack star = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = star.getItemMeta();
        meta.setDisplayName("§6Appel Météorique");
        meta.setLore(Arrays.asList(
                "§7Sort : Fait pleuvoir des météores sur une zone.",
                "§bCoût : " + MANA_COST_METEOR_CALLING + " Mana",
                "§cDégâts par météore : 2 cœurs (impact) + 1 cœur (explosion)",
                "§eCooldown : 10 minutes"
        ));
        star.setItemMeta(meta);
        return star;
    }
}
