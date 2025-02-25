package main.listeners;

import main.game.CampfireData;
import main.game.CampfireManager;
import main.game.PlayerFlasks;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.Location;

import java.util.Map;

public class CampfireListener implements Listener {
    private CampfireManager campfireManager;
    private Map<Player, PlayerFlasks> playerFlasksMap;

    /**
     * Constructeur pour initialiser les dépendances.
     * @param campfireManager : Le gestionnaire des feux de camp.
     * @param playerFlasksMap : La map des fioles des joueurs.
     */
    public CampfireListener(CampfireManager campfireManager, Map<Player, PlayerFlasks> playerFlasksMap) {
        this.campfireManager = campfireManager;
        this.playerFlasksMap = playerFlasksMap;
    }

    /**
     * Gérer l'interaction des joueurs avec un feu de camp.
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        // Vérifier si le bloc cliqué est un feu de camp
        if (block != null && block.getType() == Material.CAMPFIRE) {
            Location campfireLocation = block.getLocation();

            // Vérifier si le feu de camp est géré par CampfireManager
            if (campfireManager.hasCampfire(campfireLocation)) {
                CampfireData campfire = campfireManager.getCampfireData(campfireLocation);

                // Si le feu de camp est allumé, permettre au joueur de choisir une fiole
                if (campfire.isLit()) {
                    // Exemple : Ajouter une fiole d'Estus au joueur
                    PlayerFlasks playerFlasks = playerFlasksMap.get(player);
                    if (playerFlasks == null) {
                        playerFlasks = new PlayerFlasks();
                        playerFlasksMap.put(player, playerFlasks);
                    }
                    playerFlasks.addEstus(1);
                    player.sendMessage("§aVous avez récupéré une fiole d'Estus !");
                } else {
                    player.sendMessage("§cCe feu de camp est éteint.");
                }
            }
        }
    }
}
