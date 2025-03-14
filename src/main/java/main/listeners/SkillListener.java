package main.listeners;

import main.menus.BonusSkillMenu;
import main.role.Morgott;
import main.skills.*;
import main.menus.ClassSelectionMenu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class SkillListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        // Vérifie si l'action est un clic droit
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            // Vérifie si l'item est une arme de classe
            if (item != null && item.getType().toString().contains("_SWORD")) {
                PlayerClass playerClass = SkillManager.getInstance().getPlayerClass(player);
                if (playerClass != null) {
                    event.setCancelled(true); // Empêcher l'utilisation normale de l'item
                    playerClass.useSkill(player); // Utiliser la cendre de guerre
                }
            }
        }

        // Vérifie si l'item est une Nether Star de sélection de classe
        if (item != null && item.getType() == Material.NETHER_STAR && item.hasItemMeta() &&
                item.getItemMeta().getDisplayName().equals("§a§lChoisir votre classe")) {
            event.setCancelled(true);
            ClassSelectionMenu.open(player); // Ouvrir le menu de sélection de classe
        }

        // Vérifie si l'item est une Nether Star de compétence bonus
        if (item != null && item.getType() == Material.NETHER_STAR && item.hasItemMeta()) {
            String skillName = item.getItemMeta().getDisplayName();

            long remaining = SkillManager.getInstance().getCooldownRemaining(player);
            if (SkillManager.getInstance().isOnCooldown(player)) {
                player.sendMessage(ChatColor.RED + "Compétence en cooldown ! Temps restant : " + (remaining / 1000) + " secondes.");
                return;
            }

            switch (skillName) {
                case "§4Flamme noire":
                    new FlammeNoire().useSkill(player);
                    SkillManager.getInstance().setCooldown(player); // Définir le cooldown
                    break;

                case "§eVague de lames":
                    new VagueDeLames().useSkill(player);
                    SkillManager.getInstance().setCooldown(player); // Définir le cooldown
                    break;

                case "§bTempête de givre":
                    new TempeteDeGivre().useSkill(player);
                    SkillManager.getInstance().setCooldown(player); // Définir le cooldown
                    break;
            }
        }

        // Vérifie si l'item est la Nether Star de compétence bonus
        if (item != null && item.getType() == Material.NETHER_STAR && item.hasItemMeta() &&
                item.getItemMeta().getDisplayName().equals("§a§lCompétence bonus")) {
            event.setCancelled(true);

            // Vérifie si le joueur a déjà une compétence bonus
            if (hasBonusSkill(player)) {
                player.sendMessage("§cVous avez déjà choisi une compétence bonus !");
                return;
            }

            // Ouvre le menu de sélection des compétences bonus
            BonusSkillMenu.open(player);
        }
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (item != null && item.getType() == Material.DIAMOND_SWORD && item.hasItemMeta() &&
                    item.getItemMeta().getDisplayName().equals("§4Lame Maudite de Morgott")) {
                Morgott.useLameMaudite(player);
            }
        }

