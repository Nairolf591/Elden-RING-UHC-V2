// Nouveau fichier src/main/java/main/skills/PlayerClass.java
package main.skills;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class PlayerClass {

    public abstract void applyClass(Player player);

    public abstract void useSkill(Player player);

    public abstract ItemStack getClassItem();
}
