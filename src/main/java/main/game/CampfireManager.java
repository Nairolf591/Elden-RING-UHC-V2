package main.game;

import main.game.CampfireData;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class CampfireManager {
    private Map<Location, CampfireData> campfires = new HashMap<>();
    private int nightCounter = 0; // Compteur de nuits pour la réinitialisation des charges

    /**
     * Initialiser un nouveau feu de camp.
     * @param location : L’emplacement du feu de camp.
     */
    public void initCampfire(Location location) {
        if (!campfires.containsKey(location)) {
            campfires.put(location, new CampfireData(location));
        }
    }

    /**
     * Début de la nuit : éteindre les feux de camp et gérer la réinitialisation des charges.
     */
    public void startNight() {
        nightCounter++;
        for (CampfireData campfire : campfires.values()) {
            campfire.extinguish(); // Éteindre le feu de camp
            if (nightCounter % 2 == 0) { // Réinitialiser les charges toutes les 2 nuits
                campfire.resetCharges();
            }
        }
    }

    /**
     * Début de la journée : rallumer les feux de camp.
     */
    public void startDay() {
        for (CampfireData campfire : campfires.values()) {
            campfire.relight(); // Rallumer le feu de camp
        }
    }

    /**
     * Réduire les charges d'un feu de camp.
     * @param location : L’emplacement du feu de camp.
     * @param amount : Le nombre de charges à retirer.
     */
    public void reduceCharges(Location location, int amount) {
        CampfireData campfire = campfires.get(location);
        if (campfire != null) {
            campfire.reduceCharges(amount);
        }
    }

    /**
     * Obtenir les données d'un feu de camp.
     * @param location : L’emplacement du feu de camp.
     * @return Les données du feu de camp (ou null si inexistant).
     */
    public CampfireData getCampfireData(Location location) {
        return campfires.get(location);
    }

    /**
     * Obtenir la map de tous les feux de camp.
     * @return Une map contenant tous les feux de camp (Location → CampfireData).
     */
    public Map<Location, CampfireData> getCampfires() {
        return campfires;
    }

    /**
     * Vérifier si un feu de camp existe à un emplacement donné.
     * @param location : L’emplacement à vérifier.
     * @return true si un feu de camp existe, sinon false.
     */
    public boolean hasCampfire(Location location) {
        return campfires.containsKey(location);
    }

    /**
     * Supprimer un feu de camp.
     * @param location : L’emplacement du feu de camp à supprimer.
     */
    public void removeCampfire(Location location) {
        campfires.remove(location);
    }
}
