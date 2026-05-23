package com.roguelike3d.map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DungeonMapTest {

    @Test
    void outOfBoundsReadsAsWall() {
        DungeonMap map = new DungeonMap(3, 3);
        assertEquals(Tile.WALL, map.getTile(-1, 0));
        assertEquals(Tile.WALL, map.getTile(0, -1));
        assertEquals(Tile.WALL, map.getTile(3, 0));
        assertEquals(Tile.WALL, map.getTile(0, 3));
    }

    @Test
    void setTileIgnoresOutOfBounds() {
        DungeonMap map = new DungeonMap(2, 2);
        map.setTile(5, 5, Tile.FLOOR); // should not throw
        assertEquals(Tile.WALL, map.getTile(0, 0));
    }

    @Test
    void walkabilityReflectsTile() {
        DungeonMap map = new DungeonMap(2, 2);
        assertFalse(map.isWalkable(0, 0));

        map.setTile(0, 0, Tile.FLOOR);
        assertTrue(map.isWalkable(0, 0));

        map.setTile(0, 0, Tile.STAIRS_DOWN);
        assertTrue(map.isWalkable(0, 0));
    }
}

