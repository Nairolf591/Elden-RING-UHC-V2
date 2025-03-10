// src/main/java/main/role/Melina.java
package main.role;

import main.game.Camp;
import main.game.GameManager;
import main.game.GameState;
import main.game.ManaManager;
import main.game.PlayerData;
import main.game.PlayerManager;
import main.game.Role;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.text.SimpleDateFormat;
import java.util.*;

public class Melina implements Listener, CommandExecutor {

    private static final int AURA_OF_DAWN_MANA_COST = 60;
    private static final int AURA_OF_DAWN_COOLDOWN = 10 * 60; // 10 minutes en secondes
    private static final int PIERCING_DAWN_MANA_COST = 50;
    private static final int PIERCING_DAWN_DAMAGE = 4; // 2 coeurs
    private static final double PIERCING_DAWN_KNOCKBACK = 1.0;

    private final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>(); // Cooldowns pour compétences et commande

    private String lastGraceDate = "";  // Pour le reset journalier
    private int sunriseStartTick = 0;



    public Melina() {
        Bukkit.getServer().getPluginManager().registerEvents(this, org.bukkit.plugin.java.JavaPlugin.getPlugin(main.main.class));
        calculateSunriseTick();
    }

    public static void applyMelina(Player player) {
        PlayerData playerData = PlayerManager.getInstance().getPlayerData(player);
        if (playerData == null) {
            Bukkit.getLogger().warning("PlayerData est null pour " + player.getName());
            return;
        }

        playerData.setRole(Role.MELINA); // Melina a son propre rôle
        playerData.setCamp(Camp.BASTION_DE_LA_TABLE_RONDE); // Melina est dans le camp de la Table Ronde
        playerData.setGuidedByGraceState(PlayerData.GraceState.GUIDED); // Melina est déjà guidée.
        Bukkit.getLogger().info("Rôle Melina appliqué à " + player.getName());

        // Donner l'épée et la nether star au joueur
        player.getInventory().addItem(getPiercingDawnSword());
        player.getInventory().addItem(getAuraOfDawnStar());

        // Message informatif
        player.sendMessage(ChatColor.GOLD + "╔══════════════════════════════════════════╗");
        player.sendMessage(ChatColor.GOLD + "║               §eVous êtes §dMelina §e!                          ║");
        player.sendMessage(ChatColor.GOLD + "╠══════════════════════════════════════════╣");
        player.sendMessage(ChatColor.GRAY + " Camp : " + Camp.BASTION_DE_LA_TABLE_RONDE.getColor() + Camp.BASTION_DE_LA_TABLE_RONDE.getName());
        player.sendMessage(ChatColor.GRAY + " Pouvoirs :");
        player.sendMessage(ChatColor.GRAY + " - §6Cendre de l’Aube Perçante §7(Épée): Projette un rayon lumineux.");
        player.sendMessage(ChatColor.GRAY + " - §6Voile de l’Aube §7(Nether Star): Crée un bouclier protecteur.");
        player.sendMessage(ChatColor.GRAY + " - §6/melina_grace <nom_du_joueur> §7: Vérifie si un joueur est guidé.");
        player.sendMessage(ChatColor.GOLD + "╚══════════════════════════════════════════╝");

        if (!player.hasPotionEffect(PotionEffectType.SPEED)) {
            // Speed 1 (amplificateur 0) = 20% * (0 + 1) = 20% D'augmentation
            //donc on retire 10%, et ca donne bien 10% d'augmentation.
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (PlayerManager.getInstance().getPlayerRole(player) != Role.MELINA) return; // Changement ici
        PlayerData playerData = PlayerManager.getInstance().getPlayerData(player);
        if (playerData.getRole() != Role.MELINA) return; // et ici


        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (item != null && item.getType() == Material.IRON_SWORD && item.hasItemMeta() &&
                    item.getItemMeta().getDisplayName().equals("§6Cendre de l’Aube Perçante")) {
                usePiercingDawn(player);
            } else if (item != null && item.getType() == Material.NETHER_STAR && item.hasItemMeta() &&
                    item.getItemMeta().getDisplayName().equals("§6Voile de l’Aube")) {
                useAuraOfDawn(player);
            }
        }
    }


