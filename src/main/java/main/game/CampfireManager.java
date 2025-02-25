package main.game;

import main.menus.CampfireMenu;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class CampfireManager implements Listener {

    private final JavaPlugin plugin;
    private final Map<Location, CampfireData> campfires = new HashMap<>(); // Stocke les feux de camps et leurs données
    private final Map<Player, Integer> estusCharges = new HashMap<>(); // Charges de fioles d'Estus par joueur
    private final Map<Player, Integer> manaCharges = new HashMap<>(); // Charges de fioles de Mana par joueur

    public CampfireManager(JavaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        startCampfireEffectChecker();
        startDayNightChecker();
    }

    // Démarre la vérification des effets des feux de camps
    private void startCampfireEffectChecker() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    applyCampfireEffects(player);
                }
            }
        }.runTaskTimer(plugin, 0, 20); // Exécuter toutes les secondes
    }

    // Démarre la vérification du jour et de la nuit
    private void startDayNightChecker() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (isNight()) {
                    extinguishCampfiresAtNight(); // Éteint les feux de camps la nuit
                } else {
                    relightCampfiresAtDay(); // Rallume les feux de camps au lever du jour
                }
            }
        }.runTaskTimer(plugin, 0, 20 * 60); // Vérifie toutes les minutes (1200 ticks = 1 minute)
    }

    // Rallume les feux de camps au lever du jour
    private void relightCampfiresAtDay() {
        for (Map.Entry<Location, CampfireData> entry : campfires.entrySet()) {
            CampfireData data = entry.getValue();
            if (!data.isLit()) {
                data.relight(); // Rallume le feu de camp
                // Effets visuels et sonores pour indiquer que le feu de camp est rallumé
                Location campfireLocation = entry.getKey();
                campfireLocation.getWorld().spawnParticle(org.bukkit.Particle.FLAME, campfireLocation, 10, 0.5, 0.5, 0.5, 0.1);
                campfireLocation.getWorld().playSound(campfireLocation, org.bukkit.Sound.BLOCK_FIRE_AMBIENT, 1.0f, 1.0f);
                Bukkit.getLogger().info("Le feu de camp à " + campfireLocation + " a été rallumé.");
            }
        }
    }

    // Éteint les feux de camps la nuit
    private void extinguishCampfiresAtNight() {
        for (Map.Entry<Location, CampfireData> entry : campfires.entrySet()) {
            CampfireData data = entry.getValue();
            if (data.isLit()) {
                data.extinguish();
                Bukkit.getLogger().info("Le feu de camp à " + entry.getKey() + " a été éteint.");
            }
        }
    }

    // Applique les effets des feux de camps aux joueurs proches
    private void applyCampfireEffects(Player player) {
        for (Map.Entry<Location, CampfireData> entry : campfires.entrySet()) {
            Location campfireLocation = entry.getKey();
            CampfireData data = entry.getValue();

            if (player.getLocation().distance(campfireLocation) <= 5 && data.isLit() && !isNight()) {
                // Régénération passive de santé (1hp/10sec)
                if (player.getHealth() < player.getMaxHealth()) {
                    player.setHealth(player.getHealth() + 1);
                }
            }
        }
    }

    // Gestion des interactions avec les feux de camps
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block clickedBlock = event.getClickedBlock();
            if (clickedBlock != null && clickedBlock.getType() == Material.CAMPFIRE) {
                handleCampfireInteraction(event.getPlayer(), clickedBlock.getLocation());
            }
        }
    }

    // Gère l'interaction avec un feu de camp
    private void handleCampfireInteraction(Player player, Location campfireLocation) {
        CampfireData campfireData = campfires.getOrDefault(campfireLocation, new CampfireData());

        if (campfireData.isLit()) {
            // Vérifie si le joueur est un demi-dieu ou un solitaire
            if (PlayerManager.isDemigodOrSolo(player)) {
                // Ouvrir le menu spécial pour les demi-dieux et solitaires
                CampfireMenu.openCampfireMenuForSoloOrDemigod(player);
            } else {
                // Ouvrir le menu normal pour les autres joueurs
                CampfireMenu.openCampfireMenu(player);
            }
        } else {
            player.sendMessage("§cLe feu de camp est éteint !");
        }
    }

    // Gestion des clics dans le menu
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("§6Feu de Camp")) {
            event.setCancelled(true); // Empêche de prendre les items

            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem == null || !clickedItem.hasItemMeta()) return;

            String itemName = clickedItem.getItemMeta().getDisplayName();

            // Gestion des choix du menu
            switch (itemName) {
                case "§cFiole d'Estus":
                    handleEstusChoice(player);
                    break;
                case "§bFiole de Mana":
                    handleManaChoice(player);
                    break;
                case "§dLes deux fioles":
                    handleBothChoice(player);
                    break;
                case "§6Retirer des charges":
                    handleChargeChoice(player);
                    break;
                case "§cQuitter":
                    player.closeInventory();
                    break;
            }
        }
    }

    // Applique le choix du joueur pour la fiole d'Estus
    private void handleEstusChoice(Player player) {
        if (estusCharges.getOrDefault(player, 0) < 2) {
            estusCharges.put(player, estusCharges.getOrDefault(player, 0) + 1);
            player.sendMessage("§aVous avez choisi une fiole d'Estus !");
        } else {
            player.sendMessage("§cVous avez déjà utilisé vos 2 charges d'Estus !");
        }
        player.closeInventory();
    }

    // Applique le choix du joueur pour la fiole de Mana
    private void handleManaChoice(Player player) {
        if (manaCharges.getOrDefault(player, 0) < 2) {
            manaCharges.put(player, manaCharges.getOrDefault(player, 0) + 1);
            player.sendMessage("§aVous avez choisi une fiole de Mana !");
        } else {
            player.sendMessage("§cVous avez déjà utilisé vos 2 charges de Mana !");
        }
        player.closeInventory();
    }

    // Applique le choix du joueur pour les deux fioles
    private void handleBothChoice(Player player) {
        if (estusCharges.getOrDefault(player, 0) < 2 && manaCharges.getOrDefault(player, 0) < 2) {
            estusCharges.put(player, estusCharges.getOrDefault(player, 0) + 1);
            manaCharges.put(player, manaCharges.getOrDefault(player, 0) + 1);
            player.sendMessage("§aVous avez choisi les deux fioles !");
        } else {
            player.sendMessage("§cVous avez déjà utilisé vos charges !");
        }
        player.closeInventory();
    }

    // Gère le choix des demi-dieux et solitaires pour retirer des charges
    private void handleChargeChoice(Player player) {
        // Ici, vous pouvez implémenter un système pour retirer un nombre spécifié de charges
        player.sendMessage("§6Vous pouvez retirer des charges.");
        player.closeInventory();
    }

    // Vérifie si c'est la nuit
    private boolean isNight() {
        long time = Bukkit.getWorld("world").getTime();
        return time > 13000 && time < 23000;
    }

    // Classe interne pour stocker les données d'un feu de camp
    private static class CampfireData {
        private int charges = 6; // Charges initiales
        private boolean lit = true; // État du feu de camp

        public int getCharges() {
            return charges;
        }

        public void removeCharge() {
            if (charges > 0) {
                charges--;
            }
        }

        public void addCharge(int amount) {
            charges += amount;
        }

        public boolean isLit() {
            return lit;
        }

        public void extinguish() {
            lit = false;
        }

        public void relight() {
            lit = true;
        }
    }
}
