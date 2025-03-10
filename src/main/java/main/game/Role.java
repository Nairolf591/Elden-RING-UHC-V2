package main.game;

public enum Role {
    SANS_ECLAT(
            "Le Sans-Éclat",
            "Un humble chevalier sans gloire, mais au cœur pur.",
            Camp.BASTION_DE_LA_TABLE_RONDE
    ),
    RADAHN(
            "Radahn",
            "Un guerrier solitaire, maître des étoiles.",
            Camp.SOLITAIRE
    ),
    MORGOTT(
            "Morgott",
            "Un demi-dieu imposteur, roi de la nuit.",
            Camp.DEMI_DIEUX
    ),
    MELINA(
            "Melina",
            "Une mystérieuse jeune femme qui guide les Sans-Éclats.",
            Camp.BASTION_DE_LA_TABLE_RONDE
    );

    private final String name;        // Nom du rôle
    private final String description; // Description du rôle
    private final String powers;      // Pouvoirs du rôle
    private final Camp camp;          // Camp associé au rôle

    Role(String name, String description, String powers, Camp camp) {
        this.name = name;
        this.description = description;
        this.powers = powers;
        this.camp = camp;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPowers() {
        return powers;
    }

    public Camp getCamp() {
        return camp;
    }
}
