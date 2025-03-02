// src/main/java/main/role/Melina.java
package main.role;

import main.game.*;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.*;

public class Melina implements CommandExecutor, Listener {

    private static final int LAME_DAPPEL_MANA_COST = 50;
    private static final int BENEDICTION_MANA_COST = 60;
    private static final int BENEDICTION_COOLDOWN = 600 * 20; // 10 minutes
    private final Map<UUID, Long> presenceCooldowns = new HashMap<>();
    private final Map<UUID, Set<UUID>> presenceUsed = new HashMap<>();
    private final JavaPlugin plugin;

    public Melina(JavaPlugin plugin)
    {
        this.plugin = plugin;
    }


    public static void applyMelina(Player player) {
        PlayerData playerData = PlayerManager.getInstance().getPlayerData(player);
        if (playerData == null) {
            Bukkit.getLogger().warning("PlayerData est null pour " + player.getName());
            return;
        }
        playerData.setRole(Role.MELINA);
        Bukkit.getLogger().info("Rôle Melina appliqué à " + player.getName());

        // Donner la Nether Star (Bénédiction)
        player.getInventory().addItem(getBenedictionStar());

        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        player.sendMessage(ChatColor.GOLD + "╔══════════════════════════════════════════╗");
        player.sendMessage(ChatColor.GOLD + "║               §6§lVous êtes Melina !               ║");
        player.sendMessage(ChatColor.GOLD + "╠══════════════════════════════════════════╣");
        player.sendMessage(ChatColor.GRAY + " Camp : " + ChatColor.GREEN + "Bastion de la Table Ronde");
        player.sendMessage(ChatColor.GRAY + " Description : L'alliée des Sans-Éclats, capable de guider et renforcer.");
        player.sendMessage(ChatColor.GRAY + " Pouvoirs :");
        player.sendMessage(ChatColor.GRAY + " - §3§lCendre de Guerre : Lame d'appel :§r Inflige des dégâts purs et marque.");
        player.sendMessage(ChatColor.GRAY + " - §3§lCompétence : Bénédiction de l'Arbre-Monde :§r Restaure points de mana et points de vie .");
        player.sendMessage(ChatColor.GRAY + " - §3§lCommande : /melina presence <joueur> <camp> :§r Information si un joueur est dans le camp.");
        player.sendMessage(ChatColor.GOLD + "╚══════════════════════════════════════════╝");
    }


    // Cendre de Guerre : Lame d'Appel
    public void useLameDAppel(Player player) {
        if (ManaManager.getInstance().getMana(player) < LAME_DAPPEL_MANA_COST) {
            player.sendMessage(ChatColor.RED + "Pas assez de Mana !");
            return;
        }
        ManaManager.getInstance().consumeMana(player, LAME_DAPPEL_MANA_COST);

        // 1. ANIMATION DE LA DAGUE (ItemDisplay + BukkitRunnable)
        final ItemDisplay dague = (ItemDisplay) player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.ITEM_DISPLAY); //Directement au niveau des yeux
        dague.setItemStack(new ItemStack(Material.GOLDEN_SWORD));

        // --- Transformation (IMPORTANT pour le visuel) ---
        Transformation transformation = dague.getTransformation();
        // Redimensionnement
        transformation.getScale().set(0.75f, 0.75f, 0.75f); // Légèrement plus petite

        // Rotation (légère inclinaison vers l'avant, comme si elle "flottait")
        transformation.getLeftRotation().set(new AxisAngle4f((float) Math.toRadians(-20), 1f, 0f, 0f)); // Inclinée vers l'avant
        transformation.getRightRotation().set(new AxisAngle4f((float) Math.toRadians(90), 0f, 1f, 0f));   // Tournée vers la droite (pour faire face à la direction du regard)
        dague.setTransformation(transformation);

