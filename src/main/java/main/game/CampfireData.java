package main.game;

import org.bukkit.Location;

public class CampfireData {
    private final Location location; // Emplacement du feu de camp
    private int charges;             // Nombre de charges restantes
    private boolean isLit;           // État du feu de camp (allumé ou éteint)

    /**
     * Constructeur pour un nouveau feu de camp.
     * @param location : L’emplacement du feu de camp.
     */
    public CampfireData(Location location) {
        this.location = location;
        this.charges = 6; // Réinitialisation à 6 charges
        this.isLit = true; // Par défaut, le feu de camp est allumé
    }

    /**
     * Éteindre le feu de camp.
     */
    public void extinguish() {
        this.isLit = false;
    }

    /**
     * Rallumer le feu de camp.
     */
    public void relight() {
        this.isLit = true;
    }

    /**
     * Réinitialiser les charges à 6.
     */
    public void resetCharges() {
        this.charges = 6;
    }

    /**
     * Réduire les charges du feu de camp.
     * @param amount : Le nombre de charges à retirer.
     */
    public void reduceCharges(int amount) {
        this.charges -= amount;
        if (this.charges < 0) { // Assurer que les charges ne deviennent pas négatives
            this.charges = 0;
        }
    }

    /**
     * Obtenir l’emplacement du feu de camp.
     * @return L’emplacement du feu de camp.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Obtenir le nombre de charges restantes.
     * @return Le nombre de charges restantes.
     */
    public int getCharges() {
        return charges;
    }

    /**
     * Savoir si le feu de camp est allumé.
     * @return true si le feu de camp est allumé, sinon false.
     */
    public boolean isLit() {
        return isLit;
    }
}
