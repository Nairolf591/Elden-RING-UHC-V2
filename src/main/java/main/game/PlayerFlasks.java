package main.game;

import org.bukkit.entity.Player;

public class PlayerFlasks {
    private int estus; // Nombre de fioles d'Estus
    private int mana;  // Nombre de fioles de Mana
    private boolean hasChargesLeft; // Indique si le joueur a des charges restantes dans son dernier feu de camp

    /**
     * Constructeur par défaut. Initialise les fioles à 0.
     */
    public PlayerFlasks() {
        this.estus = 0;
        this.mana = 0;
        this.hasChargesLeft = false;
    }

    /**
     * Utiliser une fiole d'Estus.
     * @param player : Le joueur qui utilise la fiole.
     */
    public void useEstus(Player player) {
        if (estus > 0) {
            estus--; // Retirer une fiole
            double health = player.getHealth();
            player.setHealth(Math.min(health + 6, 20)); // Soigner 3 cœurs (6 points de vie)
            player.sendMessage("§aVous avez utilisé une fiole d'Estus !");
        } else {
            player.sendMessage("§cVous n'avez plus de fioles d'Estus.");
        }
    }

    /**
     * Utiliser une fiole de Mana.
     * @param player : Le joueur qui utilise la fiole.
     */
    public void useMana(Player player) {
        if (mana > 0) {
            mana--; // Retirer une fiole
            int currentMana = ManaManager.getInstance().getMana(player);
            ManaManager.getInstance().setMana(player, Math.min(currentMana + 90, 100)); // Restaurer 90 mana
            player.sendMessage("§aVous avez utilisé une fiole de Mana !");
        } else {
            player.sendMessage("§cVous n'avez plus de fioles de Mana.");
        }
    }

    /**
     * Utiliser une fiole d'Estus automatiquement si le joueur est en dessous de 5 coeurs.
     * @param player : Le joueur à soigner.
     */
    public void autoUseEstus(Player player) {
        if (estus > 0 && player.getHealth() < 10) { // 5 coeurs = 10 points de vie
            estus--; // Retirer une fiole
            double health = player.getHealth();
            player.setHealth(Math.min(health + 6, 20)); // Soigner 3 coeurs (6 points de vie)
            player.sendMessage("§aVous avez utilisé une fiole d'Estus !");
        }
    }

    /**
     * Utiliser une fiole de Mana automatiquement si le joueur est en dessous de 50 mana.
     * @param player : Le joueur à régénérer.
     */
    public void autoUseMana(Player player) {
        if (mana > 0 && ManaManager.getInstance().getMana(player) < 50) {
            mana--; // Retirer une fiole
            int currentMana = ManaManager.getInstance().getMana(player);
            ManaManager.getInstance().setMana(player, Math.min(currentMana + 90, 100)); // Restaurer 90 mana
            player.sendMessage("§aVous avez utilisé une fiole de Mana !");
        }
    }

    /**
     * Réinitialiser les fioles (appelé à la fin de la nuit).
     */
    public void resetFlasks() {
        estus = 0;
        mana = 0;
        hasChargesLeft = false;
    }

    /**
     * Ajouter des fioles d'Estus.
     * @param amount : Le nombre de fioles à ajouter.
     */
    public void addEstus(int amount) {
        estus += amount;
    }

    /**
     * Ajouter des fioles de Mana.
     * @param amount : Le nombre de fioles à ajouter.
     */
    public void addMana(int amount) {
        mana += amount;
    }

    /**
     * Définir si le joueur a des charges restantes dans son dernier feu de camp.
     * @param hasChargesLeft : true s'il reste des charges, sinon false.
     */
    public void setHasChargesLeft(boolean hasChargesLeft) {
        this.hasChargesLeft = hasChargesLeft;
    }

    // Getters

    /**
     * Obtenir le nombre de fioles d'Estus.
     * @return Le nombre de fioles d'Estus.
     */
    public int getEstus() {
        return estus;
    }

    /**
     * Obtenir le nombre de fioles de Mana.
     * @return Le nombre de fioles de Mana.
     */
    public int getMana() {
        return mana;
    }

    /**
     * Savoir si le joueur a des charges restantes dans son dernier feu de camp.
     * @return true s'il reste des charges, sinon false.
     */
    public boolean hasChargesLeft() {
        return hasChargesLeft;
    }

    /**
     * Définir le nombre de fioles d'Estus.
     * @param estus : Le nombre de fioles d'Estus à définir.
     */
    public void setEstus(int estus) {
        this.estus = estus;
    }

    /**
     * Définir le nombre de fioles de Mana.
     * @param mana : Le nombre de fioles de Mana à définir.
     */
    public void setMana(int mana) {
        this.mana = mana;
    }
}