        new BukkitRunnable() {
            int ticks = 0;
            final Location startLoc = player.getEyeLocation().clone(); // Emplacement de départ
            // AUCUN besoin de initialY ici, car on travaille avec un NOUVEAU Vector3f à chaque fois

            @Override
            public void run() {
                ticks++;

                if (ticks > 15) { // Durée totale: 15 ticks (0.75 secondes)
                    dague.remove();
                    this.cancel();
                    return;
                }

                // 1. Flottement (sinusoïdale)
                float newY = (float) (Math.sin(ticks * 0.5) * 0.1); // Calcule juste la *variation* de Y

                // 2. Mouvement vers l'AVANT
                Vector3f forward = new Vector3f(player.getLocation().getDirection().toVector3f());
                forward.mul(ticks * 0.05f);

                // 3. Créez un NOUVEAU Vector3f pour la translation TOTALE:
                //    Commencez par la position *initiale* (obtenue avec getTranslation()),
                //    puis ajoutez les modifications.
                Vector3f finalTranslation = new Vector3f(dague.getTransformation().getTranslation()); // POSITION INITIALE
                finalTranslation.add(0, newY, 0);   // Ajoute le flottement (y)
                finalTranslation.add(forward);       // Ajoute le mouvement vers l'avant

                // 4. Créez une NOUVELLE transformation:
                Transformation newTransformation = new Transformation(finalTranslation,
                        dague.getTransformation().getLeftRotation(),
                        dague.getTransformation().getScale(),
                        dague.getTransformation().getRightRotation());

                dague.setTransformation(newTransformation); // Applique la NOUVELLE transformation
            }
        }.runTaskTimer(plugin, 0L, 1L);


        // 2. PARTICULES
        new BukkitRunnable() {
            @Override
            public void run() {
                // CRIT_MAGIC (effet principal)
                for (int i = 0; i < 40; i++) { // Nombre de particules
                    Location loc = player.getLocation().add(Math.random() * 8 - 4, Math.random() * 2, Math.random() * 8 - 4); // Rayon de 4 blocs
                    player.getWorld().spawnParticle(Particle.CRIT_MAGIC, loc, 1, 0, 0, 0, 0.1); // Vitesse modérée
                }

                // SPELL_MOB (cercles concentriques)
                for (int r = 1; r <= 4; r++) { // Rayons de 1 à 4
                    for (int i = 0; i < 12; i++) { // Nombre de particules par cercle
                        double angle = 2 * Math.PI * i / 12;
                        Location loc = player.getLocation().add(r * Math.cos(angle), 0.5, r * Math.sin(angle));
                        player.getWorld().spawnParticle(Particle.SPELL_MOB, loc, 0, 0, 0, 0, 1,  new Particle.DustOptions(Color.fromRGB(255, 255, 240), 1)); // Blanc/doré pâle
                    }
                }
            }
        }.runTask(plugin); // Exécution immédiate


