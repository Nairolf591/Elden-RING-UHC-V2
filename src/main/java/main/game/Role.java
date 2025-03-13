package main.game;

import main.game.Camp;

public enum Role {
    SANS_ECLAT("Le Sans-Éclat", "Un humble chevalier sans gloire, mais au cœur pur.", Camp.BASTION_DE_LA_TABLE_RONDE),
    RADAHN("Radahn", "Un guerrier solitaire, maître des étoiles.", Camp.SOLITAIRE),
    MORGOTT("Morgott", "Un demi-dieu imposteur, roi de la nuit.", Camp.DEMI_DIEUX),
    MELINA("Melina", "Une mystérieuse jeune femme qui guide les Sans-Éclats.", Camp.BASTION_DE_LA_TABLE_RONDE);

    private final String name;
    private final String description;
    private final Camp camp;

    Role(String name, String description, Camp camp) {
        this.name = name;
        this.description = description;
        this.camp = camp;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Camp getCamp() {
        return camp;
    }
}
