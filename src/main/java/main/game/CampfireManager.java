package main.game;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CampfireManager {
    private final Map<Location, CampfireData> campfires = new HashMap<>();

    /**
     * Initialiser un nouveau feu de camp.
     * @param location : L'emplacement du feu de camp.
     */
    public void initCampfire(Location location) {
        if (!campfires.containsKey(location)) {
            campfires.put(location, new CampfireData(location));
        }
    }

    /**
     * Obtenir les données d'un feu de camp.
     * @param location : L'emplacement du feu de camp.
     * @return Les données du feu de camp, ou null s'il n'existe pas.
     */
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
