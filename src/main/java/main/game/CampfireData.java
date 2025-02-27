// src/main/java/main/game/CampfireData.java (Avec gestion de l'état)
package main.game;

import org.bukkit.Location;

public class CampfireData {
    private final Location location;
    private int charges;
    private boolean isLit; // Important pour gérer l'état allumé/éteint

    public CampfireData(Location location) {
        this.location = location;
        this.charges = 6;
        this.isLit = true; // Allumé par défaut
    }

    public boolean reduceCharges(int amount) {
        if (this.charges >= amount) {
            this.charges -= amount;
            if (this.charges <= 0) {
                this.charges = 0;  //Pour que ça passe pas en négatif
            }
            return true;
        }
        return false;
    }

    public void resetCharges() {
        this.charges = 6;
    }


    public void extinguish() {
        isLit = false;
    }


    public void relight() {
        isLit = true;
    }

    public Location getLocation() {
        return location;
    }

    public int getCharges() {
        return charges;
    }

    public boolean isLit() {
        return isLit;
    }
}
