package com.roguelike3d.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Procedurally generates a dungeon floor using random room placement
 * connected by L-shaped corridors (BSP-lite approach).
 */
public class MapGenerator {

    private static final int MAP_W       = 40;
    private static final int MAP_H       = 40;
    private static final int MIN_ROOM    = 4;
    private static final int MAX_ROOM    = 10;
    private static final int MAX_ROOMS   = 18;
    private static final int MAX_TRIES   = 200;
    private static final float ENEMY_SPAWN_PROBABILITY = 0.70f;
    private static final float ITEM_SPAWN_PROBABILITY  = 0.40f;

    private final Random rng;

    public MapGenerator()          { this.rng = new Random(); }
    public MapGenerator(long seed) { this.rng = new Random(seed); }

    // ---------------------------------------------------------------
    // Public API

    public record Room(int x, int y, int w, int h) {
        int cx() { return x + w / 2; }
        int cy() { return y + h / 2; }

        boolean overlaps(Room o) {
            return x < o.x + o.w + 1 &&
                   x + w + 1 > o.x   &&
                   y < o.y + o.h + 1 &&
                   y + h + 1 > o.y;
        }
    }

    public record GenerationResult(
            DungeonMap   map,
            int[]        playerStart,
            int[]        stairsPos,
            List<int[]>  enemySpawns,
            List<int[]>  itemSpawns) {}

    public GenerationResult generate() {
        DungeonMap   map   = new DungeonMap(MAP_W, MAP_H);
        List<Room>   rooms = new ArrayList<>();

        for (int tries = 0; tries < MAX_TRIES && rooms.size() < MAX_ROOMS; tries++) {
            int rw = MIN_ROOM + rng.nextInt(MAX_ROOM - MIN_ROOM + 1);
            int rh = MIN_ROOM + rng.nextInt(MAX_ROOM - MIN_ROOM + 1);
            int rx = 1 + rng.nextInt(MAP_W - rw - 2);
            int ry = 1 + rng.nextInt(MAP_H - rh - 2);
            Room room = new Room(rx, ry, rw, rh);

            // Performance: use traditional loop instead of stream for early exit
            boolean overlaps = false;
            for (Room r : rooms) {
                if (room.overlaps(r)) {
                    overlaps = true;
                    break;
                }
            }
            if (overlaps) continue;

            carveRoom(map, room);
            if (!rooms.isEmpty()) {
                Room prev = rooms.get(rooms.size() - 1);
                carveCorridor(map, prev.cx(), prev.cy(), room.cx(), room.cy());
            }
            rooms.add(room);
        }
        if (rooms.isEmpty()) {
            int rw = 6;
            int rh = 6;
            int rx = (MAP_W - rw) / 2;
            int ry = (MAP_H - rh) / 2;
            Room fallback = new Room(rx, ry, rw, rh);
            carveRoom(map, fallback);
            rooms.add(fallback);
        }

        // Stairs in the last room
        Room startRoom = rooms.get(0);
        Room endRoom   = rooms.get(rooms.size() - 1);
        map.setTile(endRoom.cx(), endRoom.cy(), Tile.STAIRS_DOWN);

        int[] playerStart = { startRoom.cx(), startRoom.cy() };
        int[] stairsPos   = { endRoom.cx(),   endRoom.cy()   };

        // Enemy spawns – one per middle room (70 % chance)
        List<int[]> enemySpawns = new ArrayList<>();
        for (int i = 1; i < rooms.size() - 1; i++) {
            if (rng.nextFloat() < ENEMY_SPAWN_PROBABILITY) {
                Room r = rooms.get(i);
                enemySpawns.add(new int[]{ r.cx(), r.cy() });
            }
        }

        // Item spawns – random position inside room (40 % chance per room)
        List<int[]> itemSpawns = new ArrayList<>();
        for (Room r : rooms) {
            if (rng.nextFloat() < ITEM_SPAWN_PROBABILITY) {
                // Ensure room is large enough to spawn items safely (minimum 3x3)
                if (r.w >= 3 && r.h >= 3) {
                    int ix = r.x + 1 + rng.nextInt(r.w - 2);
                    int iy = r.y + 1 + rng.nextInt(r.h - 2);
                    itemSpawns.add(new int[]{ ix, iy });
                }
            }
        }

        return new GenerationResult(map, playerStart, stairsPos, enemySpawns, itemSpawns);
    }

    // ---------------------------------------------------------------
    // Helpers

    private void carveRoom(DungeonMap map, Room r) {
        for (int x = r.x; x < r.x + r.w; x++) {
            for (int y = r.y; y < r.y + r.h; y++) {
                map.setTile(x, y, Tile.FLOOR);
            }
        }
    }

    /** L-shaped corridor, 3 tiles wide: horizontal first, then vertical. */
    private void carveCorridor(DungeonMap map, int x1, int y1, int x2, int y2) {
        int x = x1;
        while (x != x2) {
            for (int dy = -1; dy <= 1; dy++) map.setTile(x, y1 + dy, Tile.FLOOR);
            x += (x2 > x) ? 1 : -1;
        }
        int y = y1;
        while (y != y2) {
            for (int dx = -1; dx <= 1; dx++) map.setTile(x2 + dx, y, Tile.FLOOR);
            y += (y2 > y) ? 1 : -1;
        }
        // Endpoint block (covers the corner junction too)
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) map.setTile(x2 + dx, y2 + dy, Tile.FLOOR);
        }
    }
}
