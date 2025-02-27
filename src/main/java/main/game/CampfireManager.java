// src/main/java/main/game/CampfireManager.java (Gestion complète)
package main.game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Campfire;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CampfireManager {
    private final Map<Location, CampfireData> campfires = new HashMap<>();
    private int nightCounter = 0; // Compteur pour la réinitialisation

    public void initCampfire(Location location) {
        if (!campfires.containsKey(location)) {
            campfires.put(location, new CampfireData(location));
            // Vérifie et met à jour l'état isLit en fonction de l'état réel du bloc
            Block block = location.getBlock();
            if (block.getType() == Material.CAMPFIRE) {
                Campfire campfire = (Campfire) block.getBlockData();
                if (campfire.isLit()) {
                    campfires.get(location).relight();
                } else {
                    campfires.get(location).extinguish();
                }
            }
        }
    }

    public void startNight() {
        nightCounter++;
        for (CampfireData campfireData : campfires.values()) {
            Location loc = campfireData.getLocation();
            Block block = loc.getBlock();
            if (block.getType() == Material.CAMPFIRE) {
                Campfire campfire = (Campfire) block.getBlockData(); // Cast en Campfire (DONNÉES)
                if (campfire.isLit()) {
                    campfire.setLit(false); //Désactivation VISUEL du feu
                    campfireData.extinguish(); //Extinction du feu logiquement
                    block.setBlockData(campfire); //Applique le changement
                }
            }
        }
    }


    public void startDay() {

        for (CampfireData campfireData : campfires.values()) {
            Location location = campfireData.getLocation();
            Block block = location.getBlock();
            if (block.getType() == Material.CAMPFIRE) {
                Campfire campfire = (Campfire) block.getBlockData();
                campfire.setLit(true); //Allumage VISUEL du feu
                campfireData.relight(); //Allumage LOGIQUE
                block.setBlockData(campfire);
                //Réinitialisation tout les 2 jours
                if (nightCounter % 2 == 0) {
                    campfireData.resetCharges();
                }
            }
        }
    }

    public boolean reduceCharges(Location location, int amount) {
        CampfireData campfire = campfires.get(location);
        return campfire != null && campfire.reduceCharges(amount);
    }


    public CampfireData getCampfireData(Location location) {
        return campfires.get(location);
    }

    public boolean hasCampfire(Location location) {
        return campfires.containsKey(location);
    }


    public Set<Location> getCampfireLocations() {
        return campfires.keySet();
    }
}
