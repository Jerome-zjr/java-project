package com.roguelike3d.theme;

import java.awt.Color;

/**
 * Default theme singleton for fallback rendering when no theme is available.
 * This eliminates need for null checks throughout the codebase.
 */
public final class DefaultTheme {
    private static final FloorTheme DEFAULT = new FloorTheme(
        "Default",
        new Color(130, 95, 65),      // wallBase
        new Color(45, 45, 45),       // floor
        new Color(25, 25, 75),       // ceiling
        new Color(220, 190, 70),     // stairs
        new Color(100, 100, 100),    // enemyTint
        new Color(150, 150, 150),    // itemTint
        new Color(200, 200, 200),    // particleColor
        80,                           // particleCount
        1.2,                          // particleSpeed
        2,                            // particleMinSize
        4                             // particleMaxSize
    );

    private DefaultTheme() {
        // Utility class, no instantiation
    }

    /**
     * Get the default theme singleton.
     * @return default theme instance
     */
    public static FloorTheme get() {
        return DEFAULT;
    }
}
