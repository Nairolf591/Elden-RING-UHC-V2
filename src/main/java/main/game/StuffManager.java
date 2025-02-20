package main.game;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class StuffManager {

    private static StuffManager instance;
    private ItemStack[] defaultStuff; // Stuff configuré par défaut
    private Map<Player, ItemStack[]> playerInventories; // Sauvegarde des inventaires des joueurs

    private StuffManager() {
        defaultStuff = new ItemStack[36]; // Taille de l'inventaire du joueur
        playerInventories = new HashMap<>();
        initializeDefaultStuff(); // Initialiser le stuff de base
    }

    public static StuffManager getInstance() {
        if (instance == null) {
            instance = new StuffManager();
        }
        return instance;
    }

    // Définir le stuff par défaut (base UHC)
    public void setDefaultStuff(ItemStack[] stuff) {
        this.defaultStuff = stuff;
    }

    // Obtenir le stuff configuré
    public ItemStack[] getDefaultStuff() {
        return defaultStuff;
    }

    // Appliquer le stuff configuré au joueur
    public void applyStuff(Player player) {
        PlayerInventory inventory = player.getInventory();
        playerInventories.put(player, inventory.getContents()); // Sauvegarder l'inventaire actuel
        inventory.setContents(defaultStuff); // Remplacer par le stuff configuré
    }

    // Confirmer et sauvegarder le stuff modifié
    public void confirmStuff(Player player) {
        if (playerInventories.containsKey(player)) {
            // Sauvegarder le nouveau stuff
            defaultStuff = player.getInventory().getContents();

            // Restaurer l'inventaire d'origine du joueur
            player.getInventory().setContents(playerInventories.remove(player));

            // Notifier le joueur
            player.sendMessage("§aLe stuff par défaut a été mis à jour !");
        } else {
            player.sendMessage("§cTu n'as pas commencé à modifier le stuff.");
        }
    }

    // Annuler la modification et rendre l'ancien inventaire
    public void cancelStuff(Player player) {
        if (playerInventories.containsKey(player)) {
            player.getInventory().setContents(playerInventories.get(player));
            playerInventories.remove(player);
            player.sendMessage("§cModification du stuff annulée.");
        }


    }

        // Initialiser le stuff de base UHC
        private void initializeDefaultStuff () {
            // Épée en diamant
            defaultStuff[0] = new ItemStack(Material.DIAMOND_SWORD);

            // Arc
            defaultStuff[1] = new ItemStack(Material.BOW);

            // Flèches (64)
            defaultStuff[2] = new ItemStack(Material.ARROW, 64);

            // Pioche en diamant
            defaultStuff[3] = new ItemStack(Material.DIAMOND_PICKAXE);

            // Hache en diamant
            defaultStuff[4] = new ItemStack(Material.DIAMOND_AXE);

            // Blocs de cobblestone (64)
            defaultStuff[5] = new ItemStack(Material.COBBLESTONE, 64);

            // Blocs de bûches (64)
            defaultStuff[6] = new ItemStack(Material.OAK_LOG, 64);

            // Casque en fer
            defaultStuff[7] = new ItemStack(Material.IRON_HELMET);

            // Plastron en diamant
            defaultStuff[8] = new ItemStack(Material.DIAMOND_CHESTPLATE);

            // Jambières en fer
            defaultStuff[9] = new ItemStack(Material.IRON_LEGGINGS);

            // Bottes en diamant
            defaultStuff[10] = new ItemStack(Material.DIAMOND_BOOTS);

            // Pommes d'or (16)
            defaultStuff[11] = new ItemStack(Material.GOLDEN_APPLE, 16);

            // Seaux d'eau (2)
            defaultStuff[12] = new ItemStack(Material.WATER_BUCKET);
            defaultStuff[13] = new ItemStack(Material.WATER_BUCKET);

            // Carottes dorées (64)
            defaultStuff[14] = new ItemStack(Material.GOLDEN_CARROT, 64);

            // Remplir les emplacements vides (optionnel)
            for (int i = 1; i < 36; i++) {
                if (defaultStuff[i] == null) {
                    defaultStuff[i] = new ItemStack(Material.AIR);
                }
            }
        }
}