// Ajouter un nouvel item pour Courroux de l'Omen si nécessaire
        if (item != null && item.getType() == Material.NETHER_STAR && item.hasItemMeta() && item.getItemMeta().getDisplayName().equals("§4Courroux de l'Omen")) {
            long remainingCooldown = SkillManager.getInstance().getCooldownRemaining(player);

            if (remainingCooldown > 0) {
                // Afficher le temps restant
                long minutes = remainingCooldown / 60;
                long seconds = remainingCooldown % 60;
                player.sendMessage(ChatColor.RED + "Compétence en cooldown ! Temps restant : " + minutes + "m " + seconds + "s");
            } else {
                // Activer la compétence
                Morgott.useCourrouxOmen(player);
                SkillManager.getInstance().setCooldown(player);
                player.sendMessage(ChatColor.GREEN + "Vous avez utilisé Courroux de l'Omen !");
            }

            event.setCancelled(true); // Annuler l'événement pour éviter des comportements indésirables
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("§6§lChoisissez votre classe")) {
            event.setCancelled(true); // Empêcher le joueur de prendre les items
            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem == null || !clickedItem.hasItemMeta()) return;

            String itemName = clickedItem.getItemMeta().getDisplayName();

            // Vérifier si le joueur a déjà une classe
            if (SkillManager.getInstance().getPlayerClass(player) != null) {
                player.sendMessage(ChatColor.RED + "Vous avez déjà choisi une classe !");
                return;
            }

            switch (itemName) {
                case "§6Grand Espadon":
                    SkillManager.getInstance().assignClass(player, new GrandEspadon());
                    player.sendMessage("§aVous avez choisi la classe : Grand Espadon");
                    break;

                case "§eNagakiba":
                    SkillManager.getInstance().assignClass(player, new Nagakiba());
                    player.sendMessage("§aVous avez choisi la classe : Nagakiba");
                    break;

                case "§dBâton d'éclat de Lusat":
                    SkillManager.getInstance().assignClass(player, new BatonEclatLusat());
                    player.sendMessage("§aVous avez choisi la classe : Bâton d'éclat de Lusat");
                    break;

                case "§cFermer":
                    player.closeInventory();
                    break;
            }
            player.closeInventory();
        }

        // Gestion du menu de sélection des compétences bonus
        if (event.getView().getTitle().equals("§6§lChoisissez une compétence bonus")) {
            event.setCancelled(true); // Empêcher le joueur de prendre les items
            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem == null || !clickedItem.hasItemMeta()) return;

            String itemName = clickedItem.getItemMeta().getDisplayName();

            switch (itemName) {
                case "§4Flamme noire":
                    giveBonusSkillItem(player, "§4Flamme noire");
                    break;

                case "§eVague de lames":
                    giveBonusSkillItem(player, "§eVague de lames");
                    break;

                case "§bTempête de givre":
                    giveBonusSkillItem(player, "§bTempête de givre");
                    break;

                case "§cFermer":
                    player.closeInventory();
                    break;
            }
            player.closeInventory();
        }
    }

    /**
     * Vérifie si le joueur a déjà une compétence bonus.
     */
    private boolean hasBonusSkill(Player player) {
        for (ItemStack item : player.getInventory()) {
            if (item != null && item.getType() == Material.NETHER_STAR && item.hasItemMeta() &&
                    (item.getItemMeta().getDisplayName().equals("§4Flamme noire") ||
                            item.getItemMeta().getDisplayName().equals("§eVague de lames") ||
                            item.getItemMeta().getDisplayName().equals("§bTempête de givre"))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Donne une Nether Star renommée pour utiliser la compétence bonus choisie.
     */
    private void giveBonusSkillItem(Player player, String skillName) {
        ItemStack skillItem = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = skillItem.getItemMeta();
        meta.setDisplayName(skillName);

        // Ajouter des informations sur la compétence
        switch (skillName) {
            case "§4Flamme noire":
                meta.setLore(Arrays.asList(
                        "§7Applique un effet de faiblesse et met les ennemis en feu.",
                        "§bCoût : 60 Mana",
                        "§cDurée : 2 secondes",
                        "§aCliquez pour activer."
                ));
                break;
            case "§eVague de lames":
                meta.setLore(Arrays.asList(
                        "§7Inflige des dégâts dans une zone.",
                        "§bCoût : 50 Mana",
                        "§cDégâts : 2 cœurs (4 points)",
                        "§aCliquez pour activer."
                ));
                break;
            case "§bTempête de givre":
                meta.setLore(Arrays.asList(
                        "§7Applique un effet de lenteur aux ennemis.",
                        "§bCoût : 70 Mana",
                        "§cDurée : 5 secondes",
                        "§aCliquez pour activer."
                ));
                break;
        }

        skillItem.setItemMeta(meta);
        player.getInventory().addItem(skillItem);
        player.sendMessage("§aVous avez choisi la compétence bonus : " + skillName);
    }
}
