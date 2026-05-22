package com.roguelike3d.theme;

import java.awt.Color;

public record FloorTheme(
        String name,
        Color wallBase,
        Color floor,
        Color ceiling,
        Color stairs,
        Color enemyTint,
        Color itemTint,
        Color particleColor,
        int particleCount,
        double particleSpeed,
        int particleMinSize,
        int particleMaxSize) {
}
