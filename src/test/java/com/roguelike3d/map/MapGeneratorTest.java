package com.roguelike3d.map;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MapGeneratorTest {

    @Test
    void generateCreatesWalkableStartAndExactlyOneStairsTile() {
        MapGenerator.GenerationResult result = new MapGenerator(123L).generate();

        DungeonMap map = result.map();
        assertEquals(40, map.getWidth());
        assertEquals(40, map.getHeight());

        int[] start = result.playerStart();
        assertTrue(start[0] >= 0 && start[0] < map.getWidth());
        assertTrue(start[1] >= 0 && start[1] < map.getHeight());
        assertTrue(map.isWalkable(start[0], start[1]));

        int[] stairs = findStairs(map);
        assertNotNull(stairs, "Expected STAIRS_DOWN to exist");
        assertEquals(Tile.STAIRS_DOWN, map.getTile(stairs[0], stairs[1]));
        assertTrue(map.isWalkable(stairs[0], stairs[1]));

        assertEquals(1, countTile(map, Tile.STAIRS_DOWN));
    }

    @Test
    void generatePlacesSpawnsOnWalkableTilesWithinBounds() {
        MapGenerator.GenerationResult result = new MapGenerator(999L).generate();
        DungeonMap map = result.map();

        assertAllSpawnsValid(map, result.enemySpawns());
        assertAllSpawnsValid(map, result.itemSpawns());
    }

    @Test
    void generationIsReproducibleWithSameSeed() {
        long seed = 42L;
        MapGenerator.GenerationResult a = new MapGenerator(seed).generate();
        MapGenerator.GenerationResult b = new MapGenerator(seed).generate();

        assertArrayEquals(a.playerStart(), b.playerStart());
        assertEquals(findStairsKey(a.map()), findStairsKey(b.map()));
        assertSpawnListsEqual(a.enemySpawns(), b.enemySpawns());
        assertSpawnListsEqual(a.itemSpawns(), b.itemSpawns());
    }

    private static void assertAllSpawnsValid(DungeonMap map, List<int[]> spawns) {
        for (int[] p : spawns) {
            assertNotNull(p);
            assertEquals(2, p.length);
            assertTrue(p[0] >= 0 && p[0] < map.getWidth());
            assertTrue(p[1] >= 0 && p[1] < map.getHeight());
            assertTrue(map.isWalkable(p[0], p[1]));
        }
    }

    private static int countTile(DungeonMap map, Tile tile) {
        int count = 0;
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                if (map.getTile(x, y) == tile) count++;
            }
        }
        return count;
    }

    private static int[] findStairs(DungeonMap map) {
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                if (map.getTile(x, y) == Tile.STAIRS_DOWN) return new int[] { x, y };
            }
        }
        return null;
    }

    private static String findStairsKey(DungeonMap map) {
        int[] stairs = findStairs(map);
        assertNotNull(stairs, "Expected STAIRS_DOWN to exist");
        return stairs[0] + "," + stairs[1];
    }

    private static void assertSpawnListsEqual(List<int[]> a, List<int[]> b) {
        assertEquals(a.size(), b.size());
        for (int i = 0; i < a.size(); i++) {
            assertArrayEquals(a.get(i), b.get(i), "Spawn mismatch at index " + i);
        }
    }
}

