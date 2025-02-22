package main.command;

import main.game.GameManager;
import main.game.GameState;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class StopUHCCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Vérifier si la commande est exécutée par un joueur ou la console
        if (!(sender instanceof Player) && !sender.hasPermission("eldenringuhc.stop")) {
            sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande !");
            return true;
        }

        // Appeler la méthode pour arrêter l'UHC
        stopUHC();
        sender.sendMessage(ChatColor.GREEN + "L'UHC a été arrêté et tout a été réinitialisé !");
        return true;
    }

    private void stopUHC() {
        // Téléporter tous les joueurs dans le monde par défaut
        World defaultWorld = Bukkit.getWorld("world"); // Remplace "world" par ton monde par défaut
        if (defaultWorld == null) {
            Bukkit.broadcastMessage(ChatColor.RED + "Erreur : Le monde par défaut n'existe pas !");
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            // Téléporter le joueur dans le monde par défaut
            player.teleport(defaultWorld.getSpawnLocation());

            // Rétablir les stats par défaut
            player.setMaxHealth(20); // Réinitialiser la vie maximale à 20
            player.setHealth(20); // Vie maximale
            player.setFoodLevel(20); // Nourriture maximale
            player.setGameMode(GameMode.SURVIVAL);

            // Effacer les effets de potion
            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }

            // Vider l'inventaire
            player.getInventory().clear();
        }

// Réinitialiser le monde UHC (optionnel)
        World uhcWorld = Bukkit.getWorld("UHC");
        if (uhcWorld != null) {
            // Supprimer et recréer le monde UHC (attention : cela supprimera toutes les modifications)
            Bukkit.unloadWorld(uhcWorld, false);
            WorldCreator worldCreator = new WorldCreator("UHC"); // Créer un WorldCreator avec le nom "UHC"
            Bukkit.createWorld(worldCreator); // Recréer le monde


            // Rétablir l'état du jeu à CONFIG
            GameManager.getInstance().setCurrentState(GameState.CONFIG);

            // Notifier tous les joueurs
            Bukkit.broadcastMessage(ChatColor.GOLD + "L'UHC a été arrêté. Tout a été réinitialisé !");
        }
    }
}
