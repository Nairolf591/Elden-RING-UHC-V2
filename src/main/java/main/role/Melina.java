package main.role;

import main.game.*;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Melina implements CommandExecutor, Listener {  // <--- Implémente Listener

    private final JavaPlugin plugin;
    private static final int LAME_DAPPEL_MANA_COST = 50;  // <--- Coût en mana
    private static final int BENEDICTION_MANA_COST = 60;
    private static final long BENEDICTION_COOLDOWN = 10 * 60 * 20; // 10 minutes en ticks (20 ticks/sec)

    public Melina(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("melina")) {
            if (args.length == 3 && args[0].equalsIgnoreCase("presence")) {
                Player target = Bukkit.getPlayer(args[1]);

                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "Joueur introuvable.");
                    return true;
                }
                String role = args[2];
                checkPresence(sender, target, role);

                return true; //  Important, après avoir géré la commande.
            } else {
                sender.sendMessage(ChatColor.RED + "Utilisation : /melina presence <joueur> <rôle>");
                return false;  // Indique une mauvaise utilisation
            }
        }
        return false;
    }


    public static void applyMelina(Player player) {
        PlayerData playerData = PlayerManager.getInstance().getPlayerData(player);
        if (playerData == null) {
            Bukkit.getLogger().warning("PlayerData est null pour " + player.getName());
            return;
        }

        playerData.setRole(Role.MELINA);
        Bukkit.getLogger().info("Rôle Melina appliqué à " + player.getName());

        // Donner la Nether Star
        player.getInventory().addItem(getCallingBlade());

        //Donner l'épée
        player.getInventory().addItem(getSpectralBlade());  // <--- CORRECTION : Ajout de getSpectralBlade()

        // ... (le reste de votre code pour les messages, etc.) ...
        // Envoyer un message de bienvenue
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        player.sendMessage(ChatColor.GOLD + "╔══════════════════════════════════════════╗");
        player.sendMessage(ChatColor.GOLD + "║               §eVous êtes §6Melina §e!                           ║");
        player.sendMessage(ChatColor.GOLD + "╠══════════════════════════════════════════╣");
        player.sendMessage(ChatColor.GRAY + " Camp : " + ChatColor.GREEN + "Bastion de la Table Ronde");
        player.sendMessage(ChatColor.GRAY + " Description : L'alliée des Sans-Éclats, capable de guider et renforcer leur pouvoir.");
        player.sendMessage(ChatColor.GRAY + " Pouvoirs :");
        player.sendMessage(ChatColor.GRAY + " - §3§lCendre de Guerre : Lame d'appel : §r Inflige des dégâts purs et marque brièvement les ennemis. (§e50 Mana§f)");
        player.sendMessage(ChatColor.GRAY + " - §3§lCompétence : Bénédiction de l'Arbre-Monde :§r Restaure points de mana et points de vie aux alliés proches. (§e60 Mana, 10min Cooldown§f)");
        player.sendMessage(ChatColor.GRAY + " - §3§lCommande : /melina presence <joueur> <camp> :§r Permet d'obtenir une information pour savoir si un joueur est dans le camp visé, utilisable chaque début de journée durant 2 minutes.");
        player.sendMessage(ChatColor.GOLD + "╚══════════════════════════════════════════╝");

    }

    //Ajout de la méthode pour créer la lame
    public static ItemStack getSpectralBlade()
    {
        ItemStack epee = new ItemStack(Material.GOLDEN_SWORD);
        ItemMeta meta = epee.getItemMeta();
        meta.setDisplayName("§3Lame Spectrale");
        meta.setLore(Arrays.asList(
                "§7Cendre de guerre : Inflige des dégâts purs et marque brièvement les ennemis.",
                "§7Les ennemis marqués prennent 10% de dégâts supplémentaires de toutes les sources pendant 5 secondes."
        ));
        epee.setItemMeta(meta);
        return epee;
    }


    // Nether Star pour la compétence principale
    public static ItemStack getCallingBlade() {
        ItemStack star = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = star.getItemMeta();
        meta.setDisplayName("§3§lLame d'appel");
        meta.setLore(Arrays.asList(
                "§7Compétence : Inflige des dégâts purs et marque les ennemis.",
                "§eCoût: " + LAME_DAPPEL_MANA_COST + " Mana"
        ));
        star.setItemMeta(meta);
        return star;
    }
    public static ItemStack getBenedictionArbreMonde() {
        ItemStack star = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = star.getItemMeta();
        meta.setDisplayName("§3§lBénédiction de l'Arbre-Monde");
        meta.setLore(Arrays.asList(
                "§7Compétence : Restaure 3 coeurs et 60 points de Mana aux alliés proches",
                "§eCoût: " + BENEDICTION_MANA_COST + " Mana",
                "§6Cooldown : 10 minutes"
        ));
        star.setItemMeta(meta);
        return star;
    }


    // Gestionnaire d'événement pour le clic droit sur la Nether Star
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();


        // Vérifier si le joueur a le rôle Melina
        if (PlayerManager.getInstance().getPlayerRole(player) != Role.MELINA) { //Condition if pour ne pas le faire avec tous les joueurs.
            return; // Si le joueur n'est pas Melina, sortir
        }

        //Cendre de Guerre
        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) &&
                item != null && item.getType() == Material.GOLDEN_SWORD && item.hasItemMeta() && //GOLDEN_SWORD, pas nether star
                item.getItemMeta().getDisplayName().equals("§3Lame Spectrale")) { // <--- TRÈS IMPORTANT: Vérifier le nom *exact*

            useLameDAppel(player);  // <--- Appeler la méthode lame d'appel
            event.setCancelled(true); //Important pour empecher l'action
        }

        // Vérifier si l'action est un clic droit avec la Nether Star spécifique
        else if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) &&
                item != null && item.getType() == Material.NETHER_STAR && item.hasItemMeta() &&
                item.getItemMeta().getDisplayName().equals("§3§lLame d'appel")) { // <--- TRÈS IMPORTANT: Vérifier le nom *exact*

            player.sendMessage("§cCette compétence a été remplacé par la Cendre de Guerre."); //On affiche un message comme quoi ça a été remplacé
        }
        //De même pour Bénédiction de l'Arbre-Monde
        else if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) &&
                item != null && item.getType() == Material.NETHER_STAR && item.hasItemMeta() &&
                item.getItemMeta().getDisplayName().equals("§3§lBénédiction de l'Arbre-Monde")) { // <--- TRÈS IMPORTANT: Vérifier le nom *exact*

            useBenedictionArbreMonde(player);  // <--- Appeler la méthode
        }
    }


    private void useLameDAppel(Player player) {
        if (ManaManager.getInstance().getMana(player) < LAME_DAPPEL_MANA_COST) {
            player.sendMessage(ChatColor.RED + "Pas assez de mana !");
            return;
        }
        ManaManager.getInstance().consumeMana(player, LAME_DAPPEL_MANA_COST);

        // Créer la dague (ItemDisplay)
        ItemDisplay dague = (ItemDisplay) player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.ITEM_DISPLAY);
        ItemStack dagueItem = new ItemStack(Material.GOLDEN_SWORD);
        ItemMeta meta = dagueItem.getItemMeta();

        meta.setDisplayName("§eLame d'Appel");
        dagueItem.setItemMeta(meta);
        dague.setItemStack(dagueItem);
        dague.setBillboard(Display.Billboard.CENTER); // La dague fait toujours face au joueur

        // Transformation initiale (pour la faire apparaître devant le joueur)
        Transformation transformation = dague.getTransformation();
        //On ajoute ça
        Vector3f translation = transformation.getTranslation();
        translation.add(new Vector3f(player.getLocation().getDirection().toVector3f()).mul(1.5f));
        transformation.getScale().set(0.5f);    //Taille
        //Rotation pour que ça soit vertical
        transformation.getLeftRotation().fromAxisAngleDeg(0, 0, 1, -90); // Rotation verticale

        // --- BukkitRunnable pour contrôler l'animation ---
        new BukkitRunnable() {
            int ticks = 0;
            final Location startLoc = player.getEyeLocation().clone(); // Emplacement de départ
            //final Vector3f initialTranslation = transformation.getTranslation().clone(); //Récupération, inutile avec cette technique
            final float initialY = (float)startLoc.getY();
            @Override
            public void run() {
                ticks++;

                if (ticks > 15) { // Durée totale: 15 ticks (0.75 secondes)
                    dague.remove();
                    this.cancel();
                    return;
                }

                // 1. Récupérer la Transformation *ACTUELLE*:
                Transformation currentTransform = dague.getTransformation();

                // 2. Obtenir la translation *SANS CLONER*:
                Vector3f currentTranslation = currentTransform.getTranslation(); // PAS de .clone()

                // 3.  Calculer la nouvelle position Y (flottement):

                float newY = (float) (initialY + (Math.sin(ticks * 0.5) * 0.1)); //Cast

                // 4. Calculer le mouvement vers l'avant :
                Vector3f forward = player.getLocation().getDirection().toVector3f();  // PAS besoin de new Vector3f(...)
                forward.mul(ticks * 0.05f);

                // 5.  ***MODIFIER DIRECTEMENT*** les composantes x, y, z
                currentTranslation.x = (float) (startLoc.getX() + forward.x);  //  X initial + forward, CAST
                currentTranslation.y = newY;                         //  Y flottant
                currentTranslation.z = (float) (startLoc.getZ() + forward.z);  //  Z initial + forward, Cast

                // 6.  On a modifié les valeurs directement, pas besoin de setTranslation
                dague.setTransformation(currentTransform); //On met juste la transformation
            }
        }.runTaskTimer(plugin, 0L, 1L);


        // --- BukkitRunnable pour la gestion des dégâts et de la marque ---
        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                ticks++;

                if (!dague.isValid() || ticks > 15) {  // La dague a disparu (animation terminée)
                    this.cancel();
                    return;
                }

                // --- Gestion des collisions et dégâts ---
                for (Player target : Bukkit.getOnlinePlayers()) {
                    if(target == player) continue; //Pour ne pas se taper sois-même
                    if (target.getLocation().distance(dague.getLocation()) < 1.5) { // Portée
                        target.damage(4, player); // 2 coeurs de dégâts *purs*
                        target.playSound(target.getLocation(),Sound.BLOCK_ANVIL_LAND, 1f, 1f);
                        // Marquer la cible (Metadata)
                        target.setMetadata("MarqueSpectrale", new FixedMetadataValue(plugin, true));

                        // Particules sur la cible
                        target.getWorld().spawnParticle(Particle.SPELL_WITCH, target.getLocation().add(0,1,0), 20, 0.5, 0.5, 0.5, 0);
                        target.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, target.getLocation().add(0,1,0), 10, 0.2, 0.2, 0.2, 0);


                        // Retirer la marque après 5 secondes
                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            if (target.isValid()) {  // Important de vérifier si la cible est toujours valide
                                target.removeMetadata("MarqueSpectrale", plugin);
                            }
                        }, 100L);  // 5 secondes * 20 ticks/seconde

                        dague.remove(); //On retire la dague
                        this.cancel(); //Important, pour ne pas continuer
                        break; // Une seule cible par coup.  Sort de la boucle *après* avoir appliqué les dégâts.
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 1L); // Exécuter toutes les ticks
    }
    public static boolean isMarked(LivingEntity target, JavaPlugin plugin) {
        return target.hasMetadata("MarqueSpectrale") && !target.getMetadata("MarqueSpectrale").isEmpty() && target.getMetadata("MarqueSpectrale").get(0).asBoolean();
    }


    private void useBenedictionArbreMonde(Player player)
    {
        if (isOnCooldown(player)) {
            long remainingSeconds = getCooldownRemaining(player) / 20;
            long minutes = remainingSeconds / 60;
            long seconds = remainingSeconds % 60;
            player.sendMessage(ChatColor.RED + "Bénédiction de l'Arbre-Monde est en cooldown ! Temps restant : " +
                    minutes + " minutes et " + seconds + " secondes.");
            return;
        }
        if (ManaManager.getInstance().getMana(player) < BENEDICTION_MANA_COST) {
            player.sendMessage(ChatColor.RED + "Pas assez de mana !");
            return;
        }

        ManaManager.getInstance().consumeMana(player, BENEDICTION_MANA_COST);
        setCooldown(player);

        double radius = 5.0;

        // Effets visuels pour Melina
        player.getWorld().spawnParticle(Particle.HEART, player.getLocation().add(0, 1, 0), 20, 0.5, 0.5, 0.5);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        //Son supplementaire
        player.playSound(player.getLocation(),Sound.BLOCK_BEACON_ACTIVATE, 1f, 1f);

        // Effets sur les alliés proches (ceux dans le même camp)
        for (Player ally : Bukkit.getOnlinePlayers()) {
            //Vérification (camp ET rayon)
            if (PlayerManager.getInstance().getPlayerCamp(ally) == PlayerManager.getInstance().getPlayerCamp(player) &&
                    ally.getLocation().distance(player.getLocation()) <= radius)
            {

                //Soigne de 3 coeurs
                AttributeInstance maxHealth = ally.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                if(maxHealth != null){
                    ally.setHealth(Math.min(ally.getHealth() + 6, maxHealth.getValue())); //Important de ne pas dépasser la vie max
                }

                ManaManager.getInstance().setMana(ally, ManaManager.getInstance().getMana(ally) + 60); //Restaure 60 mana
                ally.sendMessage("§aMelina vous a soigné et restauré votre mana !");
            }
        }
    }



    private void checkPresence(CommandSender sender, Player target, String roleName) {
        // 0. Vérifier si le joueur qui envoie la commande est Melina

        if (!(sender instanceof Player) || PlayerManager.getInstance().getPlayerRole((Player)sender) != Role.MELINA) {
            sender.sendMessage(ChatColor.RED + "Seul Melina peut utiliser cette commande !");
            return;
        }
        Player player = (Player) sender;  // <--- On cast en Player, car on sait que sender est un Player à partir d'ici



        //On ajoute les conditions pour l'utilisation de la commande :
        //Début de journée
        World world = player.getWorld();  //  <--- On utilise 'player'
        long currentTime = world.getTime();
        if (!(currentTime >= 0 && currentTime < 1000)) // Début du jour
        {
            sender.sendMessage(ChatColor.RED + "Vous ne pouvez utiliser cette commande qu'au début de la journée !");
            return;
        }

        //Metadata
        if(!player.hasMetadata("presence_check_available")){  // <--- On utilise 'player'
            player.setMetadata("presence_check_available", new FixedMetadataValue(plugin, true));  // <--- On utilise 'player'
        }

        //Cooldown
        long lastCheckTime = player.hasMetadata("last_presence_check") ? player.getMetadata("last_presence_check").get(0).asLong() : 0; // <--- On utilise 'player'


        // ... (le reste de votre code pour checkPresence, SANS modification) ...

        if (player.hasMetadata("presence_check_available") && player.getMetadata("presence_check_available").get(0).asBoolean()) { //Si y'a le metadata et qu'il est disponible

            // 1. Trouver le rôle correspondant au nom donné
            Role targetRole = null;
            for (Role role : Role.values()) {
                if (role.getName().equalsIgnoreCase(roleName)) {
                    targetRole = role;
                    break;
                }
            }

            if (targetRole == null) {
                sender.sendMessage(ChatColor.RED + "Rôle invalide !");
                return;
            }

            // 2. Comparer avec le rôle *réel* de la cible.  On utilise PlayerManager pour ça.
            if (PlayerManager.getInstance().getPlayerRole(target) == targetRole) {
                sender.sendMessage(ChatColor.GREEN + target.getName() + " est bien " + targetRole.getName() + ".");
            } else {
                sender.sendMessage(ChatColor.RED + target.getName() + " n'est pas " + targetRole.getName() + ".");
            }

            // 3. Désactiver la possibilité de faire un autre check, et stocker le timestamp.
            player.setMetadata("presence_check_available", new FixedMetadataValue(plugin, false));   // <--- On utilise 'player'
            player.setMetadata("last_presence_check", new FixedMetadataValue(plugin, System.currentTimeMillis()));  // <--- On utilise 'player'

            //4. Réactiver après 2 minutes
            new BukkitRunnable() {
                @Override
                public void run() {
                    if(sender instanceof Player){ //Important, pour empecher les erreurs avec la console
                        ((Player)sender).removeMetadata("presence_check_available", plugin); // <--- On cast, et on utilise sender
                    }
                }
            }.runTaskLater(plugin, 2400); // 2 minutes * 60 secondes/minute * 20 ticks/seconde

        } else { //Utilisation et cooldown
            long timeSinceLastCheck = (System.currentTimeMillis() - lastCheckTime) / 1000; // Temps écoulé en secondes
            sender.sendMessage(ChatColor.RED + "Vous avez déjà utilisé cette commande récemment. Veuillez attendre avant de la réutiliser.");

            if(timeSinceLastCheck < 120){ //Si c'est moins de 2 minutes, c'est juste qu'on vient de l'utiliser
                sender.sendMessage(ChatColor.RED + "La commande sera de nouveau disponible au début de la prochaine journée !");
            }
        }
    }


    private boolean isOnCooldown(Player player) {
        if (player.hasMetadata("benediction_cooldown")) {
            long cooldownEndTime = player.getMetadata("benediction_cooldown").get(0).asLong();
            return System.currentTimeMillis() < cooldownEndTime;
        }
        return false;
    }

    private long getCooldownRemaining(Player player) {
        if (player.hasMetadata("benediction_cooldown")) {
            long cooldownEndTime = player.getMetadata("benediction_cooldown").get(0).asLong();
            return Math.max(0, cooldownEndTime - System.currentTimeMillis()); //Pour pas de valeurs négatives
        }
        return 0;
    }

    private void setCooldown(Player player) {
        long cooldownEndTime = System.currentTimeMillis() + BENEDICTION_COOLDOWN; //Ajoute le cooldown
        player.setMetadata("benediction_cooldown", new FixedMetadataValue(plugin, cooldownEndTime));
    }
}