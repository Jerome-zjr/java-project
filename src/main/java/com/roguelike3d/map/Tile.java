package com.roguelike3d.map;

/**
 * Every cell type the dungeon grid can hold.
 */
public enum Tile {
    FLOOR,
    WALL,
    STAIRS_DOWN;

    public boolean isWalkable() {
        return this != WALL;
    }

    public boolean isOpaque() {
        return this == WALL;
    }
}
