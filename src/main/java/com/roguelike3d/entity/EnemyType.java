package com.roguelike3d.entity;

import java.awt.Color;

/**
 * Defines the stats and appearance of every enemy variant.
 * Adding a new enemy type requires only a new enum constant here.
 */
public enum EnemyType {

    SKELETON("Skeleton", 20,  5, 1, new Color(200, 200, 200), 0.8, 50),
    ZOMBIE  ("Zombie",   35,  8, 3, new Color( 80, 160,  80), 0.5, 80),
    DEMON   ("Demon",    55, 18, 6, new Color(180,  40,  40), 0.7, 150);

    public final String name;
    public final int    maxHp;
    public final int    attack;
    public final int    defense;
    public final Color  color;
    /** Steps per second (how often the enemy takes a move action). */
    public final double speed;
    public final int    scoreValue;

    EnemyType(String name, int maxHp, int attack, int defense,
              Color color, double speed, int scoreValue) {
        this.name       = name;
        this.maxHp      = maxHp;
        this.attack     = attack;
        this.defense    = defense;
        this.color      = color;
        this.speed      = speed;
        this.scoreValue = scoreValue;
    }
}
