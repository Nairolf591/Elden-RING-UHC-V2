// src/main/java/main/game/PlayerFlasks.java (Avec limites)
package main.game;

import org.bukkit.entity.Player;

public class PlayerFlasks {
    private int estus;
    private int mana;
    private static final int MAX_FLASKS = 2;  // Limite totale

    public PlayerFlasks() {
        this.estus = 0;
        this.mana = 0;
    }
    /**
     * Ajouter une fiole d'Estus.
     * @param player : Le joueur à qui ajouter la fiole.
     */
    public boolean addEstus(Player player) {
        if (estus < 2 && estus+mana < MAX_FLASKS) {
            estus++;
            player.sendMessage("§aVous avez récupéré une fiole d'Estus !");
            return true; //Retourne si l'ajout est fait ou non
        }
        else
        {
            player.sendMessage("§cVous avez atteint la limite maximal de fioles !");
            return false;
        }
    }
    /**
     * Ajouter une fiole de Mana.
     * @param player : Le joueur à qui ajouter la fiole.
     */
    public boolean addMana(Player player) {
        if(mana < 2 && estus+mana < MAX_FLASKS) {
            mana++;
            player.sendMessage("§aVous avez récupéré une fiole de Mana !");
            return true; //Retourne si l'ajout est fait ou non
        }
        else
        {
            player.sendMessage("§cVous avez atteint la limite maximal de fioles !");
            return false; //Retourne si l'ajout est fait ou non
        }
    }


    public void autoUseEstus(Player player, boolean isDay) {
        if (estus > 0 && player.getHealth() < 10) {
            estus--;
            double healAmount = isDay ? 3 : 1.5;  // Ajusté à la description
            player.setHealth(Math.min(player.getHealth() + healAmount, player.getMaxHealth())); //Important pour ne pas dépasser la vie max
            player.sendMessage("§aVous avez utilisé une fiole d'Estus !");
        }
    }

    public void autoUseMana(Player player) {
        if (mana > 0 && ManaManager.getInstance().getMana(player) < 50) {
            mana--;
            ManaManager.getInstance().setMana(player, ManaManager.getInstance().getMana(player) + 90); //Complément de mana
            player.sendMessage("§aVous avez utilisé une fiole de Mana !");
        }
    }

    //Plus besoin car géré dans CampfireManager
    /*public void resetFlasks() {
        estus = 0;
        mana = 0;
    }*/

    // Getters, important pour l'affichage, la sauvegarde, etc.
    public int getEstus() {
        return estus;
    }

    public int getMana() {
        return mana;
    }

    public void resetFlasks() { //REMIS
        estus = 0;
        mana = 0;
    }
}
