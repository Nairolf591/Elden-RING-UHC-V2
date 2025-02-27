package main.game;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Campfire; // Interface de DONNÉES, pas de bloc

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import main.game.*;

public class CampfireManager {
    private final Map<Location, CampfireData> campfires = new HashMap<>();
    private int nightCounter = 0; // Compteur de nuits

    /**
     * Initialiser un nouveau feu de camp.
     * @param location : L'emplacement du feu de camp.
     */
    public void initCampfire(Location location) {
        if (!campfires.containsKey(location)) {
            campfires.put(location, new CampfireData(location));

            Block block = location.getBlock();
            if (block.getType() == Material.CAMPFIRE) {
                Campfire campfireData = (Campfire) block.getBlockData(); // Cast en Campfire (DONNÉES)
                if (campfireData.isLit()) {
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
            Location location = campfireData.getLocation();
            Block block = location.getBlock();
            if (block.getType() == Material.CAMPFIRE) {
                Campfire campfire = (Campfire) block.getBlockData();// Cast en Campfire (DONNÉES)
                if (campfire.isLit()) {
                    campfire.setLit(false);
                    campfireData.extinguish();
                    block.setBlockData(campfire); // Appliquer les changements au bloc
                }
            }
        }
    }

    //Appel de la méthode pour réinitialiser/rallumer les feux de camps
    public void startDay() {
        for (CampfireData campfireData : campfires.values()) {
            Location location = campfireData.getLocation();
            Block block = location.getBlock();
            if (block.getType() == Material.CAMPFIRE) {
                Campfire campfire = (Campfire) block.getBlockData();// Cast en Campfire (DONNÉES)
                if (!campfire.isLit()) {
                    campfire.setLit(true);
                    campfireData.relight();
                    if (nightCounter % 2 == 0) {
                        campfireData.resetCharges();
                    }
                    block.setBlockData(campfire); // Appliquer les changements au bloc
                }
            }
        }
    }


    // Réduit les charges d'un feu de camp.  Retourne true si la réduction a réussi, false sinon.
    public boolean reduceCharges(Location location, int amount) {
        CampfireData campfire = campfires.get(location);
        if (campfire != null) {

            if(campfire.getCharges() >= amount){
                campfire.reduceCharges(amount);
                return true; //Retourne si la réduction a été effectué
            }
        }
        return false;
    }

    // Méthodes utilitaires (pour simplifier le code ailleurs)
    public CampfireData getCampfireData(Location location) {
        return campfires.get(location);
    }

    /**
     * Vérifier si un feu de camp existe à un emplacement donné.
     * @param location : L'emplacement à vérifier.
     * @return true si le feu de camp existe, false sinon.
     */
    public boolean hasCampfire(Location location) {
        return campfires.containsKey(location);
    }

    /**
     * Réinitialiser les charges de tous les feux de camp.
     */
    public void resetAllCharges() {
        for (CampfireData campfire : campfires.values()) {
            campfire.resetCharges();
        }
    }

    /**
     * Obtenir les emplacements de tous les feux de camp enregistrés.
     * @return Un Set contenant les emplacements des feux de camp.
     */
    public Set<Location> getCampfireLocations() {
        return campfires.keySet();
    }
}
