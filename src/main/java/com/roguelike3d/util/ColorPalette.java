package com.roguelike3d.util;

import java.awt.Color;

/**
 * Color palette constants used throughout the game.
 * Centralized color management to reduce object allocation and ensure consistency.
 */
public class ColorPalette {
    private ColorPalette() {
        // Utility class, no instantiation
    }

    // UI Colors
    public static final Color HUD_GREEN = new Color(0, 200, 0);
    public static final Color HUD_RED = new Color(255, 50, 50);
    public static final Color HUD_YELLOW = new Color(255, 255, 0);
    public static final Color HUD_DARK_GRAY = new Color(64, 64, 64);
    public static final Color HUD_BLACK = new Color(0, 0, 0);
    public static final Color HUD_WHITE = new Color(255, 255, 255);

    // Background Colors
    public static final Color BG_DARK_BLUE = new Color(25, 25, 75);
    public static final Color BG_DARK_GRAY = new Color(20, 20, 20);

    // Minimap Colors
    public static final Color MINIMAP_WALL_GRAY = new Color(64, 64, 64);
    public static final Color MINIMAP_PLAYER_GREEN = new Color(0, 255, 0);
    public static final Color MINIMAP_STAIR_YELLOW = new Color(255, 255, 0);
    public static final Color MINIMAP_ENEMY_RED = new Color(255, 0, 0);
    public static final Color MINIMAP_ITEM_CYAN = new Color(0, 255, 255);

    // Game Over Screen Colors
    public static final Color GAMEOVER_GAME_OVER_RED = new Color(200, 0, 0);
    public static final Color GAMEOVER_SCORE_WHITE = new Color(255, 255, 255);
    public static final Color GAMEOVER_CONTINUE_YELLOW = new Color(200, 200, 0);

    // Rendering Colors
    public static final Color SPRITE_SHADOW_BLACK = new Color(0, 0, 0);
}
