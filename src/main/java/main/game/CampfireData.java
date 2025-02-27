package main.game;

import org.bukkit.Location;

public class CampfireData {
    private final Location location; // Emplacement du feu de camp
    private int charges;             // Nombre de charges restantes
    private boolean isLit;           // État du feu de camp (allumé ou éteint)

    public CampfireData(Location location) {
        this.location = location;
        this.charges = 6; // Initialiser à 6 charges
        this.isLit = true; // Par défaut, allumé
    }


    /**
     * Réinitialiser les charges à 6.
     */
    public void resetCharges() {
        charges = 6;
    }

    /**
     * Éteindre le feu de camp.
     */
    public void extinguish() {
        isLit = false;
    }

    /**
     * Rallumer le feu de camp.
     */
    public void relight() {
        isLit = true;
    }

    // Getters
    public Location getLocation() {
        return location;
    }

    public int getCharges() {
        return charges;
    }

    /**
     * Réduire les charges du feu de camp.
     * @param amount : Le nombre de charges à retirer.
     */
    public boolean reduceCharges(int amount) {
        //Pas de if (this.charges >= amount), la condition est testé dans la méthode de CampfireManager
        this.charges -= amount;
        if (this.charges < 0) {
            this.charges = 0;
        }
        return true;
    }

    public boolean isLit() {
        return isLit;
    }
}