        // 3. SONS (légèrement décalés)
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.3f, 1f); // Pitch légèrement augmenté
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 0.5f, 1.9f); // Volume faible, pitch élevé
        }, 2L); // Délai de 2 ticks


        // 4. DÉGÂTS ET MARQUE SPECTRALE
        for (Entity entity : player.getNearbyEntities(4, 4, 4)) { // Rayon de 4 blocs
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;
                livingEntity.damage(3, player); // 1.5 cœurs de dégâts purs

                // Appliquer la marque (effet de potion + runnable)
                livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 40, 0, false, false)); // Brille pendant 2 secondes.
                livingEntity.setMetadata("MarqueSpectrale", new org.bukkit.metadata.FixedMetadataValue(plugin, true));

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        livingEntity.removeMetadata("MarqueSpectrale", plugin);
                    }
                }.runTaskLater(plugin, 40L); // 2 secondes (40 ticks).
            }
        }
    }

    // Méthode utilitaire pour vérifier si une entité est marquée
    public static boolean isMarked(LivingEntity entity, JavaPlugin plugin) {
        return entity.hasMetadata("MarqueSpectrale");
    }
    // Compétence Active : Bénédiction de l'Arbre-Monde
    public void useBenediction(Player player) {
        // Cooldown check
        if (player.hasMetadata("benediction_cooldown")) {
            long timeLeft = (player.getMetadata("benediction_cooldown").get(0).asLong() - System.currentTimeMillis()) / 1000; // Convertit en secondes
            if (timeLeft > 0) {
                player.sendMessage(ChatColor.RED + "Bénédiction de l'Arbre-Monde est en cooldown ! Temps restant : " + timeLeft + " secondes.");
                return;
            }
        }

        if (ManaManager.getInstance().getMana(player) < BENEDICTION_MANA_COST) {
            player.sendMessage(ChatColor.RED + "Pas assez de Mana !");
            return;
        }
        ManaManager.getInstance().consumeMana(player, BENEDICTION_MANA_COST);
        player.setMetadata("benediction_cooldown", new org.bukkit.metadata.FixedMetadataValue(plugin, System.currentTimeMillis() + BENEDICTION_COOLDOWN));

        // --- ANIMATION (début) ---
        // Particules (intenses)
        for (int i = 0; i < 60; i++) {
            Location loc = player.getLocation().add(Math.random() * 4 - 2, Math.random() * 2, Math.random() * 4 - 2);
            player.getWorld().spawnParticle(Particle.SPELL_MOB, loc, 0, 0, 0, 0, 1, new Particle.DustOptions(Color.fromRGB(255, 215, 0), 1)); // Doré
            player.getWorld().spawnParticle(Particle.SPELL_MOB, loc, 0, 0, 0, 0, 1, new Particle.DustOptions(Color.WHITE, 1)); //Et blanche
            if (i % 10 == 0) { // Moins de particules de cœur
                player.getWorld().spawnParticle(Particle.HEART, loc, 0, 0, 0.1, 0, 1);
            }
        }


        // Son
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f);


        // --- EFFETS (sur Melina et les alliés) ---
        int radius = 12;
        //Restauration instantané de mana:
        ManaManager.getInstance().setMana(player, ManaManager.getInstance().getMana(player) + 10);
        for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
            if (entity instanceof Player) {
                Player ally = (Player) entity;
                //On vérifie si l'allié fait bien partie du meme camp
                if (CampManager.getInstance().getPlayerCamp(ally).equals(CampManager.getInstance().getPlayerCamp(player))) {
                    //Effet de régénération de mana
                    ManaManager.getInstance().setMana(ally, ManaManager.getInstance().getMana(ally) + 10);
                    new BukkitRunnable() {
                        int ticks = 0;
                        @Override
                        public void run() {
                            if(ticks < 400)//Pendant 20 secondes
                            {
                                //Régénération de 1 mana par seconde
                                ManaManager.getInstance().setMana(ally, ManaManager.getInstance().getMana(ally) + 1);
                                ticks = ticks + 20;
                            }
                            else
                            {
                                cancel();
                            }
                        }
                    }.runTaskTimer(plugin,0L, 20L);

                    // --- ANIMATION (pendant la durée, subtile) ---
                    new BukkitRunnable() { //On fait une boucle
                        int ticks = 0;
                        @Override
                        public void run() {
                            if (ticks >= 400) { // 20 secondes (400 ticks)
                                this.cancel();
                                return;
                            }
                            // Particules sur l'allié
                            Location loc = ally.getLocation().add(Math.random() * 2 - 1, Math.random() * 2, Math.random() * 2 - 1);
                            ally.getWorld().spawnParticle(Particle.SPELL_MOB, loc, 0, 0, 0, 0, 1, new Particle.DustOptions(Color.fromRGB(255, 215, 0), 1)); // Doré
                            if (ticks % 40 == 0) { // Particule de cœur toutes les 2 secondes
                                ally.getWorld().spawnParticle(Particle.HEART, loc, 1, 0, 0.1, 0, 1);

                                // --- EFFET: Soin ---
                                if (ally.getHealth() < ally.getMaxHealth()) { //Vérification si il n'a pas toute sa vie
                                    double newHealth = Math.min(ally.getHealth() + 1, ally.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue());//Important pour que la vie ne dépasse pas son maximum
                                    ally.setHealth(newHealth);                              }
                            }

                            ticks++;
                        }
                    }.runTaskTimer(plugin, 20L, 1L); // Léger retard initial, puis très rapide

                    //Son subtile de cloche, chaque seconde
                    new BukkitRunnable()
                    {
                        int ticks = 0;

                        @Override
                        public void run() {
                            if(ticks >= 400) //Au bout des 20 secondes
                            {
                                cancel(); //On arrete
                                return;
                            }
                            //Son de cloche
                            player.getWorld().playSound(player.getLocation(),Sound.BLOCK_BELL_USE, 0.5f, 1f);
                            ticks = ticks + 20;

                        }
                    }.runTaskTimer(plugin, 0L, 20L);
                }
            }
        }
    }

    // Commande /melina presence
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Seul un joueur peut utiliser cette commande.");
            return true;
        }

        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();

        // Vérification du rôle Melina
        if (PlayerManager.getInstance().getPlayerRole(player) != Role.MELINA) {
            player.sendMessage("§cVous n'avez pas la permission d'utiliser cette commande.");
            return true;
        }
        //Vérification des arguments, si il y en a au moins 2
        if (args.length < 2) {
            player.sendMessage("§cUtilisation : /melina presence <joueur> <camp>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]); //A partir du premier argument
        String campName = args[1].toLowerCase(); //Deuxième argument, on met en minuscule

        //Si le joueur visé n'est pas en ligne
        if (target == null) {
            player.sendMessage("§cLe joueur spécifié n'est pas en ligne.");
            return true;
        }

        //Cooldown pour éviter les spams
        if (presenceCooldowns.containsKey(playerUUID)) {
            long timeLeft = (presenceCooldowns.get(playerUUID) - System.currentTimeMillis()) / 1000;
            if (timeLeft > 0) {
                player.sendMessage("§cVous devez attendre " + timeLeft + " secondes avant d'utiliser cette commande.");
                return true;
            }
        }

        // Vérification si la commande a déjà été utilisée sur la cible
        if (presenceUsed.containsKey(playerUUID) && presenceUsed.get(playerUUID).contains(target.getUniqueId())) {
            player.sendMessage("§c[Présence Spectrale] §r§4§lErreur : §r§fVous avez déjà utilisé Présence Spectrale sur ce joueur, vos sens sont encore troublés.");
            return true;
        }
        //Récupération des données du joueur
        main.game.PlayerData playerData = main.game.PlayerManager.getInstance().getPlayerData(target);

        boolean campMatch = false;
        //Différents cas, si le camp visé est un de ceux la
        switch (campName)
        {
            case "bastion": //Si c'est le cas
                campMatch = playerData.getCamp() == Camp.BASTION_DE_LA_TABLE_RONDE; //On vérifie si il est de ce camp
                break;
            case "demidieu":
                campMatch = playerData.getCamp() == Camp.DEMI_DIEUX;
                break;
            case "solitaire":
                campMatch = playerData.getCamp() == Camp.SOLITAIRE;
                break;
            default: //Si ce n'est pas un des cas
                player.sendMessage("§cCamp invalide. Utilisez : Bastion, DemiDieu, Solitaire.");
                return true;
        }

        // Message final (uniquement à Melina)
        if (campMatch) {
            player.sendMessage("§a[Présence Spectrale] §r§6§lConfirmation : §r§fLe joueur §e" + target.getName() + "§r§f semble résonner avec l'énergie du camp §e" + campName + "§r§f.");
        } else {
            player.sendMessage("§c[Présence Spectrale] §r§4§lIncertitude : §r§fLe joueur §e" + target.getName() + "§r§f ne semble pas lié au camp §e" + campName + "§r§f.");
        }

        // Ajouter la cible à la liste des joueurs vérifiés
        presenceUsed.computeIfAbsent(playerUUID, k -> new HashSet<>()).add(target.getUniqueId());

        // Définir le cooldown (5 secondes)
        presenceCooldowns.put(playerUUID, System.currentTimeMillis() + 5000);
        return true;
    }
    public static ItemStack getBenedictionStar() {
        ItemStack star = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = star.getItemMeta();
        meta.setDisplayName("§6Bénédiction de l'Arbre-Monde");
        meta.setLore(Arrays.asList(
                "§7Cliquez droit pour activer.",
                "§7Restaure le mana et les points de vie des alliés proches.",
                "§bCoût: 60 Mana",
                "§9Cooldown: 10 minutes"
        ));
        star.setItemMeta(meta);
        return star;
    }
}
