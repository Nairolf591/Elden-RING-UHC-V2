package main.game;

import org.bukkit.entity.Player;

import main.game.ManaManager;

public class PlayerFlasks {
    private int estus; // Nombre de fioles d'Estus
    private int mana;  // Nombre de fioles de Mana
    private static final int MAX_FLASKS = 2; // Limite maximale de fioles (1 Estus et 1 Mana, ou 2 Estus)

    public PlayerFlasks() {
        this.estus = 0;
        this.mana = 0;
    }

    /**
     * Ajouter une fiole d'Estus.
     * @param player : Le joueur à qui ajouter la fiole.
     * @return true si la fiole a été ajoutée, false si la limite est atteinte.
     */
    public boolean addEstus(Player player) {
        if (estus < 2 || (estus + mana) < MAX_FLASKS) {
            estus++;
            player.sendMessage("§aVous avez récupéré une fiole d'Estus !");
            return true;
        } else {
            player.sendMessage("§cVous avez déjà atteint la limite de fioles d'Estus.");
            return false;
        }
    }

    /**
     * Ajouter une fiole de Mana.
     * @param player : Le joueur à qui ajouter la fiole.
     * @return true si la fiole a été ajoutée, false si la limite est atteinte.
     */
    public boolean addMana(Player player) {
        if (mana < 2 || (estus + mana) < MAX_FLASKS) {
            mana++;
            player.sendMessage("§aVous avez récupéré une fiole de Mana !");
            return true;
        } else {
            player.sendMessage("§cVous avez déjà atteint la limite de fioles de Mana.");
            return false;
        }
    }

    /**
     * Utiliser une fiole d'Estus automatiquement si le joueur est en dessous de 5 cœurs.
     * @param player : Le joueur à soigner.
     * @param isNight : true si c'est la nuit, false sinon.
     */
    public void autoUseEstus(Player player, boolean isNight) {
        if (estus > 0 && player.getHealth() < 10) { // 5 cœurs = 10 points de vie
            estus--;
            double healAmount = isNight ? 1.5 : 3; // 0,75 cœurs la nuit, 1,5 cœurs le jour
            player.setHealth(Math.min(player.getHealth() + healAmount, 20));
            player.sendMessage("§aVous avez utilisé une fiole d'Estus !");
        }
    }

    /**
     * Utiliser une fiole de Mana automatiquement si le joueur est en dessous de 50 mana.
     * @param player : Le joueur à régénérer.
     */
    public void autoUseMana(Player player) {
        if (mana > 0 && ManaManager.getInstance().getMana(player) < 50) {
            mana--;
            int currentMana = ManaManager.getInstance().getMana(player);
            ManaManager.getInstance().setMana(player, Math.min(currentMana + 90, 100)); // Restaurer 90 mana
            player.sendMessage("§aVous avez utilisé une fiole de Mana !");
        }
    }

    /**
     * Vider les fioles à la fin de la nuit.
     */
    public void resetFlasks() {
        estus = 0;
        mana = 0;
    }

    // Getters
    public int getEstus() {
        return estus;
    }

    public int getMana() {
        return mana;
    }
}
