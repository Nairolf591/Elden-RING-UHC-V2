package main.game;

import org.bukkit.ChatColor;

public enum Camp {
    BASTION_DE_LA_TABLE_RONDE(
            "Bastion de la Table Ronde",
            "Les chevaliers unis pour restaurer l'ordre.",
            ChatColor.GREEN
    ),
    DEMI_DIEUX(
            "Demi-dieux",
            "Des êtres immortels cherchant à dominer Elden Ring.",
            ChatColor.RED
    ),
    SOLITAIRE(
            "Solitaire",
            "Un aventurier solitaire, sans allié.",
            ChatColor.YELLOW
    );

    private final String name;        // Nom du camp
    private final String description; // Description du camp
    private final ChatColor color;    // Couleur associée au camp

    Camp(String name, String description, ChatColor color) {
        this.name = name;
        this.description = description;
        this.color = color;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ChatColor getColor() {
        return color;
    }
}
