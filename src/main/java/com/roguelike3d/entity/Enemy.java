package com.roguelike3d.entity;

import com.roguelike3d.map.DungeonMap;

/**
 * An enemy NPC that chases and attacks the player.
 * All behaviour is driven by two timers so the game loop stays frame-rate
 * independent.
 */
public class Enemy extends Entity {

    private static final double CHASE_RANGE  = 12.0;   // tiles
    private static final double ATTACK_RANGE =  1.2;   // tiles
    private static final double ATTACK_RATE  =  1.5;   // attacks per second

    private final EnemyType type;
    private double moveTimer;
    private double attackTimer;

    public Enemy(double x, double y, EnemyType type) {
        super(x, y, type.maxHp, type.attack, type.defense);
        this.type        = type;
        this.moveTimer   = 0;
        this.attackTimer = 0;
    }

    /**
     * Called once per game-loop tick.
     * @return damage dealt to the player this tick (0 if no attack)
     */
    public int update(double delta, Player player, DungeonMap map) {
        if (!alive) return 0;

        double dx   = player.getX() - x;
        double dy   = player.getY() - y;
        double dist = Math.sqrt(dx * dx + dy * dy);

        // Movement
        moveTimer += delta;
        double moveInterval = 1.0 / type.speed;
        if (moveTimer >= moveInterval) {
            moveTimer = 0;
            if (dist <= CHASE_RANGE && dist > ATTACK_RANGE) {
                moveToward(player, map, dx, dy, dist);
            }
        }

        // Attack
        attackTimer += delta;
        if (dist <= ATTACK_RANGE && attackTimer >= 1.0 / ATTACK_RATE) {
            attackTimer = 0;
            return Math.max(1, attack - player.getDefense());
        }
        return 0;
    }

    private void moveToward(Player player, DungeonMap map,
                            double dx, double dy, double dist) {
        if (dist < 0.001) return;

        // Prefer the dominant axis; try the other axis as fallback
        int stepX = (int) Math.signum(dx);
        int stepY = (int) Math.signum(dy);

        if (Math.abs(dx) >= Math.abs(dy)) {
            if (map.isWalkable((int)(x + stepX), (int) y) && !occupiedByPlayer(player, (int)(x + stepX), (int)y))
                x += stepX;
            else if (stepY != 0 && map.isWalkable((int) x, (int)(y + stepY)) && !occupiedByPlayer(player, (int)x, (int)(y + stepY)))
                y += stepY;
        } else {
            if (stepY != 0 && map.isWalkable((int) x, (int)(y + stepY)) && !occupiedByPlayer(player, (int)x, (int)(y + stepY)))
                y += stepY;
            else if (map.isWalkable((int)(x + stepX), (int) y) && !occupiedByPlayer(player, (int)(x + stepX), (int)y))
                x += stepX;
        }
    }

    private boolean occupiedByPlayer(Player p, int tx, int ty) {
        return (int) p.getX() == tx && (int) p.getY() == ty;
    }

    public EnemyType getType() { return type; }
}
