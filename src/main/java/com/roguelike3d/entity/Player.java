package com.roguelike3d.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * The player-controlled character.
 * Tracks viewing angle, current floor and score in addition to base stats.
 */
public class Player extends Entity {

    private double angle;      // radians – 0 = facing +X (east)
    private int    floor;
    private int    score;
    private final List<String> inventory;   // simple name list for HUD display

    public Player(double x, double y) {
        super(x, y, 100, 15, 3);
        this.angle     = 0.0;
        this.floor     = 1;
        this.score     = 0;
        this.inventory = new ArrayList<>();
    }

    public double getAngle()           { return angle; }
    public void   setAngle(double a)   { this.angle = a; }

    public int  getFloor()             { return floor; }
    public void nextFloor()            { floor++; score += 100; }

    public int  getScore()             { return score; }
    public void addScore(int v)        { score += v; }

    public List<String> getInventory() { return inventory; }
    public void addInventoryEntry(String name) { inventory.add(name); }
}
