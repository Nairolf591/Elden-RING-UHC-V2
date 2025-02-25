package main.game;

import org.bukkit.entity.Player;

public class PlayerData {
    private final Player player; // Joueur
    private Camp camp;           // Camp du joueur
    private Role role;           // Rôle du joueur

    public PlayerData(Player player) {
        this.player = player;
        this.camp = null; // Aucun camp assigné par défaut
        this.role = null; // Aucun rôle assigné par défaut
    }

    // Getter et Setter pour le camp
    public Camp getCamp() {
        return camp;
    }

    public void setCamp(Camp camp) {
        this.camp = camp;
        player.sendMessage(camp.getColor() + "Tu as rejoint le camp : " + camp.getName());
        player.sendMessage(camp.getColor() + "Description : " + camp.getDescription());
    }

    // Getter et Setter pour le rôle
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
        player.sendMessage(role.getCamp().getColor() + "Tu es maintenant : " + role.getName());
        player.sendMessage(role.getCamp().getColor() + "Description : " + role.getDescription());
        player.sendMessage(role.getCamp().getColor() + "Pouvoirs : " + role.getPowers());
    }

    // Vérifie si le joueur est un demi-dieu ou un solitaire
    public boolean isDemigodOrSolo() {
        return camp == Camp.DEMI_DIEUX || camp == Camp.SOLITAIRE;
    }
}
