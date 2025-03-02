package main.game;

public enum Role {
    SANS_ECLAT(
            "Le Sans-Éclat",
            "Un humble chevalier sans gloire, mais au cœur pur.",
            "§7Aucun pouvoir spécial pour l'instant.",
            Camp.BASTION_DE_LA_TABLE_RONDE
    ),
    RADAHN(
            "Radahn",
            "Un guerrier solitaire, maître des étoiles.",
            "§7Aucun pouvoir spécial pour l'instant.",
            Camp.SOLITAIRE
    ),
    MORGOTT(
            "Morgott",
            "Un demi-dieu imposteur, roi de la nuit.",
            "§7Aucun pouvoir spécial pour l'instant.",
            Camp.DEMI_DIEUX
    ),
    MELINA(
        "Melina",
                "L'alliée des Sans-Éclats, capable de guider et renforcer leur pouvoir.",
                "§7- §3§lCendre de Guerre : Lame d'appel : §r Inflige des dégâts purs et marque brièvement les ennemis. (§e50 Mana§f)\n" +
                "§7- §3§lCompétence : Bénédiction de l'Arbre-Monde :§r Restaure points de mana et points de vie aux alliés proches. (§e60 Mana, 10min Cooldown§f)\n" +
                "§7- §3§lCommande : /melina presence <joueur> <camp> :§r Permet d'obtenir une information pour savoir si un joueur est dans le camp visé, utilisable chaque début de journée durant 2 minutes.",
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
