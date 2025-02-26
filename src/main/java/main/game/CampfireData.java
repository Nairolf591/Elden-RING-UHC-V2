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
     * Retirer des charges du feu de camp.
     * @param amount : Le nombre de charges à retirer.
     * @return true si les charges ont été retirées, false si le feu de camp n'a pas assez de charges.
     */
    public boolean reduceCharges(int amount) {
        if (charges >= amount) {
            charges -= amount;
            return true;
        }
        return false;
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

    public boolean isLit() {
        return isLit;
    }
}