    private void usePiercingDawn(Player player) {
        if (!checkCooldown(player, "piercing_dawn")) return; //Cooldown
        if (ManaManager.getInstance().getMana(player) < PIERCING_DAWN_MANA_COST) { //Mana
            player.sendMessage(ChatColor.RED + "Mana Insuffisant !");
            return;
        }

        ManaManager.getInstance().consumeMana(player, PIERCING_DAWN_MANA_COST);
        setCooldown(player, "piercing_dawn", 20); // 20 secondes de cooldown.

        // Rayon
        Location start = player.getEyeLocation();
        Vector direction = start.getDirection().normalize();
        World world = player.getWorld();

        for (double i = 0; i < 10; i += 0.5) {
            Location point = start.clone().add(direction.clone().multiply(i));

            // Particules le long du trajet
            world.spawnParticle(Particle.END_ROD, point, 0, 0, 0, 0, 0.1);
            //Effet de scintillement (sinusoïdal)
            double offset = Math.sin(i * 5) * 0.2;  // Facteur de 5 pour augmenter la fréquence
            world.spawnParticle(Particle.END_ROD, point.clone().add(offset, offset, offset), 0, 0, 0, 0, 0.1);

            // Collision
            for (Entity entity : world.getNearbyEntities(point, 0.5, 0.5, 0.5)) {
                if (entity instanceof LivingEntity && entity != player) {
                    LivingEntity target = (LivingEntity) entity;

                    // Particules d'impact
                    world.spawnParticle(Particle.SPELL_INSTANT, target.getLocation().add(0, 1, 0), 30, 0.5, 0.5, 0.5, 0.2);

                    // Dégâts et knockback
                    target.damage(PIERCING_DAWN_DAMAGE, player);
                    target.setVelocity(direction.clone().multiply(PIERCING_DAWN_KNOCKBACK).setY(0.2)); // Y = 0.2 pour soulever

                    return; // Arrête après la première collision
                }
            }
        }

        // Son
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_CAST_SPELL, 1.0f, 1.0f);
        player.sendMessage(ChatColor.GREEN + "Vous avez utilisé Cendre de l'Aube Perçante.");
    }

    private void useAuraOfDawn(Player player) {
        if (!checkCooldown(player, "aura_of_dawn")) return; //Cooldown
        if (ManaManager.getInstance().getMana(player) < AURA_OF_DAWN_MANA_COST) {
            player.sendMessage(ChatColor.RED + "Pas assez de Mana !");
            return;
        }

        ManaManager.getInstance().consumeMana(player, AURA_OF_DAWN_MANA_COST);
        setCooldown(player, "aura_of_dawn", AURA_OF_DAWN_COOLDOWN);


        // Particules (bouclier)
        World world = player.getWorld();
        Location center = player.getLocation();
        new BukkitRunnable() {
            int ticks = 0;
            final int duration = 6 * 20; // 6 secondes

            @Override
            public void run() {
                if (ticks >= duration || !player.isOnline()) {
                    this.cancel();
                    return;
                }
                //Bouclier, création d'un cercle
                for (double i = 0; i < 360; i += 20) { //Pas
                    double angle = Math.toRadians(i);
                    double x = Math.cos(angle) * 2; //Rayon
                    double z = Math.sin(angle) * 2;
                    Location point = center.clone().add(x, 1, z);  //Y=1 pour que ca soit au bon niveau.

                    //Types de particules
                    world.spawnParticle(Particle.ENCHANTMENT_TABLE, point, 0); // Pas de données extra
                    world.spawnParticle(Particle.SPELL_WITCH, point, 0);

                    //Mouvement (rotation et élévation)
                    double yOffset = Math.sin(ticks * 0.1) * 0.5; // Oscillation
                    point.add(0, yOffset, 0);


                }
                //Knockback des ennemis
                for (Entity entity : world.getNearbyEntities(center, 3, 3, 3)) {
                    if (entity instanceof LivingEntity && entity != player) {
                        Vector direction = entity.getLocation().subtract(center).toVector().normalize();
                        entity.setVelocity(direction.multiply(0.5)); // Léger
                    }
                }


                ticks++;
            }
        }.runTaskTimer(org.bukkit.plugin.java.JavaPlugin.getPlugin(main.main.class), 0, 1);

        //Réduction des dégats
        new BukkitRunnable() {
            //Pas de variable
            @Override
            public void run() {
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 6 * 20, 0, false, false)); //Résistance 1
            }
        }.runTask(org.bukkit.plugin.java.JavaPlugin.getPlugin(main.main.class));

        // Son
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f);
        player.sendMessage(ChatColor.GREEN + "Vous avez utilisé Voile de l'Aube.");
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("melina_grace")) return false;

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Seul un joueur peut exécuter cette commande.");
            return true;
        }

        Player player = (Player) sender;

        // Vérification du rôle Melina *DIRECTEMENT*
        if (PlayerManager.getInstance().getPlayerRole(player) != Role.MELINA) { // Changement ici
            player.sendMessage(ChatColor.RED + "Seul Melina peut utiliser cette commande.");
            return true;
        }


        // Vérification des arguments
        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Utilisation: /melina_grace <nom_du_joueur>");
            return true;
        }

        // Vérification du cooldown et de la contrainte temporelle
        if (!checkGraceCommandAvailability(player)) {
            return true; // Le message d'erreur est déjà géré dans la fonction
        }

        // Réinitialisation du cooldown si nécessaire
        resetGraceCooldownIfNeeded();


        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Joueur introuvable ou hors ligne.");
            return true;
        }

        PlayerData targetData = PlayerManager.getInstance().getPlayerData(target);
        if (targetData == null) {
            Bukkit.getLogger().warning("PlayerData est null pour " + target.getName());
            return true;
        }
        if (isGraceEligible(targetData)) { //On vérifie si il a le bon rôle
            PlayerData.GraceState currentState = targetData.getGuidedByGraceState();
            String message;
            switch (currentState) {
                case GUIDED:
                    message = ChatColor.GREEN + target.getName() + " a déjà été guidé par la Grâce.";
                    break;
                case MAYBE_GUIDED:
                    message = ChatColor.YELLOW + target.getName() + " est peut-être guidé par la Grâce.";
                    break;
                case NOT_GUIDED:
                    message = ChatColor.RED + target.getName() + " n'est pas guidé par la Grâce.";
                    break;
                default:
                    message = ChatColor.RED + "Erreur: état de grâce inconnu."; // Ne devrait jamais arriver

            }
             player.sendMessage(message);


        } else {
            player.sendMessage(ChatColor.RED + target.getName() + " n'est pas éligible pour être guidé par la grace !");
        }

        setCooldown(player, "grace_command", 24 * 60 * 60); // 24 heures, géré par la fonction.
        return true;
    }


    /**
     * Vérifie si le joueur a le rôle requis pour être guidé par la Grâce.
     *
     * @param playerData Les données du joueur cible.
          * @param playerData Les données du joueur cible.
     * @return true si le joueur est éligible, false sinon.
     */
    private boolean isGraceEligible(PlayerData playerData) {
        Role role = playerData.getRole();
        return role == Role.SANS_ECLAT; // Seul les Sans-Éclats peuvent être guidés (Melina est déjà guidé de base, et a son propre rôle)
    }



    private boolean checkGraceCommandAvailability(Player player) {
        String currentTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        // Vérifier la contrainte de temps (lever du soleil + 3min30)
        World world = Bukkit.getWorld("UHC"); // Assurez-vous que "UHC" est le nom de votre monde
        if (world == null) {
            Bukkit.getLogger().severe("Le monde UHC n'existe pas !");
            return false; // Ou une autre gestion d'erreur
        }
        long worldTime = world.getTime();
        calculateSunriseTick(); //Pour être sur.

        if (!(worldTime >= sunriseStartTick && worldTime <= sunriseStartTick + (3 * 60 + 30) * 20)) { // Vérifie LEVER_DU_SOLEIL < TEMPS_DU_MONDE < LEVER_DU_SOLEIL + 3min30
            player.sendMessage(ChatColor.RED + "La commande /melina_grace ne peut être utilisée qu'au lever du soleil (max 3min30 après) !");
            return false;
        }

        // Vérifier le cooldown (géré par checkCooldown)
        return checkCooldown(player, "grace_command");
    }


    //Calcul le moment du lever du soleil
    private void calculateSunriseTick() {
        //Lever du soleil
        World world = Bukkit.getWorlds().get(0);

        long fullTime = world.getFullTime();

        long day = fullTime / 24000;

        this.sunriseStartTick = (int) ((day * 24000) + 23000);
        if (this.sunriseStartTick >= 24000) {
            this.sunriseStartTick -= 24000;
        }
    }

    /**
     * Vérifie si un cooldown est actif pour un joueur et une action donnés.
     * Gère les messages.
     *
     * @param player     Le joueur concerné.
     * @param actionName Le nom de l'action (compétence, commande, etc.).
     * @return true si le cooldown n'est pas actif ou n'existe pas, false si le cooldown est actif.
     */
    private boolean checkCooldown(Player player, String actionName) {
        UUID playerId = player.getUniqueId();

        if (!cooldowns.containsKey(playerId) || !cooldowns.get(playerId).containsKey(actionName)) {
            return true; // Pas de cooldown
        }

        long lastUsed = cooldowns.get(playerId).get(actionName);
        long cooldownDuration = getCooldownDuration(actionName); // Récupère dynamiquement
        long timeLeft = (lastUsed + cooldownDuration * 1000L - System.currentTimeMillis()) / 1000;

        if (timeLeft > 0) {
            player.sendMessage(ChatColor.RED + "Action '" + actionName + "' en cooldown. Temps restant : " + timeLeft + " secondes.");
            return false;
        }

        cooldowns.get(playerId).remove(actionName); // Nettoie le cooldown expiré
        return true;
    }

    /**
     * Définit ou met à jour un cooldown pour un joueur et une action donnés.
     *
     * @param player        Le joueur concerné.
     * @param actionName    Le nom de l'action (compétence, commande, etc.).
     * @param cooldownSeconds  La durée du cooldown, en secondes.
     */
    private void setCooldown(Player player, String actionName, int cooldownSeconds) {
        UUID playerId = player.getUniqueId();
        cooldowns.putIfAbsent(playerId, new HashMap<>());
        cooldowns.get(playerId).put(actionName, System.currentTimeMillis() + (cooldownSeconds * 1000L)); // Ajout correct
    }

    private long getCooldownDuration(String actionName) {
        switch (actionName) {
            case "piercing_dawn":
                return 20; // 20 secondes pour Piercing Dawn
            case "aura_of_dawn":
                return AURA_OF_DAWN_COOLDOWN;
            case "grace_command":
                return 24 * 60 * 60; // 24 heures pour /melina_grace
            default:
                return 0; // Pas de cooldown par défaut
        }
    }

    private void resetGraceCooldownIfNeeded() {
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        if (!today.equals(lastGraceDate)) {
            // Nouvelle journée, réinitialiser les cooldowns de la commande /melina_grace
            cooldowns.values().forEach(map -> map.remove("grace_command"));
            lastGraceDate = today; // Mettre à jour la date
        }
    }


    public static ItemStack getPiercingDawnSword() {
        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        ItemMeta meta = sword.getItemMeta();
        meta.setDisplayName("§6Cendre de l’Aube Perçante");
        meta.setLore(Arrays.asList(
                "§7Cendre de guerre : Projette un rayon lumineux qui inflige des dégâts et repousse les ennemis.",
                "§bCoût : " + PIERCING_DAWN_MANA_COST + " Mana",
                "§cDégâts : " + PIERCING_DAWN_DAMAGE / 2 + " coeurs",
                "§eCooldown : 20 secondes"
        ));
        sword.setItemMeta(meta);
        return sword;
    }

    public static ItemStack getAuraOfDawnStar() {
        ItemStack star = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = star.getItemMeta();
        meta.setDisplayName("§6Voile de l’Aube");
        meta.setLore(Arrays.asList(
                "§7Compétence : Crée un bouclier protecteur qui réduit les dégâts et repousse les ennemis.",
                "§bCoût : " + AURA_OF_DAWN_MANA_COST + " Mana",
                "§eCooldown : " + AURA_OF_DAWN_COOLDOWN + " secondes",
                "§aDurée : 6 secondes",
                "§9Réduction des dégâts : 40%"
        ));
        star.setItemMeta(meta);
        return star;
    }
}

