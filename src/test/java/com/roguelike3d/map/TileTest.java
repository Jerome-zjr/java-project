package com.roguelike3d.map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TileTest {

    @Test
    void walkabilityAndOpacityMatchDefinitions() {
        assertTrue(Tile.FLOOR.isWalkable());
        assertFalse(Tile.FLOOR.isOpaque());

        assertFalse(Tile.WALL.isWalkable());
        assertTrue(Tile.WALL.isOpaque());

        assertTrue(Tile.STAIRS_DOWN.isWalkable());
        assertFalse(Tile.STAIRS_DOWN.isOpaque());
    }
}

