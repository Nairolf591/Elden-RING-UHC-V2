package main.game;

public enum GameState {
    CONFIG("§eConfiguration"), // État pour configurer le jeu
    STARTING("§6Démarrage"),   // État avant le début du jeu (countdown, etc.)
    PLAYING("§2En cours"),     // État pendant que la partie est en cours
    END("§cFin");             // État à la fin de la partie

    private final String displayName; // Nom affiché de l'état

    GameState(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
