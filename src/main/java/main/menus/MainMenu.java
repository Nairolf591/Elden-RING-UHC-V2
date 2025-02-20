package main.menus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class MainMenu {

    public static final String TITLE = "§6§lElden Ring UHC §r§fMenu Principal";

    public static void open(Player player) {
        // Créer l'inventaire de menu
        Inventory menu = Bukkit.createInventory(null, 9, TITLE);

        // Bouton Lancer la partie
        ItemStack startGame = new ItemStack(Material.GREEN_BANNER);
        ItemMeta startGameMeta = startGame.getItemMeta();
        startGameMeta.setDisplayName("§a§lLancer la partie");
        startGameMeta.setLore(Arrays.asList("§7Cliquez pour commencer le UHC !"));
        startGame.setItemMeta(startGameMeta);
        menu.setItem(0, startGame);

        // Bouton Configurer la bordure
        ItemStack borderConfig = new ItemStack(Material.BEACON);
        ItemMeta borderConfigMeta = borderConfig.getItemMeta();
        borderConfigMeta.setDisplayName("§6§lConfigurer la bordure");
        borderConfigMeta.setLore(Arrays.asList("§7Cliquez pour configurer la bordure."));
        borderConfig.setItemMeta(borderConfigMeta);
        menu.setItem(2, borderConfig);

        // Bouton Configurer les rôles
        ItemStack rolesConfig = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta rolesConfigMeta = rolesConfig.getItemMeta();
        rolesConfigMeta.setDisplayName("§b§lConfigurer les rôles");
        rolesConfigMeta.setLore(Arrays.asList("§7Cliquez pour configurer les rôles."));
        rolesConfig.setItemMeta(rolesConfigMeta);
        menu.setItem(4, rolesConfig);

        // Bouton Configurer le stuff
        ItemStack stuffConfig = new ItemStack(Material.CHEST);
        ItemMeta stuffConfigMeta = stuffConfig.getItemMeta();
        stuffConfigMeta.setDisplayName("§e§lConfigurer le stuff");
        stuffConfigMeta.setLore(Arrays.asList("§7Cliquez pour configurer le stuff."));
        stuffConfig.setItemMeta(stuffConfigMeta);
        menu.setItem(6, stuffConfig);

        // Bouton Configuration Avancée
        ItemStack advancedConfig = new ItemStack(Material.REDSTONE);
        ItemMeta advancedConfigMeta = advancedConfig.getItemMeta();
        advancedConfigMeta.setDisplayName("§c§lConfiguration Avancée");
        advancedConfigMeta.setLore(Arrays.asList("§7Cliquez pour ouvrir le menu de configuration avancée."));
        advancedConfig.setItemMeta(advancedConfigMeta);
        menu.setItem(8, advancedConfig);

        // Ouvrir le menu au joueur
        player.openInventory(menu);

        // Jouer un son
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
    }

}
