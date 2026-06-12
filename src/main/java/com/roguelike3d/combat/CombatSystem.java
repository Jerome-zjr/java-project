package com.roguelike3d.combat;

import com.roguelike3d.entity.Entity;
import java.util.Random;

/**
 * Stateless utility that handles damage calculation between two entities.
 * Adds ±20 % random variance so combat feels less predictable.
 */
public final class CombatSystem {

    private static final Random RNG = new Random();

    private CombatSystem() {}

    /**
     * Attacker hits defender.
     * Defense reduction is applied inside {@link Entity#takeDamage(int)}.
     * @return the actual HP removed from the defender
     */
    public static int attack(Entity attacker, Entity defender) {
        double var = 0.80 + RNG.nextDouble() * 0.40;   // [0.80 – 1.20]
        int damage = (int) Math.round(Math.max(1, attacker.getAttack()) * var);
        int hpBefore = defender.getHp();
        defender.takeDamage(damage);
        return hpBefore - defender.getHp();   // actual HP removed after defense
    }
}
