package main.game;

import java.util.HashMap;
import java.util.Map;

public class RoleManager {

    private static RoleManager instance; // Instance du singleton
    private Map<Role, Boolean> roleStates; // États des rôles (true = activé, false = désactivé)

    private RoleManager() {
        roleStates = new HashMap<>();
        // Initialiser tous les rôles comme désactivés par défaut
        for (Role role : Role.values()) {
            roleStates.put(role, false);
        }
    }

    // Méthode pour obtenir l'instance du singleton
    public static RoleManager getInstance() {
        if (instance == null) {
            instance = new RoleManager();
        }
        return instance;
    }

    // Activer ou désactiver un rôle
    public void toggleRole(Role role) {
        boolean currentState = roleStates.get(role);
        roleStates.put(role, !currentState);
    }

    // Vérifier si un rôle est activé
    public boolean isRoleEnabled(Role role) {
        return roleStates.getOrDefault(role, false);
    }
}
