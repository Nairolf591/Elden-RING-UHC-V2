package main.command;

import main.listeners.ConfigurationCompassListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import main.EldenRingUHCScoreboard;

public class SetHostCommand implements CommandExecutor {

    private EldenRingUHCScoreboard scoreboardManager;
    private String hostName;

    public SetHostCommand(EldenRingUHCScoreboard scoreboardManager) {
        this.scoreboardManager = scoreboardManager;
        this.hostName = "Kayou"; // Valeur par défaut
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Vérifier si l'expéditeur est un joueur et a la permission d'opérateur
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Seul un joueur peut exécuter cette commande.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.isOp()) {
            player.sendMessage(ChatColor.RED + "Vous devez être opérateur pour utiliser cette commande.");
            return true;
        }

        // Vérifier si un argument (le pseudo de l'hôte) est fourni
        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Utilisation: /sethote <joueur>");
            return true;
        }

        // Récupérer le pseudo de l'hôte
        String newHostName = args[0];
        Player hostPlayer = Bukkit.getPlayer(newHostName);

        // Vérifier si le joueur est en ligne
        if (hostPlayer == null) {
            player.sendMessage(ChatColor.RED + "Le joueur " + newHostName + " n'est pas en ligne.");
            return true;
        }

        // Définir l'hôte
        this.hostName = newHostName;
        player.sendMessage(ChatColor.GREEN + "L'hôte est maintenant " + newHostName + ".");
        ConfigurationCompassListener.giveConfigurationCompass(hostPlayer);

        // Mettre à jour le nom de l'hôte dans le scoreboard
        scoreboardManager.updateHostName(newHostName);

        // Envoyer un message de confirmation
        sender.sendMessage("§aL'hôte a été défini comme " + newHostName + " !");
        return true;
    }

    public String getHostName() {
        return hostName;
    }

}
