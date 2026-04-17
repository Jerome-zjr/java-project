package com.roguelike3d.item;

import java.awt.Color;

/**
 * All collectible item varieties.
 * New items need only a constant added here.
 */
public enum ItemType {

    HEALTH_POTION   ("Health Potion",    "Restores 40 HP",         new Color(255,  80,  80), 0),
    STRENGTH_POTION ("Strength Potion",  "Increases ATK by 5",     new Color(255, 160,  40), 5),
    ARMOR_SHARD     ("Armor Shard",      "Increases DEF by 2",     new Color( 80, 140, 255), 0);

    public final String name;
    public final String description;
    public final Color  color;
    /** Extra attack added to player on pickup (0 = none). */
    public final int    attackBonus;

    ItemType(String name, String description, Color color, int attackBonus) {
        this.name        = name;
        this.description = description;
        this.color       = color;
        this.attackBonus = attackBonus;
    }
}
