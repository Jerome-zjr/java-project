package com.roguelike3d;

/**
 * Centralized game configuration constants.
 * All tunable game parameters are defined here for easy modification.
 */
public final class GameConstants {
    private GameConstants() {
        // Utility class, no instantiation
    }

    // ── Screen ─────────────────────────────────────────────────────────────
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;
    public static final int TARGET_FPS = 60;

    // ── Movement & Physics ─────────────────────────────────────────────────
    /** Player movement speed (tiles per second) */
    public static final double MOVE_SPEED = 3.5;
    /** Player rotation speed (radians per second) */
    public static final double ROT_SPEED = 2.2;
    /** Collision detection margin for wall sliding (tiles) */
    public static final double COLLISION_MARGIN = 0.28;

    // ── Combat ─────────────────────────────────────────────────────────────
    /** Cooldown between player swings (seconds) */
    public static final double ATTACK_COOLDOWN = 0.45;
    /** Attack range (tiles) - used as 1.6 tiles squared = 2.56 for performance */
    public static final double ATTACK_RANGE = 1.6;
    public static final double ATTACK_RANGE_SQ = ATTACK_RANGE * ATTACK_RANGE; // 2.56

    // ── Items ──────────────────────────────────────────────────────────────
    /** Distance (in tiles) within which an item is auto-collected */
    public static final double ITEM_PICKUP_RANGE = 0.80;
    public static final double ITEM_PICKUP_RANGE_SQ = ITEM_PICKUP_RANGE * ITEM_PICKUP_RANGE; // 0.64

    // ── Stairs ─────────────────────────────────────────────────────────────
    /** Distance (in tiles) from stairs centre within which F triggers descent */
    public static final double STAIRS_TRIGGER_RANGE = 1.5;
    public static final double STAIRS_TRIGGER_RANGE_SQ = STAIRS_TRIGGER_RANGE * STAIRS_TRIGGER_RANGE; // 2.25
    /** Minimum seconds between consecutive floor transitions (cooldown) */
    public static final double STAIRS_COOLDOWN = 0.5;

    // ── Difficulty ────────────────────────────────────────────────────────
    /** Enemy type tier advances every N floors */
    public static final int FLOORS_PER_ENEMY_TIER = 3;
    /** Total number of floors required to clear the game.
     *  NOTE: Must be kept in sync with FloorThemes.MAX_FLOOR to ensure theme availability. */
    public static final int MAX_FLOOR = 18;

    // ── Rendering ─────────────────────────────────────────────────────────
    /** Minimap tile size (pixels per game tile) */
    public static final int MINIMAP_TILE_SIZE = 4;
}
